package jp.co.dk.datastoremanager.core.testdbaccessobjects.rdb;

import java.io.Serializable;
import java.util.Date;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.DataBaseRecord;
import jp.co.dk.datastoremanager.core.rdb.DataConvertable;
import jp.co.dk.datastoremanager.core.rdb.Record;

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
		usersRecord.stringData       = dataBaseRecord.getString("STRING_DATA");
		usersRecord.intData          = dataBaseRecord.getInt("INT_DATA");
		usersRecord.longData         = dataBaseRecord.getLong("LONG_DATA");
		usersRecord.dateData         = dataBaseRecord.getDate("DATE_DATA");
		usersRecord.timestampData    = dataBaseRecord.getTimestamp("TIMESTAMP_DATA");
		usersRecord.bytesData        = dataBaseRecord.getBytes("BYTES_DATA");
		usersRecord.serializableData = dataBaseRecord.getObject("OBJECT_DATA");
		usersRecord.objectData       = dataBaseRecord.getObject("CONVERTBYTES_DATA");
		return usersRecord;
	}
}
