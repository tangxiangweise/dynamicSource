package com.dynamic.source.handler;

/**
 * 动态数据源线程持有者
 */
public class DynamicDataSourceHolder {

    private static final ThreadLocal<DynamicDataSourceGlobal> holder = new ThreadLocal<DynamicDataSourceGlobal>();

    /**
     * 设置当前线程使用的数据源
     * @param dataSource
     */
    public static void putDataSource(DynamicDataSourceGlobal dataSource) {
        holder.set(dataSource);
    }

    /**
     * 获取当前线程需要使用的数据源
     * @return
     */
    public static DynamicDataSourceGlobal getDataSource() {
        return holder.get();
    }

    /**
     * 清空使用的数据源
     */
    public static void clearDataSource() {
        holder.remove();
    }

}
