package jp.co.dk.datastoremanager.core.rdb;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;

import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.ColumnMetaData;
import jp.co.dk.datastoremanager.core.rdb.DataBaseRecord;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

public class DataBaseRecordTest extends DataStoreManagerTestFoundation{
	
	@Mocked
	private ResultSet resultset;
	
	@Test
	public void getString() throws SQLException {
		// ==============================正常系==============================
        try {
        	new Expectations() {{
    			resultset.getString("USERID"); result = "1234567890";
            }};
            DataBaseRecord target = new DummyDataBaseRecord(resultset);
			assertEquals(target.getString("USERID"), "1234567890");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
        
        try {
        	new Expectations() {{
    			resultset.getString(0); result = "1234567890";
            }};
            DataBaseRecord target = new DummyDataBaseRecord(resultset);
			assertEquals(target.getString(0), "1234567890");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
        
        // ==============================異常系==============================
        try {
        	new Expectations() {{
    			resultset.getString("USERID"); result = new SQLException("SQLExceptionのモックアップ例外クラス");
            }};
            DataBaseRecord target = new DummyDataBaseRecord(resultset);
			target.getString("USERID");
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), GET_COLUMN_IS_FAILE_BY_NAME);
		}
        
        try {
        	new Expectations() {{
    			resultset.getString(0); result = new SQLException("SQLExceptionのモックアップ例外クラス");
            }};
            DataBaseRecord target = new DummyDataBaseRecord(resultset);
			target.getString(0);
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), GET_COLUMN_IS_FAILE_BY_INDEX
					);
		}
	}
	
	@Test
	public void getInt() throws SQLException {
		// ==============================正常系==============================
        try {
        	new Expectations() {{
    			resultset.getInt("AGE"); result = new Integer(20);
            }};
            DataBaseRecord target = new DummyDataBaseRecord(resultset);
			assertEquals(target.getInt("AGE").get(), 20);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
        
        try {
        	new Expectations() {{
    			resultset.getInt(1); result = new Integer(20);
            }};
            DataBaseRecord target = new DummyDataBaseRecord(resultset);
			assertEquals(target.getInt(1), 20);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
        
        // ==============================異常系==============================
        try {
        	new Expectations() {{
    			resultset.getInt("AGE"); result = new SQLException("SQLExceptionのモックアップ例外クラス");
            }};
            DataBaseRecord target = new DummyDataBaseRecord(resultset);
			target.getInt("AGE");
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), GET_COLUMN_IS_FAILE_BY_NAME);
		}
        
        try {
        	new Expectations() {{
    			resultset.getInt(1); result = new SQLException("SQLExceptionのモックアップ例外クラス");
            }};
            DataBaseRecord target = new DummyDataBaseRecord(resultset);
			target.getInt(1);
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), GET_COLUMN_IS_FAILE_BY_INDEX
					);
		}
	}
	
	@Test
	public void getDate() throws SQLException, ParseException {
		// ==============================正常系==============================
        try {
        	new Expectations() {{
    			resultset.getDate("BIRTHDAY"); result = new java.sql.Date(createDateByString("20130101000000").getTime());
            }};
            DataBaseRecord target = new DummyDataBaseRecord(resultset);
			assertEquals(target.getDate("BIRTHDAY"), createDateByString("20130101000000"));
		} catch (DataStoreManagerException e) {
			fail(e);
		}
        
        try {
        	new Expectations() {{
    			resultset.getDate(2); result = new java.sql.Date(createDateByString("20130101000000").getTime());
            }};
            DataBaseRecord target = new DummyDataBaseRecord(resultset);
			assertEquals(target.getDate(2), createDateByString("20130101000000"));
		} catch (DataStoreManagerException e) {
			fail(e);
		}
        
        // ==============================異常系==============================
        try {
        	new Expectations() {{
    			resultset.getDate("BIRTHDAY"); result = new SQLException("SQLExceptionのモックアップ例外クラス");
            }};
            DataBaseRecord target = new DummyDataBaseRecord(resultset);
			target.getDate("BIRTHDAY");
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), GET_COLUMN_IS_FAILE_BY_NAME);
		}
        
        try {
        	new Expectations() {{
    			resultset.getDate(2); result = new SQLException("SQLExceptionのモックアップ例外クラス");
            }};
            DataBaseRecord target = new DummyDataBaseRecord(resultset);
			target.getDate(2);
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), GET_COLUMN_IS_FAILE_BY_INDEX
					);
		}
	}
}

class DummyDataBaseRecord extends DataBaseRecord {

	protected DummyDataBaseRecord(ResultSet resultSet) {
		super(resultSet);
	}

	@Override
	protected ColumnMetaData createColumnMetaData(ResultSetMetaData rs, int i) throws SQLException {
		return null;
	}
	
}