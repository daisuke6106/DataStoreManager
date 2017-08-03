package jp.co.dk.datastoremanager.core.rdb;

import java.sql.PreparedStatement;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;

abstract class SqlParameter {
	
	abstract void set(int index, PreparedStatement statement) throws DataStoreManagerException;
	
}
