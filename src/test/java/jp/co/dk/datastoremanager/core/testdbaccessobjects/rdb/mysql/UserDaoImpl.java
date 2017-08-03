package jp.co.dk.datastoremanager.core.testdbaccessobjects.rdb.mysql;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import jp.co.dk.datastoremanager.core.DataStore;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.rdb.Sql;
import jp.co.dk.datastoremanager.core.testdbaccessobjects.rdb.UsersRecord;

public class UserDaoImpl extends AbstractDataBaseAccessObject implements UsersDao{

	public UserDaoImpl(DataBaseAccessParameter dataBaseAccessParameter) throws DataStoreManagerException {
		super(dataBaseAccessParameter);
	}
	
	public UserDaoImpl(DataStore dataStore) throws DataStoreManagerException {
		super(dataStore);
	}
	
	@Override
	public void createTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("CREATE TABLE USERS ");
		sb.append('(');
		sb.append("STRING_DATA       VARCHAR(10) NOT NULL,");
		sb.append("INT_DATA          INT         NOT NULL,");
		sb.append("LONG_DATA         BIGINT(8)   NOT NULL,");
		sb.append("DATE_DATA         DATE        NOT NULL,");
		sb.append("TIMESTAMP_DATA    DATETIME, ");
		sb.append("BYTES_DATA        LONGBLOB, ");
		sb.append("OBJECT_DATA       LONGBLOB, ");
		sb.append("CONVERTBYTES_DATA LONGBLOB  ");
		sb.append(")");
		Sql sql = new Sql(sb.toString());
		this.createTable(sql);
	}
	
	@Override
	public void dropTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("DROP TABLE USERS ");
		Sql sql = new Sql(sb.toString());
		this.dropTable(sql);
	}

	@Override
	public UsersRecord select(String stringData) throws DataStoreManagerException {
		Sql sql = new Sql("SELECT * FROM USERS WHERE STRING_DATA = ?");
		sql.setParameter(stringData);
		return selectSingle(sql, new UsersRecord());
	}

	@Override
	public void insert(String stringData, int intData, long longData,	Date dateData, Timestamp timestampData, byte[] byteDate, Serializable objectData, Object convertByteData) throws DataStoreManagerException {
		Sql sql = new Sql("INSERT INTO USERS VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
		sql.setParameter(stringData);
		sql.setParameter(intData);
		sql.setParameter(longData);
		sql.setParameter(dateData);
		sql.setParameter(timestampData);
		sql.setParameter(byteDate);
		sql.setParameter(objectData);
		sql.setParameterConvertToBytes(convertByteData);
		insert(sql);
	}

	@Override
	public int update(String stringData, int intData) throws DataStoreManagerException {
		Sql sql = new Sql("UPDATE USERS SET INT_DATA = ? WHERE STRING_DATA = ?");
		sql.setParameter(stringData);
		sql.setParameter(intData);
		return update(sql);
	}

	@Override
	public int delete(String stringData) throws DataStoreManagerException {
		Sql sql = new Sql("DELETE FROM USERS WHERE STRING_DATA = ?");
		sql.setParameter(stringData);
		return delete(sql);
	}
	
	
}
