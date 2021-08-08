package com.ape.listener;

import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/8/4
 */
@Component
@CanalTable(value = "article")
public class ArticleListener implements EntryHandler {
    @Override
    public void insert(Object o) {

    }

    @Override
    public void update(Object before, Object after) {

    }

    @Override
    public void delete(Object o) {

    }
}
