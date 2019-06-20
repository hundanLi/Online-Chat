package club.lzlbog.chatting.config.db;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-17 10:21
 **/
@EnableTransactionManagement
@Configuration
public class DatasourceConfig {

    /** 数据源配置
     * @return druid数据库连接池
     */
    @Primary
    @Bean
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /** 注入事务管理器
     * @return 事务管理器
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
