package com.ape.article.controller;


import com.ape.article.annotation.HtmlFilter;
import com.ape.article.model.ArticleDO;
import com.ape.article.service.ArticleService;
import com.ape.common.model.ResultVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author Yan
 * @since 2021-05-15
 */
@Api(value = "文章控制器", description = "文章控制器")
@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "新建空文章", notes = "新建空文章")
    @PostMapping("/create")
    public ResultVO<ArticleDO> createArticle(@ApiParam("用户ID") @RequestParam("uid") String uid){
        ArticleDO articleDO = articleService.create(uid);
        return ResultVO.OK(articleDO);
    }

    @ApiOperation(value = "发布文章", notes = "发布文章")
    @HtmlFilter
    @PostMapping("/publish")
    public ResultVO<String> publishArticle(@RequestBody @Valid ArticleDO articleDO){
        articleService.publish(articleDO);
        return ResultVO.OK("发布成功");
    }

    @ApiOperation(value = "保存文章至草稿箱", notes = "保存文章至草稿箱")
    @HtmlFilter
    @PostMapping("/save")
    public ResultVO<String> saveArticle(@RequestBody @Valid ArticleDO articleDO){
        articleDO.setIsPublish(false);
        QueryWrapper<ArticleDO> wrapper = buildArticleDOWrapper(articleDO.getId(), articleDO.getUid());
        articleService.update(wrapper);
        return ResultVO.OK("保存成功");
    }

    @ApiOperation(value = "根据文章ID查询", notes = "根据文章ID查询")
    @GetMapping("/search/{id}")
    public ResultVO<ArticleDO> searchArticle(@ApiParam("文章ID") @PathVariable("id") String id){
        ArticleDO articleDO = articleService.getById(id);
        return ResultVO.OK(articleDO);
    }

    @ApiOperation(value = "删除文章", notes = "删除文章")
    @DeleteMapping("/delete")
    public ResultVO<String> deleteArticle(@ApiParam("文章ID") @RequestParam("aid") String aid,
                                          @ApiParam("用户ID") @RequestParam("uid") String uid){
        QueryWrapper<ArticleDO> wrapper = buildArticleDOWrapper(aid, uid);
        boolean result = articleService.remove(wrapper);
        return result ? ResultVO.OK("删除成功") : ResultVO.ERROR("删除失败");
    }

    /**
     * 构建文章ID和用户ID的查询条件
     * @param id
     * @param uid
     * @return
     */
    private QueryWrapper<ArticleDO> buildArticleDOWrapper(String id, String uid) {
        QueryWrapper<ArticleDO> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("uid", uid);
        return wrapper;
    }
}
