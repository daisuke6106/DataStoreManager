package jp.co.dk.datastoremanager.core.rdb;

import java.sql.PreparedStatement;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;

interface SqlParameter {
	
	public void set(int index, PreparedStatement statement) throws DataStoreManagerException;
	
}
