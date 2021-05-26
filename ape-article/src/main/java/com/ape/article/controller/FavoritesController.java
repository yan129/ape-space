package com.ape.article.controller;


import com.ape.article.model.FavoritesDO;
import com.ape.article.service.FavoritesService;
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
 * 文章收藏表 前端控制器
 * </p>
 *
 * @author Yan
 * @since 2021-05-25
 */
@Api(value = "文章收藏控制器", description = "文章收藏控制器")
@Slf4j
@RestController
@RequestMapping("/favorites")
public class FavoritesController {

    @Autowired
    private FavoritesService favoritesService;

    @ApiOperation(value = "添加文章收藏", notes = "添加文章收藏")
    @PostMapping("/save")
    public ResultVO<String> saveFavorites(@RequestBody FavoritesDO favoritesDO){
        favoritesService.save(favoritesDO);
        return ResultVO.OK("收藏成功");
    }

    @ApiOperation(value = "取消收藏文章", notes = "取消收藏文章")
    @DeleteMapping("/delete")
    public ResultVO<String> deleteFavorites(@ApiParam("ID") @RequestParam("id") String id){
        favoritesService.removeById(id);
        return ResultVO.OK("已取消收藏");
    }

    @ApiOperation(value = "根据用户ID分页查看收藏文章", notes = "根据用户ID分页查看收藏文章")
    @GetMapping("/search/{uid}")
    public ResultVO<List<Map<String, Object>>> searchFavorites(@ApiParam("用户ID") @PathVariable("uid") String uid,
                                    @ApiParam("pageNum第几页") @RequestParam("pageNum") long pageNum,
                                    @ApiParam("pageSize每页条数") @RequestParam("pageSize") long pageSize){
        List<Map<String, Object>> favorites = favoritesService.searchAllFavoritesByUid(pageNum, pageSize, uid);
        return ResultVO.OK(favorites);
    }

    @ApiOperation(value = "统计收藏文章总数", notes = "统计收藏文章总数")
    @GetMapping("/count")
    public ResultVO<Integer> countFavoritesByUid(@ApiParam("用户ID") @RequestParam("uid") String uid){
        QueryWrapper<FavoritesDO> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid);
        int count = favoritesService.count(wrapper);
        return ResultVO.OK(count);
    }
}
