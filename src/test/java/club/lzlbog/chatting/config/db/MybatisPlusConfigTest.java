package club.lzlbog.chatting.config.db;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class MybatisPlusConfigTest {

    /**
     * 代码生成器示例
     */

    @Test
    public void generator() {
        //1.全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setActiveRecord(true) //是否支持AR模式
                .setAuthor("li")    //作者
                .setOutputDir("/home/li/java/IdeaProjects/springboot/chatting/src/main/java")   //生成路径
                .setFileOverride(true)  //文件覆盖
                .setIdType(IdType.AUTO) //主键策略=自增
                .setServiceName("%sService") //设置生成的Service接口的名字首字母是否为I,如IUserService
                .setBaseResultMap(true)
                .setBaseColumnList(true)
        ;
        //2.数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)    //设置数据库类型
                .setDriverName("com.mysql.jdbc.Driver") //设置数据库驱动
                .setUrl("jdbc:mysql://localhost:3306/chatting") //设置数据库地址
                .setUsername("chatting")    //数据库用户
                .setPassword("Chatting@123");   //用户密码

        //3.策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true)    //全局大写命名
                .setDbColumnUnderline(true) //驼峰命名规则
                .setNaming(NamingStrategy.underline_to_camel)   //数据库表映射到实体的命名策略
                .setTablePrefix("tbl_")   //表明前缀
                .setInclude("tbl_last_query")   //生成的表
        ;

        //4.包名策略配置
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent("club.lzlbog.chatting")    //公共父包名
                .setMapper("mapper")    //mapper接口包名
                .setService("service")  //service层包名
                .setEntity("entity")     //实体类包名
                .setController("controller")    //控制层包名
                .setXml("mapper")   //mapper映射文件包名
        ;

        //5.整合配置
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(pkConfig);

        //执行
//        autoGenerator.execute();
    }

}