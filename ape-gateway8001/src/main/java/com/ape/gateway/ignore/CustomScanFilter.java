package com.ape.gateway.ignore;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/28
 */
public class CustomScanFilter implements TypeFilter {

    /**
     * 网关使用了webflux，所以要排除基本包ape-common下使用的webmvc相关类的class类
     */
    public String[] className = {"com.ape.common.config.ignore"};

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        // 可以通过MetadataReader获得各种信息，然后根据自己的需求返回boolean，实例表示包名含有aaa路径的类名将满足筛选条件。
        for (String name : className) {
            return metadataReader.getClassMetadata().getClassName().contains(name);
        }
        return false;
    }
}
