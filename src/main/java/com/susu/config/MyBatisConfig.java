package com.susu.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // 关键修复：手动创建 MyBatis 配置对象，开启驼峰映射
        Configuration configuration = new Configuration();
        configuration.setMapUnderscoreToCamelCase(true); // 显式开启下划线转驼峰
        sessionFactory.setConfiguration(configuration); // 将配置关联到 SqlSessionFactory

        // 配置 PageHelper 插件（保持不变）
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("supportMethodsArguments", "true");
        pageInterceptor.setProperties(properties);
        sessionFactory.setPlugins(pageInterceptor);

        // 配置 Mapper XML 路径（保持不变）
        sessionFactory.setMapperLocations(
            new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/*.xml")
        );

        return sessionFactory.getObject();
    }
}