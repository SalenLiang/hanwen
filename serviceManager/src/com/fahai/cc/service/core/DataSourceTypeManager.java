package com.fahai.cc.service.core;

/**
 * @ClassName: DataSourceTypeManager
 * @Description: TODO
 * @author: Aaron.ye
 * @date: 2017年8月1日 下午1:27:33
 */
public class DataSourceTypeManager {
	
	private static final ThreadLocal<DynamicDataSourceGlobal> dynamicDataSource = new ThreadLocal<DynamicDataSourceGlobal>();
     
		private DataSourceTypeManager() {
	        //
	    }

	    public static void putDataSource(DynamicDataSourceGlobal dataSource){
	    	dynamicDataSource.set(dataSource);
	    }

	    public static DynamicDataSourceGlobal getDataSource(){
	        return dynamicDataSource.get();
	    }

	    public static void clearDataSource() {
	    	dynamicDataSource.remove();
	    }	


}
