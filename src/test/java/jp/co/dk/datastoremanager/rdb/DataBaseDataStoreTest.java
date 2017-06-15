package jp.co.dk.datastoremanager.rdb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jp.co.dk.datastoremanager.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.rdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.rdb.mysql.MySqlDataBaseDataStore;
import jp.co.dk.datastoremanager.testdbaccessobjects.DataStoreDaoConstantsTest;
import jp.co.dk.datastoremanager.testdbaccessobjects.UsersDao;
import static jp.co.dk.datastoremanager.message.DataStoreManagerMessage.*;
import static org.junit.Assert.*;

public class DataBaseDataStoreTest extends DataStoreManagerTestFoundation{
	
	@Test
	public void constractor() {
		// ==============================正常系==============================
		// アクセス可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成ができること
		try {
			DataBaseDataStore dataBaseDataStore = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
			assertNotNull(dataBaseDataStore.dataBaseAccessParameter);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// アクセス不可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成ができること
		try {
			DataBaseDataStore dataBaseDataStore = new MySqlDataBaseDataStore(super.getAccessFaileDataBaseAccessParameterMYSQL());
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
			DataBaseDataStore dataBaseDataStore = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
			dataBaseDataStore.startTransaction();
			assertNotNull(dataBaseDataStore.transaction);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// ==============================異常系==============================
		// アクセス不可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成ができ、トランザクション開始時に例外が発生すること
		try {
			DataBaseDataStore dataBaseDataStore = new MySqlDataBaseDataStore(super.getAccessFaileDataBaseAccessParameterMYSQL());
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
			DataBaseDataStore dataBaseDataStore = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
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
			DataBaseDataStore dataBaseDataStore = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
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
			DataBaseDataStore dataBaseDataStore = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
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
			DataBaseDataStore dataBaseDataStore = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
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
			DataBaseDataStore dataBaseDataStore = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
			dataBaseDataStore.startTransaction();
			assertTrue(dataBaseDataStore.isTransaction());
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// アクセス可能なデータアクセスパラメータを設定した場合、正常にインスタンス生成でき、トランザクションが開始されていない場合、
		// falseが返却されること
		try {
			DataBaseDataStore dataBaseDataStore = new MySqlDataBaseDataStore(super.getAccessFaileDataBaseAccessParameterMYSQL());
			assertFalse(dataBaseDataStore.isTransaction());
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void test_executeSqls() throws DataStoreManagerException, ParseException {
		// ==============================正常系==============================
		DataBaseDataStore target01 = new MySqlDataBaseDataStore(this.getAccessableDataBaseAccessParameterMYSQL());
		target01.startTransaction();
		// ＝＝＝＝＝＝＝＝テーブル作成＝＝＝＝＝＝＝＝
		// テーブルを作成
		target01.createTable(createTableSql());
		// 再度同じテーブルを作成しようとした場合、例外が発生すること。
		try {
			target01.createTable(createTableSql());
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
		// ＝＝＝＝＝＝＝＝レコードを追加＝＝＝＝＝＝＝＝
		// レコードを追加
		target01.insert(insertSql_1234567890());
		// 再度レコードを追加しようとした場合、例外が発生すること。
		try {
			target01.insert(insertSql_1234567890());
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
		// ＝＝＝＝＝＝＝＝レコードを更新＝＝＝＝＝＝＝＝
		// レコードを更新
		int updateCount01 = target01.update(updateSql_1234567890_to_0987654321());
		assertEquals(updateCount01 , 1);
		
		// 再度更新しようした場合、更新件数が０件で有ること
		int updateCount02 = target01.update(updateSql_1234567890_to_0987654321());
		assertEquals(updateCount02 , 0);
		
		// 不正なSQLを実行した場合、例外が発生すること
		try {
			target01.update(updateFaileSql());
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
		// ＝＝＝＝＝＝＝＝レコードを取得＝＝＝＝＝＝＝＝
		// レコードを取得
		target01.select(selectSql_0987654321());
		
		// 不正なSQLを実行した場合、例外が発生すること
		try {
			target01.select(selectFaileSql());
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
		// ＝＝＝＝＝＝＝＝レコードを削除＝＝＝＝＝＝＝＝
		// レコードを削除
		int deleteResult01 = target01.delete(deleteSql_0987654321());
		assertEquals(deleteResult01, 1);
		
		// 再度レコードを削除した場合、更新件数が０件であること
		int deleteResult02 = target01.delete(deleteSql_0987654321());
		assertEquals(deleteResult02, 0);
		
		// 不正なSQLを実行した場合、例外が発生すること
		try {
			target01.delete(deleteFaileSql());
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
		// ＝＝＝＝＝＝＝＝テーブルを削除＝＝＝＝＝＝＝＝
		// テーブルを削除
		target01.dropTable(dropTableSql());
		// 再度同じテーブルを削除しようとした場合、例外が発生すること。
		try {
			target01.dropTable(dropTableSql());
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		target01.finishTransaction();
		
		// ==============================異常系==============================
		DataBaseDataStore target02 = new MySqlDataBaseDataStore(this.getAccessableDataBaseAccessParameterORACLE());
		// ＝＝＝＝＝＝＝＝テーブル作成＝＝＝＝＝＝＝＝
		try {
			target02.createTable(createTableSql());
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), TRANSACTION_IS_NOT_STARTED);
		}
		// ＝＝＝＝＝＝＝＝レコードを追加＝＝＝＝＝＝＝＝
		try {
			target02.insert(insertSql_1234567890());
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), TRANSACTION_IS_NOT_STARTED);
		}
		// ＝＝＝＝＝＝＝＝レコードを更新＝＝＝＝＝＝＝＝
		try {
			target02.update(updateSql_1234567890_to_0987654321());
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), TRANSACTION_IS_NOT_STARTED);
		}
		
		// ＝＝＝＝＝＝＝＝レコードを取得＝＝＝＝＝＝＝＝
		try {
			target02.select(selectSql_1234567890());
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), TRANSACTION_IS_NOT_STARTED);
		}
		
		// ＝＝＝＝＝＝＝＝レコードを削除＝＝＝＝＝＝＝＝
		try {
			target02.delete(deleteSql_1234567890());
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), TRANSACTION_IS_NOT_STARTED);
		}
		// ＝＝＝＝＝＝＝＝テーブルを削除＝＝＝＝＝＝＝＝
		try {
			target02.dropTable(dropTableSql());
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), TRANSACTION_IS_NOT_STARTED);
		}
	}
	
	@Test
	public void getTable() {
		try {
			DataBaseDataStore target = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterORACLE());
			target.startTransaction();
			List<TableMetaData> tableList = target.getTables();
			assertThat(tableList.size(), is(1));
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void test_equals() {
		try {
			// トランザクション開始前は同一のパラメータで有るため、hashcode、qualsは一致すること
			DataBaseDataStore target_01_01 = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
			DataBaseDataStore target_01_02 = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
			DataBaseDataStore target_01_03 = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
			List<Object> faileList_01 = new ArrayList<Object>();
			DataBaseDataStore fail_01_01 = new MySqlDataBaseDataStore(super.getAccessFaileDataBaseAccessParameterMYSQL());
			faileList_01.add(fail_01_01);
			testEquals(target_01_01, target_01_02, target_01_03, faileList_01);
			
			DataBaseDataStore target_02_01 = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
			DataBaseDataStore target_02_02 = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
			target_02_02.startTransaction();
			assertFalse(target_02_01.equals(target_02_02));
			
			DataBaseDataStore target_03_01 = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
			target_03_01.startTransaction();
			DataBaseDataStore target_03_02 = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
			assertFalse(target_03_01.equals(target_03_02));
			
			DataBaseDataStore target_04_01 = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
			target_04_01.startTransaction();
			DataBaseDataStore target_04_02 = new MySqlDataBaseDataStore(super.getAccessableDataBaseAccessParameterMYSQL());
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
			DataBaseAccessParameter param = super.getAccessableDataBaseAccessParameterMYSQL();
			DataBaseDataStore target = new MySqlDataBaseDataStore(param);
			assertEquals(target.toString(), "CONNECTION_HASH=[Transaction has not been started]," + param.toString());
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
}
