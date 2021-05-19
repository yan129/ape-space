package com.ape.article.controller;

import com.ape.article.model.LabelDO;
import com.ape.article.service.LabelService;
import com.ape.common.model.ResultVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author Yan
 * @since 2021-05-19
 */
@Api(value = "标签控制器", description = "标签控制器")
@Slf4j
@RestController
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @ApiOperation(value = "新建标签", notes = "新建标签")
    @PostMapping("/create")
    public ResultVO<String> createLabel(@RequestBody @Valid LabelDO labelDO){
        labelService.save(labelDO);
        return ResultVO.OK("标签新建成功");
    }

    @ApiOperation(value = "更新标签", notes = "更新标签")
    @PostMapping("/update")
    public ResultVO<String> updateLabel(@RequestBody @Valid LabelDO labelDO){
        QueryWrapper<LabelDO> wrapper = buildLabelDOWrapper(labelDO.getId(), labelDO.getUid());
        labelService.update(labelDO, wrapper);
        return ResultVO.OK("标签更新成功");
    }

    @ApiOperation(value = "删除标签", notes = "删除标签")
    @DeleteMapping("/delete")
    public ResultVO<String> deleteLabel(@ApiParam("标签ID") @RequestParam("id") String id,
                                        @ApiParam("用户ID") @RequestParam("uid") String uid){
        QueryWrapper<LabelDO> wrapper = buildLabelDOWrapper(id, uid);
        boolean result = labelService.remove(wrapper);
        return result ? ResultVO.OK("标签删除成功") : ResultVO.ERROR("标签删除失败");
    }

    @ApiOperation(value = "根据用户ID查询所有标签", notes = "根据用户ID查询所有标签")
    @GetMapping("/searchAll/{uid}")
    public ResultVO<List<LabelDO>> searchAllLabel(@ApiParam("用户ID") @PathVariable("uid") String uid){
        QueryWrapper<LabelDO> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid);
        List<LabelDO> labelDOList = labelService.list(wrapper);
        return ResultVO.OK(labelDOList);
    }

    private QueryWrapper<LabelDO> buildLabelDOWrapper(@RequestParam("id") @ApiParam("标签ID") String id, @RequestParam("uid") @ApiParam("用户ID") String uid) {
        QueryWrapper<LabelDO> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("uid", uid);
        return wrapper;
    }
}
