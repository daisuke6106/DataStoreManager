package jp.co.dk.datastoremanager.core.gdb;

import java.sql.PreparedStatement;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;

abstract class CypherParameter {
	
	abstract void set(int index, PreparedStatement statement) throws DataStoreManagerException;
	
}
