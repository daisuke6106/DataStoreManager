package jp.co.dk.datastoremanager.core.testdbaccessobjects;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;

/**
 * ユーザテーブルへのDAOインターフェース
 * 
 * @version 1.0
 * @author D.Kanno
 */
public interface UsersDao {
	
	public jp.co.dk.datastoremanager.core.DataConvertable select(String stringData)throws DataStoreManagerException;
	
	public void insert(String stringData, int intData, long longData, Date dateData, Timestamp timestampData, byte[] byteDate, Serializable objectData, Object convertByteData)throws DataStoreManagerException;
	
	public int update(String stringData, int intData)throws DataStoreManagerException;
	
	public int delete(String stringData)throws DataStoreManagerException;
	
}
