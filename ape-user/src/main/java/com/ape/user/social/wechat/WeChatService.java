package com.ape.user.social.wechat;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONUtil;
import com.ape.common.exception.ServiceException;
import com.ape.common.utils.StringUtils;
import com.ape.user.bo.UserBO;
import com.ape.user.mapper.RoleMapper;
import com.ape.user.mapstruct.UserDOMapper;
import com.ape.user.model.RoleDO;
import com.ape.user.model.SocialUserDetailDO;
import com.ape.user.model.UserDO;
import com.ape.user.service.SocialUserDetailService;
import com.ape.user.service.UserService;
import com.ape.user.service.impl.UserServiceImpl;
import com.ape.user.social.SocialService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.enums.AuthUserGender;
import me.zhyd.oauth.model.AuthUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/10/20
 */
@Slf4j
@Service
public class WeChatService {
    
    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.appSecret}")
    private String appSecret;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SocialUserDetailService socialUserDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 构建请求地址
     * @param code
     * @return
     */
    private String buildRequestUrl(String code){
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
        return String.format(url, appId, appSecret, code);
    }

    /**
     * 请求获取openID和sessionKey
     * @param code
     * @return
     */
    private Map<String, String> getOpenIdAndSessionKey(String code){
        String requestUrl = this.buildRequestUrl(code);
        String responseBody = restTemplate.getForObject(requestUrl, String.class);
        return JSONUtil.toBean(responseBody, Map.class);
    }

    /**
     * 校验数据签名
     * @param requestParam
     */
    private void checkSignature(Map<String, Object> requestParam){
        String rawData = (String) requestParam.get("rawData");
        String signature = (String) requestParam.get("signature");
        String sessionKey = (String) requestParam.get("session_key");

        String generateSignature = DigestUtils.sha1Hex(rawData + sessionKey);
        if (!StringUtils.equals(signature, generateSignature)){
            throw new ServiceException("登录校验失败");
        }
    }

    /**
     * 登录
     * @param requestParam
     * @return
     */
    public void login(Map<String, Object> requestParam){
        String code = (String) requestParam.get("code");
        Map<String, String> openIdAndSessionKey = this.getOpenIdAndSessionKey(code);
        requestParam.putAll(openIdAndSessionKey);
        this.checkSignature(requestParam);
    }

    /**
     * 获取用户数据
     * @param requestParam
     * @return
     */
    public Map<String, Object> getUserInfo(Map<String, Object> requestParam) {
        String encryptedData = (String) requestParam.get("encryptedData");
        String iv = (String) requestParam.get("iv");
        String sessionKey = (String) requestParam.get("session_key");

        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                Map map = JSONUtil.toBean(result, Map.class);
                // 微信新版登录解释的encryptedData好像没有openID
                if (!map.containsKey("openId")){
                    map.put("openId", requestParam.get("openid"));
                }
                return map;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public OAuth2AccessToken generateOAuth2AccessToken(Map<String, Object> userInfo) {
        AuthUser authUser = this.userInfoConvertedToAuthUser(userInfo);
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.eq("username", authUser.getUuid());
        UserDO detailDO = userService.getOne(wrapper);
        // 第一次登录进行注册
        if (StringUtils.isEmpty(detailDO)){
            SocialUserDetailDO socialUserDetail = new SocialUserDetailDO();
            socialUserDetail.setUuid(authUser.getUuid());
            socialUserDetail.setSource(authUser.getSource());
            socialUserDetailService.save(socialUserDetail);

            UserDO userDO = userService.registerSocialUser(authUser);
            UserBO userBO = UserDOMapper.INSTANCE.doToBO(userDO);
            userBO.setRoles(SocialService.buildRoleList());

            // 生成token
            return socialUserDetailService.generateToken(userBO);
        }else {
            // 已注册
            List<RoleDO> roles = roleMapper.searchAllRoleByUid(detailDO.getId());
            UserBO userBO = UserDOMapper.INSTANCE.doToBO(detailDO);
            userBO.setRoles(roles);
            ((UserServiceImpl) userService).loginException(userBO);
            return socialUserDetailService.generateToken(userBO);
        }
    }

    private AuthUser userInfoConvertedToAuthUser(Map<String, Object> userInfo){
        AuthUser authUser = new AuthUser();
        authUser.setUuid((String) userInfo.get("openId"));
        authUser.setUsername((String) userInfo.get("nickName"));
        authUser.setAvatar((String) userInfo.get("avatarUrl"));
        if ((int) userInfo.get("gender") == 0) {
            authUser.setGender(AuthUserGender.UNKNOWN);
        }else if ((int) userInfo.get("gender") == 1){
            authUser.setGender(AuthUserGender.MALE);
        }else if ((int) userInfo.get("gender") == 2){
            authUser.setGender(AuthUserGender.FEMALE);
        }
        authUser.setSource("WECHAT");
        return authUser;
    }

}
