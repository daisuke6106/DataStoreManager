package jp.co.dk.datastoremanager.core.gdb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.gdb.Cypher;
import jp.co.dk.datastoremanager.core.gdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.gdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.core.testdbaccessobjects.DataStoreDaoConstantsTest;
import jp.co.dk.datastoremanager.core.testdbaccessobjects.UsersDao;
import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;
import static org.junit.Assert.*;

public class DataBaseDataStoreTest extends DataStoreManagerTestFoundation{
	
	@Test
	public void constractor() {
		// ==============================正常系==============================
		// アクセス可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成ができること
		try {
			DataBaseDataStore dataBaseDataStore = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			assertNotNull(dataBaseDataStore.dataBaseAccessParameter);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// アクセス不可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成ができること
		try {
			DataBaseDataStore dataBaseDataStore = new DataBaseDataStore(super.getAccessFaileDataBaseAccessParameterGDB());
			assertNotNull(dataBaseDataStore.dataBaseAccessParameter);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void startTrunsaction() {
		// ==============================正常系==============================
		// アクセス可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成でき、トランザクションが開始できること
		try {
			DataBaseDataStore dataBaseDataStore = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			dataBaseDataStore.startTransaction();
			assertNotNull(dataBaseDataStore.transaction);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// ==============================異常系==============================
		// アクセス不可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成ができ、トランザクション開始時に例外が発生すること
		try {
			DataBaseDataStore dataBaseDataStore = new DataBaseDataStore(super.getAccessFaileDataBaseAccessParameterGDB());
			dataBaseDataStore.startTransaction();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_CREATE_CONNECTION);
		}
	}
	
	@Test
	public void getDataAccessObject() {
		// ==============================正常系==============================
		// アクセス可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成ができること
		// トランザクション開始し、データアクセスオブジェクトを取得した場合、正常に取得できること。
		try {
			DataBaseDataStore dataBaseDataStore = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			dataBaseDataStore.startTransaction();
			UsersDao dao = (UsersDao)dataBaseDataStore.getDataAccessObject(DataStoreDaoConstantsTest.USERS);
			assertTrue(dao instanceof UsersDao);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// ==============================異常系==============================
		// アクセス可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成ができること
		// トランザクション開始せずに、データアクセスオブジェクトを取得した場合、例外が発生されること
		try {
			DataBaseDataStore dataBaseDataStore = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			dataBaseDataStore.getDataAccessObject(DataStoreDaoConstantsTest.USERS);
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), TRANSACTION_IS_NOT_START);
		}
	}
	
	@Test
	public void finishTrunsaction() {
		// ==============================正常系==============================
		// アクセス可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成でき、トランザクションが開始されている場合、
		// trueが返却されること。トランザクション終了後、トランザクション状態はfalseを返却されること。
		try {
			DataBaseDataStore dataBaseDataStore = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			dataBaseDataStore.startTransaction();
			assertTrue(dataBaseDataStore.isTransaction());
			dataBaseDataStore.finishTransaction();
			assertFalse(dataBaseDataStore.isTransaction());
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// ==============================異常系==============================
		// アクセス可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成でき、トランザクションが開始前に
		// トランザクション終了が実行された場合、例外が送出されること。
		try {
			DataBaseDataStore dataBaseDataStore = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			dataBaseDataStore.finishTransaction();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), TRANSACTION_IS_NOT_START);
		}
	}
	
	@Test
	public void isTransaction(){
		// ==============================正常系==============================
		// アクセス可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成でき、トランザクションが開始されている場合、
		// trueが返却されること
		try {
			DataBaseDataStore dataBaseDataStore = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			dataBaseDataStore.startTransaction();
			assertTrue(dataBaseDataStore.isTransaction());
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// アクセス可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成でき、トランザクションが開始されていない場合、
		// falseが返却されること
		try {
			DataBaseDataStore dataBaseDataStore = new DataBaseDataStore(super.getAccessFaileDataBaseAccessParameterGDB());
			assertFalse(dataBaseDataStore.isTransaction());
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void test_executeSqls() throws DataStoreManagerException, ParseException {
		// ==============================正常系==============================
		DataBaseDataStore target01 = new DataBaseDataStore(this.getAccessableDataBaseAccessParameterGDB());
		target01.startTransaction();
		
		// ＝＝＝＝＝＝＝＝レコードを追加＝＝＝＝＝＝＝＝
		try {
			target01.execute(new Cypher("CREATE(USER{name:{1},age:{2}})"));
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
	}
	
	@Test
	public void test_equals() {
		try {
			// トランザクション開始前は同一のパラメータで有るため、hashcode、qualsは一致すること
			DataBaseDataStore target_01_01 = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			DataBaseDataStore target_01_02 = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			DataBaseDataStore target_01_03 = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			List<Object> faileList_01 = new ArrayList<Object>();
			DataBaseDataStore fail_01_01 = new DataBaseDataStore(super.getAccessFaileDataBaseAccessParameterGDB());
			faileList_01.add(fail_01_01);
			testEquals(target_01_01, target_01_02, target_01_03, faileList_01);
			
			DataBaseDataStore target_02_01 = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			DataBaseDataStore target_02_02 = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			target_02_02.startTransaction();
			assertFalse(target_02_01.equals(target_02_02));
			
			DataBaseDataStore target_03_01 = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			target_03_01.startTransaction();
			DataBaseDataStore target_03_02 = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			assertFalse(target_03_01.equals(target_03_02));
			
			DataBaseDataStore target_04_01 = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			target_04_01.startTransaction();
			DataBaseDataStore target_04_02 = new DataBaseDataStore(super.getAccessableDataBaseAccessParameterGDB());
			target_04_02.startTransaction();
			assertTrue(target_04_01.equals(target_04_01));
			assertFalse(target_04_01.equals(target_04_02));
			
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void test_tostring() {
		try {
			DataBaseAccessParameter param = super.getAccessableDataBaseAccessParameterGDB();
			DataBaseDataStore target = new DataBaseDataStore(param);
			assertEquals(target.toString(), "CONNECTION_HASH=[Transaction has not been started]," + param.toString());
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
}
