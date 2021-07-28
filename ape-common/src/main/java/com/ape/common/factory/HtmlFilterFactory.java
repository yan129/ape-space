package com.ape.common.factory;

import com.ape.common.model.BaseEntity;
import com.ape.common.utils.StringUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/28
 */
@SuppressWarnings(value = "all")
@Component
public class HtmlFilterFactory implements HtmlFilterSuperFactory {

    @SneakyThrows
    @Override
    public <T extends BaseEntity> T buildModel(Class<?> clazz, String json) throws ClassNotFoundException, NoSuchMethodException {
        String clazzName = null;
        StringBuilder sb = new StringBuilder();
        String name = clazz.getName();
        if (name.contains(".")){
            String[] nameSplit = StringUtils.split(name, ".");
            for (int i = 0; i < nameSplit.length - 2; i++) {
                sb.append(nameSplit[i]).append(".");
            }
            sb.append("filter.html.");
            // 调用过滤方法的路径为过滤实体的上级目录的filter.html包下的XXHtmlFilter类 -- com.ape.article.filter.html.ArticleDOHtmlFilter
            clazzName = sb.toString() + name.substring(name.lastIndexOf(".") + 1) + "HtmlFilter";
            Method method = Class.forName(clazzName).getMethod("filter", String.class);
           return ((T) method.invoke(Class.forName(clazzName).newInstance(), json));
        }

        return null;
    }

}
