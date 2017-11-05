package jp.co.dk.datastoremanager.core.testdbaccessobjects.rdb;

import java.util.Date;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.DataBaseRecord;
import jp.co.dk.datastoremanager.core.rdb.DataConvertable;

/**
 * ユーザテーブルのレコードオブジェクト
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class UsersRecord implements DataConvertable {
	
	protected String stringData;
	
	protected int intData;
	
	protected long longData;
	
	protected Date dateData;
	
	protected Date timestampData;
	
	protected byte[] bytesData;
	
	protected Object serializableData;
	
	protected Object objectData;
	

	public String getStringData() {
		return stringData;
	}

	public int getIntData() {
		return intData;
	}

	public long getLongData() {
		return longData;
	}

	public Date getDateData() {
		return dateData;
	}

	public Date getTimestampData() {
		return timestampData;
	}

	public byte[] getBytesData() {
		return bytesData;
	}

	public Object getSerializableData() {
		return serializableData;
	}

	public Object getObjectData() {
		return objectData;
	}
	
	@Override
	public DataConvertable convert(DataBaseRecord dataBaseRecord) throws DataStoreManagerException {
		UsersRecord usersRecord = new UsersRecord();
		usersRecord.stringData       = dataBaseRecord.getString("STRING_DATA").get();
		usersRecord.intData          = dataBaseRecord.getInt("INT_DATA").get();
		usersRecord.longData         = dataBaseRecord.getLong("LONG_DATA").get();
		usersRecord.dateData         = dataBaseRecord.getDate("DATE_DATA").get();
		usersRecord.timestampData    = dataBaseRecord.getTimestamp("TIMESTAMP_DATA").get();
		usersRecord.bytesData        = dataBaseRecord.getBytes("BYTES_DATA").get();
		usersRecord.serializableData = dataBaseRecord.getObject("OBJECT_DATA").get();
		usersRecord.objectData       = dataBaseRecord.getObject("CONVERTBYTES_DATA").get();
		return usersRecord;
	}
}
