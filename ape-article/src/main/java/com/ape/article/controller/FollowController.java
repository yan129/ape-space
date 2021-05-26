package com.ape.article.controller;


import com.ape.article.model.FollowDO;
import com.ape.article.service.FollowService;
import com.ape.common.model.ResultVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户关注表 前端控制器
 * </p>
 *
 * @author Yan
 * @since 2021-05-26
 */
@Api(value = "用户关注控制器", description = "用户关注控制器")
@Slf4j
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @ApiOperation(value = "关注用户", notes = "关注用户")
    @PostMapping("/save")
    public ResultVO<String> saveFollow(@RequestBody FollowDO followDO){
        followService.save(followDO);
        return ResultVO.OK("关注成功");
    }

    @ApiOperation(value = "取消关注", notes = "取消关注")
    @DeleteMapping("/delete")
    public ResultVO<String> deleteFollow(@ApiParam("ID") @RequestParam("id") String id){
        followService.removeById(id);
        return ResultVO.OK("已取消关注");
    }

    @ApiOperation(value = "根据用户ID分页查看关注用户", notes = "根据用户ID分页查看关注用户")
    @GetMapping("/search/{uid}")
    public ResultVO<List<Map<String, Object>>> searchFavorites(@ApiParam("用户ID") @PathVariable("uid") String uid,
                                                               @ApiParam("pageNum第几页") @RequestParam("pageNum") long pageNum,
                                                               @ApiParam("pageSize每页条数") @RequestParam("pageSize") long pageSize){
        List<Map<String, Object>> follows = followService.searchAllFollowByUid(pageNum, pageSize, uid);
        return ResultVO.OK(follows);
    }

    @ApiOperation(value = "统计关注用户总数", notes = "统计关注用户总数")
    @GetMapping("/count")
    public ResultVO<Integer> countFollowByUid(@ApiParam("用户ID") @RequestParam("uid") String uid) {
        QueryWrapper<FollowDO> wrapper = new QueryWrapper<>();
        wrapper.eq("current_uid", uid);
        int count = followService.count(wrapper);
        return ResultVO.OK(count);
    }
}
