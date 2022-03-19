package com.ape.article.controller;


import com.ape.article.model.ThemeDO;
import com.ape.article.service.ThemeService;
import com.ape.article.feign.OssServiceFeign;
import com.ape.common.model.ResultVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 专题表 前端控制器
 * </p>
 *
 * @author Yan
 * @since 2021-05-18
 */
@Api(value = "专题控制器", description = "专题控制器")
@Slf4j
@RestController
@RequestMapping("/theme")
public class ThemeController {

    @Autowired
    private ThemeService themeService;
    @Autowired
    private OssServiceFeign ossServiceFeign;

    @ApiOperation(value = "新建专题", notes = "新建专题")
    @PostMapping("/create")
    public ResultVO createTheme(@RequestBody @Valid ThemeDO themeDO){
        boolean save = themeService.save(themeDO);
        return save ? ResultVO.OK(themeDO).setMessage("新建成功") : ResultVO.ERROR().setMessage("新建失败");
    }

    @ApiOperation(value = "更新专题", notes = "更新专题")
    @PostMapping("/update")
    public ResultVO<String> updateTheme(@RequestBody @Valid ThemeDO themeDO){
        QueryWrapper<ThemeDO> wrapper = buildThemeDOWrapper(themeDO.getId(), themeDO.getUid());
        themeService.update(themeDO, wrapper);
        return ResultVO.OK("更新成功");
    }

    @ApiOperation(value = "根据专题ID查询某个专题", notes = "根据专题ID查询某个专题")
    @GetMapping("/search/{id}")
    public ResultVO<ThemeDO> searchTheme(@ApiParam("专题ID") @PathVariable("id") String id){
        ThemeDO themeDO = themeService.getById(id);
        return ResultVO.OK(themeDO);
    }

    @ApiOperation(value = "根据用户ID查询所有专题", notes = "根据用户ID查询所有专题")
    @GetMapping("/searchAll/{uid}")
    public ResultVO<List<ThemeDO>> searchAllTheme(@ApiParam("用户ID") @PathVariable("uid") String uid){
        QueryWrapper<ThemeDO> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("gmt_created");
        wrapper.eq("uid", uid);
        List<ThemeDO> themeDOList = themeService.list(wrapper);
        return ResultVO.OK(themeDOList);
    }

    @ApiOperation(value = "删除某个专题", notes = "删除某个专题")
    @DeleteMapping("/delete")
    public ResultVO deleteTheme(@ApiParam("专题ID") @RequestParam("id") String id,
                                        @ApiParam("用户ID") @RequestParam("uid") String uid){
        QueryWrapper<ThemeDO> wrapper = buildThemeDOWrapper(id, uid);
        boolean result = themeService.remove(wrapper);
        return result ? ResultVO.OK().setMessage("删除成功") : ResultVO.ERROR().setMessage("删除失败");
    }

    private QueryWrapper<ThemeDO> buildThemeDOWrapper(String id, String uid) {
        QueryWrapper<ThemeDO> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("uid", uid);
        return wrapper;
    }

    @ApiOperation(value = "上传专题封面图", notes = "上传专题封面图")
    @PostMapping("/uploadCoverImg")
    public ResultVO uploadCoverImg(@RequestPart("file") MultipartFile file) {
        ResultVO<String> themeImgInfo = ossServiceFeign.upload(file, "theme", true, false);
        return themeImgInfo.getSuccess() ? ResultVO.OK(themeImgInfo.getData()) : ResultVO.ERROR().setMessage(themeImgInfo.getData());
    }

    @ApiOperation(value = "删除专题封面图", notes = "删除专题封面图")
    @DeleteMapping("/deleteCoverImg")
    public ResultVO deleteCoverImg(String url) {
        return ossServiceFeign.delete(url);
    }

}
