package com.dynamic.source.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.dynamic.source.handler.DynamicDataSource;
import com.dynamic.source.handler.DynamicDataSourceTransactionManager;
import com.dynamic.source.handler.DynamicPlugin;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DataSourceConfig {


    @Bean("readDataSource")
    @ConfigurationProperties(prefix = "dynamic-datasource.read")
    public DataSource readDataSource(StandardEnvironment env) {
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
        return common(env, druidDataSource);
    }

    @Bean("writeDataSource")
    @ConfigurationProperties(prefix = "dynamic-datasource.write")
    public DataSource writeDataSource(StandardEnvironment env) {
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
        return common(env, druidDataSource);
    }

    @Bean("dataSource")
    public DataSource dynamicDataSource(StandardEnvironment env) {
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setReadDataSource(readDataSource(env));
        dataSource.setWriteDataSource(writeDataSource(env));
        return dataSource;
    }

    @Bean("dynamicPlugin")
    public DynamicPlugin dynamicPlugin() {
        DynamicPlugin plugin = new DynamicPlugin();
        return plugin;
    }

    @Bean("transactionManager")
    public DataSourceTransactionManager dynamicTransactionManager(StandardEnvironment env) {
        DynamicDataSourceTransactionManager transactionManager = new DynamicDataSourceTransactionManager();
        transactionManager.setDataSource(dynamicDataSource(env));
        return transactionManager;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory SqlSessionFactoryBean(StandardEnvironment env) {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dynamicDataSource(env));
        Resource[] resources = null;
        try {
            resources = new PathMatchingResourcePatternResolver().getResources("classpath:mapping/*.xml");
        } catch (Exception e) {

        }
        sessionFactoryBean.setMapperLocations(resources);
        Interceptor[] plugins = {dynamicPlugin()};
        sessionFactoryBean.setPlugins(plugins);
        SqlSessionFactory sessionFactory = null;
        try {
            sessionFactory = sessionFactoryBean.getObject();
        } catch (Exception e) {

        }
        return sessionFactory;
    }

    @Bean("sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(StandardEnvironment env) {
        SqlSessionTemplate sessionTemplate = new SqlSessionTemplate(SqlSessionFactoryBean(env));
        return sessionTemplate;
    }


    public DataSource common(StandardEnvironment env, DruidDataSource druidDataSource) {
        Properties properties = new Properties();
        PropertySource<?> appProperties = env.getPropertySources().get("applicationConfig: [classpath:/application.yml]");
        Map<String, Object> source = (Map<String, Object>) appProperties.getSource();
        properties.putAll(source);
        druidDataSource.configFromPropety(properties);
        return druidDataSource;
    }

}
