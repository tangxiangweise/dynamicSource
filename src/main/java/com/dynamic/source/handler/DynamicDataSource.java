package com.dynamic.source.handler;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;


public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 写数据源
     */
    private Object writeDataSource; //写数据源
    /**
     * 读数据源
     */
    private Object readDataSource;

    /**
     * 在初始化之前被调用，设置默认数据源
     */
    @Override
    public void afterPropertiesSet() {
        // 写数据源不存在抛异常
        if (this.writeDataSource == null) {
            throw new IllegalArgumentException("Property 'writeDataSource' is required");
        }
        // 设置默认目标数据源为主库
        setDefaultTargetDataSource(writeDataSource);
        // 设置所有数据源资源
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DynamicDataSourceGlobal.WRITE.name(), writeDataSource);
        if (readDataSource != null) {
            targetDataSources.put(DynamicDataSourceGlobal.READ.name(), readDataSource);
        }
        setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        // 根据当前线程所使用的数据源进行切换
        DynamicDataSourceGlobal dynamicDataSourceGlobal = DynamicDataSourceHolder.getDataSource();
        // 如果没有被赋值，默认使用主库
        if (dynamicDataSourceGlobal == null) {
            return DynamicDataSourceGlobal.WRITE.name();
        }
        return dynamicDataSourceGlobal.name();
    }

    public void setWriteDataSource(Object writeDataSource) {
        this.writeDataSource = writeDataSource;
    }

    public void setReadDataSource(Object readDataSource) {
        this.readDataSource = readDataSource;
    }
}