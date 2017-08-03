package jp.co.dk.datastoremanager.core.rdb;

import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.core.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.core.DataStore;
import jp.co.dk.datastoremanager.core.DataStoreKind;
import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.rdb.DataBaseDataStore;

import org.junit.Test;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;
import static org.junit.Assert.*;

public class DataBaseAccessParameterTest  extends DataStoreManagerTestFoundation{
	
	@Test
	public void constractor() {
		// ==============================正常系==============================
		try {
			DataBaseAccessParameter target = new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "255.255.255.255", "test_user", "123456");
			assertEquals(target.getDataStoreKind(), DataStoreKind.ORACLE);
			assertEquals(target.getDriver(), DataBaseDriverConstants.ORACLE);
			assertEquals(target.getUrl(), "255.255.255.255");
			assertEquals(target.getUser(), "test_user");
			assertEquals(target.getPassword(), "123456");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// ==============================異常系==============================
		// データストア種別にnullが指定された場合、例外が送出されること。
		try {
			new DataBaseAccessParameter(null, DataBaseDriverConstants.ORACLE, "255.255.255.255", "test_user", "123456");
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DATA_STORE_KIND_IS_NOT_SET);
		}
		
		// ドライバーにnullが指定された場合、例外が送出されること。
		try {
			new DataBaseAccessParameter(DataStoreKind.ORACLE, null, "255.255.255.255", "test_user", "123456");
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DRIVER_IS_NOT_SET);
		}
		
		// URLにnullが指定された場合、例外が送出されること。
		try {
			new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, null, "test_user", "123456");
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), URL_IS_NOT_SET);
		}
		
		// URLに空文字が指定された場合、例外が送出されること。
		try {
			new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "", "test_user", "123456");
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), URL_IS_NOT_SET);
		}
		
		// ユーザにnullが指定された場合、例外が送出されること。
		try {
			new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "255.255.255.255", null, "123456");
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), USER_IS_NOT_SET);
		}
		
		// ユーザに空文字が指定された場合、例外が送出されること。
		try {
			new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "255.255.255.255", "", "123456");
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), USER_IS_NOT_SET);
		}
		
		// パスワードにnullが指定された場合、例外が送出されること。
		try {
			new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "255.255.255.255", "test_user", null);
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), PASSWORD_IS_NOT_SET);
		}
		
		// パスワードに空文字が指定された場合、例外が送出されること。
		try {
			new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "255.255.255.255", "test_user", "");
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), PASSWORD_IS_NOT_SET);
		}
	}
	
	@Test
	public void createDataStore() {
		// ==============================正常系==============================
		try {
			DataBaseAccessParameter target = new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "255.255.255.255", "test_user", "123456");
			assertEquals(target.getDataStoreKind(), DataStoreKind.ORACLE);
			assertEquals(target.getDriver(), DataBaseDriverConstants.ORACLE);
			assertEquals(target.getUrl(), "255.255.255.255");
			assertEquals(target.getUser(), "test_user");
			assertEquals(target.getPassword(), "123456");
			
			DataStore dataStore = target.createDataStore();
			assertTrue(dataStore instanceof DataBaseDataStore);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void test_equals() {
		try {
			DataBaseAccessParameter target_01 = new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "255.255.255.255", "test_user", "123456");
			DataBaseAccessParameter target_02 = new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "255.255.255.255", "test_user", "123456");
			DataBaseAccessParameter target_03 = new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "255.255.255.255", "test_user", "123456");
			
			List<Object> diffList = new ArrayList<Object>();
			diffList.add(new DataBaseAccessParameter(DataStoreKind.MYSQL, DataBaseDriverConstants.ORACLE, "255.255.255.255", "test_user", "123456"));
			diffList.add(new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.MYSQL, "255.255.255.255", "test_user", "123456"));
			diffList.add(new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "255.255.255.254", "test_user", "123456"));
			diffList.add(new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "255.255.255.255", "_test_user", "123456"));
			diffList.add(new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "255.255.255.255", "test_user", "_123456"));
			
			testEquals(target_01, target_02, target_03, diffList);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void test_tostring() {
		try {
			DataBaseAccessParameter target_01 = new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "jdbc:oracle:thin:@255.255.255.255:test_db", "test_user", "123456");
			assertEquals(target_01.toString(), "DATASTOREKIND=[ORACLE],DRIVER=[oracle.jdbc.driver.OracleDriver],URL=[jdbc:oracle:thin:@255.255.255.255:test_db],USER=[test_user],PASSWORD=[123456]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
}
