package com.sleephelper.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * MyBatis 配置类
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Configuration
@MapperScan("com.sleephelper.mapper")
public class MybatisConfig {

    @Autowired
    private DataSource dataSource;

    /**
     * 配置 SqlSessionFactory
     * 
     * @return SqlSessionFactory
     * @throws Exception 配置异常
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        
        // 设置 mapper xml 文件路径
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:mapper/**/*.xml"));
        
        // 设置实体类别名包
        sessionFactory.setTypeAliasesPackage("com.sleephelper.entity");
        
        return sessionFactory.getObject();
    }
}
