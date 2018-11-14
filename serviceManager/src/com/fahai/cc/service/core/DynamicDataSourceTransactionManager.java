package com.fahai.cc.service.core;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * @ClassName: DynamicDataSourceTransactionManager
 * @Description: TODO
 * @author: Aaron.ye
 * @date: 2017年8月1日 下午3:27:33
 */
public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 只读事务到读库，读写事务到写库
     * @param transaction
     * @param definition
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {

        //设置数据源
        boolean readOnly = definition.isReadOnly();
        if(readOnly) {
        	DataSourceTypeManager.putDataSource(DynamicDataSourceGlobal.READ);
        } else {
        	DataSourceTypeManager.putDataSource(DynamicDataSourceGlobal.WRITE);
        }
        super.doBegin(transaction, definition);
    }

    /**
     * 清理本地线程的数据源
     * @param transaction
     */
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        DataSourceTypeManager.clearDataSource();
    }
}
