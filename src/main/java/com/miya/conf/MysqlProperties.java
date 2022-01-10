package com.miya.conf;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author 张
 */
@Configuration
@MapperScan(basePackages = "com.miya.dao.mysql", sqlSessionTemplateRef  = "mysqlSqlSessionTemplate")
public class MysqlProperties {

    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.mysql")
    @Primary
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mysqlSqlSessionFactory")
    @Primary
    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource,
                                                    PaginationInterceptor paginationInterceptor) throws Exception {
        // 如果这里要使用mybatis-plus的话，一定要使用MybatisSqlSessionFactoryBean，而不是SqlSessionFactory；
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 设置mybatisPlus插件；否则分页不生效；
        Interceptor[] plugins = {paginationInterceptor};
        bean.setPlugins(plugins);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mappers/mysql/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "mysqlTransactionManager")
    @Primary
    public DataSourceTransactionManager mysqlTransactionManager(@Qualifier("mysqlDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "mysqlSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }



}
