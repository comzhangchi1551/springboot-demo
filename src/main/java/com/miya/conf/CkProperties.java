package com.miya.conf;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author kangjian
 */
@Configuration
@MapperScan(basePackages = "com.miya.dao.ck", sqlSessionTemplateRef  = "ckSqlSessionTemplate")
public class CkProperties {

    @Bean(name = "ckDataSource")
    @ConfigurationProperties(prefix = "spring.click-house")
    public DataSource ckDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "ckSqlSessionFactory")
    public SqlSessionFactory ckSqlSessionFactory(@Qualifier("ckDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mappers/ck/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "ckTransactionManager")
    public DataSourceTransactionManager ckTransactionManager(@Qualifier("ckDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "ckSqlSessionTemplate")
    public SqlSessionTemplate ckSqlSessionTemplate(@Qualifier("ckSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
