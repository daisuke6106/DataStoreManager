package jp.co.dk.datastoremanager.core.testdbaccessobjects.gdb;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.gdb.DataBaseNode;
import jp.co.dk.datastoremanager.core.gdb.DataConvertable;

/**
 * ユーザテーブルのレコードオブジェクト
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class UserNode implements DataConvertable {
	
	protected String stringData;
	
	protected int intData;
	
	protected boolean booleanData;
	
	public String getStringData() {
		return stringData;
	}

	public int getIntData() {
		return intData;
	}

	public boolean getBooleanData() {
		return booleanData;
	}
	
	@Override
	public DataConvertable convert(DataBaseNode dataBaseNode) throws DataStoreManagerException {
		UserNode usersNode = new UserNode();
		usersNode.stringData       = dataBaseNode.getString("STRING_DATA");
		usersNode.intData          = dataBaseNode.getInt("INT_DATA");
		usersNode.booleanData      = dataBaseNode.getBoolean("INT_DATA");
		return usersNode;
	}
}
