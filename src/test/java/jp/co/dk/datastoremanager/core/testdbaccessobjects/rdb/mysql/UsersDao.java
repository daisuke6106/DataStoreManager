package jp.co.dk.datastoremanager.core.testdbaccessobjects.rdb.mysql;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;

/**
 * ユーザテーブルへのDAOインターフェース
 * 
 * @version 1.0
 * @author D.Kanno
 */
public interface UsersDao extends jp.co.dk.datastoremanager.core.testdbaccessobjects.UsersDao {
	
	public void createTable() throws DataStoreManagerException;
	
	public void dropTable() throws DataStoreManagerException;
	
}
