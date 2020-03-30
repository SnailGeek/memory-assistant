package com.memory.assistant.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MybatisPlusGenerator {


    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {

        // 父包名
        String parent = "com.zimug.bootlaunch";

        String projectPath = System.getProperty("user.dir");

        // 生成文件的输出目录
        String outputDir = projectPath + "/src/main/java";


        //1. 全局配置
        GlobalConfig config = new GlobalConfig();
        {
            config
                    .setOutputDir(outputDir)          // 生成路径
                    .setFileOverride(true)          // 文件覆盖
                    .setActiveRecord(false)         // 是否支持AR模式
                    .setEnableCache(false)          // XML 二级缓存
                    .setBaseResultMap(true)         // XML ResultMap
                    .setBaseColumnList(true)        // XML columList
                    //.setServiceName("%sService")          // 设置生成的service接口的名字的首字母是否为I
                    .setOpen(false)  //生成后不打开文件夹
                    .setAuthor("wangf-q")
            ;
        }
        //2. 数据源配置
        DataSourceConfig dsConfig = new DataSourceConfig();
        {
            dsConfig
                    .setUrl("jdbc:mysql://localhost:3306/testdb2?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC")
                    .setUsername("root")
                    .setPassword("123qwe!@#")

                    .setDriverName("com.mysql.cj.jdbc.Driver")
                    .setDbType(DbType.MYSQL)  // 设置数据库类型
                    .setTypeConvert(new MySqlTypeConvert() {
                        @Override
                        public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
                            String t = fieldType.toLowerCase();
                            if (t.contains("bigint")) {
                                return DbColumnType.BIG_INTEGER;
                            }
                            System.out.println("转换类型：" + fieldType);
                            return super.processTypeConvert(config, fieldType);
                        }
                    });
        }

        //3. 包名策略配置
        PackageConfig pkConfig = new PackageConfig();
        {
            pkConfig
                    .setParent(parent)
                    .setMapper("dao")          // 这里是持久层包名，默认 mapper
                    .setEntity("domain")           // 这里是实体类包名，默认 entity
                    .setXml("dao")             // 这里是xml文件包名，默认mapper
                    .setService("service")        // 默认service
                    .setController("controller")  // 默认web
                    .setModuleName(scanner("模块名"))
            ;
        }

        //4. 策略配置
        StrategyConfig stConfig = new StrategyConfig();
        {
            stConfig
                    .setColumnNaming(NamingStrategy.underline_to_camel)
                    .setNaming(NamingStrategy.underline_to_camel)   // 数据库表映射到实体的命名策略
                    .setEntityBooleanColumnRemoveIsPrefix(true)
                    .setInclude(scanner("表名，多个英文逗号分隔").split(","))  // 生成的表
            ;
        }


        //5. 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        {
            // 如果模板引擎是 freemarker
//             String templatePath = "/templates/mapper.xml.ftl";
            // 如果模板引擎是 velocity
            String templatePath = "/templates/mapper.xml.vm";

            // 自定义输出配置
            List<FileOutConfig> focList = new ArrayList<>();
            // 自定义配置会被优先输出
            focList.add(new FileOutConfig(templatePath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                    return projectPath + "/src/main/resources/mapper/" + pkConfig.getModuleName()
                            + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                }
            });
            cfg.setFileOutConfigList(focList);
        }

        //6. 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        {
            // 配置自定义输出模板
            //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
            // templateConfig.setEntity("templates/entity2.java");
            // templateConfig.setService();
            // templateConfig.setController();

            templateConfig.setXml(null);
        }


        //7. 整合配置
        AutoGenerator ag = new AutoGenerator();
        {
            ag
                    .setGlobalConfig(config)
                    .setDataSource(dsConfig)
                    .setStrategy(stConfig)
                    .setPackageInfo(pkConfig)
                    .setCfg(cfg)
                    .setTemplate(templateConfig);
        }
        //8. 执行
        ag.execute();
    }

}