package com.ape.mp;

import com.ape.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/15
 * mybatis-plus代码生成器
 */
public class MPGenerator {

    /**
     * 总的文件生成路径：projectPath "/" + modelName + "/" + parent + "/" + businessPackage
     * @param modelName 模块名，maven构建项目的模块名（ape-article）
     * @param businessPackage 启动类的所在包的包名，（article）
     * @param table 数据库表名，输入多个表用,分割
     * @param tablePrefix 生成实体需要去掉的表前缀
     */
    public static void generator(String modelName, String businessPackage, String table, String tablePrefix) {
        //用来获取Mybatis-Plus.properties文件的配置信息
        final ResourceBundle rb = ResourceBundle.getBundle("mybatis-plus");

        //代码生成器
        AutoGenerator mpg = new AutoGenerator();

        //全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        // class文件输出路径
        gc.setOutputDir(projectPath + "/" + modelName + "/src/main/java");
        //主键策略
        gc.setIdType(IdType.ASSIGN_UUID);
        gc.setAuthor(rb.getString("author"));
        //自定义Service接口生成的文件名
        gc.setEntityName("%sDO");
        gc.setServiceName("%sService");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceImplName("%sServiceImpl");
        //生成后是否打开资源管理器
        gc.setOpen(false);
        //重新生成时文件是否覆盖
        gc.setFileOverride(false);
        //设置生成的xml文件封装好baseMapper
        gc.setBaseResultMap(true);
        //定义生成的实体类中日期类型
        gc.setDateType(DateType.ONLY_DATE);
        //开启Swagger2模式
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        //数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(rb.getString("url"));
        dsc.setDriverName(rb.getString("driver"));
        dsc.setUsername(rb.getString("username"));
        dsc.setPassword(rb.getString("password"));
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        //包配置
        PackageConfig pc = new PackageConfig();
        //模块名，即com.ape
        pc.setParent(rb.getString("parent"));
        pc.setModuleName(businessPackage);
        pc.setController("controller");
        pc.setEntity("model");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        //自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                // 解决子类会生成父类属性的问题，在模版里会用到该配置
                map.put("superColumns", this.getConfig().getStrategyConfig().getSuperEntityColumns());
                this.setMap(map);
            }
        };

        //自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        //自定义配置会优先输出
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/" + modelName + "/src/main/resources/mapper/"
                        + tableInfo.getEntityName().substring(0, tableInfo.getEntityName().length() - 2) + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);


        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);


        //配置策略
        StrategyConfig strategy = new StrategyConfig();
        //数据库表映射到实体的命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //生成实体时去掉表前缀
        //strategy.setTablePrefix(pc.getModuleName() + "_")
        //数据库表字段映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 生成实体继承基类
        strategy.setSuperEntityClass(BaseEntity.class);
        // 去掉继承父类的字段属性，需要在 InjectionConfig 中配置
        strategy.setSuperEntityColumns("id", "is_deleted", "gmt_created", "gmt_modified");
        // lombok 模型
        strategy.setEntityLombokModel(true);
        //是否启用 builder 模式，@Accessors(chain = true) setter链式操作，默认false
        strategy.setEntityBuilderModel(true);
        //restful api风格控制器
        strategy.setRestControllerStyle(true);
        //url中驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        //strategy.setInclude(scanner("表名，多个英文逗号分割").split(","))
        strategy.setInclude(table.split(","));
        strategy.setControllerMappingHyphenStyle(true);
        //生成实体时去掉表前缀
        strategy.setTablePrefix(tablePrefix);
        mpg.setStrategy(strategy);
        // 添加模板引擎
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        //执行
        mpg.execute();
    }

    public static void main(String[] args) {
        MPGenerator.generator("ape-article", "article", "follow", "");
    }
}
