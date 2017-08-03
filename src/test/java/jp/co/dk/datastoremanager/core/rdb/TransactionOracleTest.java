package jp.co.dk.datastoremanager.core.rdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.core.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.rdb.Transaction;
import mockit.Expectations;

import org.junit.Test;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

public class TransactionOracleTest extends DataStoreManagerTestFoundation{
	
	@Test
	public void constractor() {
		// ==============================正常系==============================
		// 引数に設定された接続先に接続出来た場合、インスタンス生成に成功し、フィールドに各値を保持していること
		try {
			Transaction target = new Transaction(this.getAccessableDataBaseAccessParameterORACLE());
			assertNotNull(target.connection);
			assertNotNull(target.dataBaseAccessParameter);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// ==============================異常系==============================
		// 引数にNULLを指定した場合、例外が発生すること
		try {
			new Transaction(null);
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), PARAMETER_IS_NOT_SET);
		}
		
		// 引数に設定されたドライバークラスの読み込み出来なかった場合、例外が発生すること
		try {
			final DataBaseAccessParameter param = this.getAccessFaileDataBaseAccessParameterORACLE();
        	new Expectations(param) {{
        		param.getDriver(); result = DataBaseDriverConstants.POSTGRESSQL;
            }};
            new Transaction(param);
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_READ_DRIVER);
		}
		
		// 引数に設定された接続先に接続出来なかった場合、例外が発生すること
		try {
			final DataBaseAccessParameter param = this.getAccessFaileDataBaseAccessParameterORACLE();
            new Transaction(param);
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_CREATE_CONNECTION);
		}
	}
	
	@Test
	public void execute() throws DataStoreManagerException, ParseException {
		
		Transaction target = new Transaction(this.getAccessableDataBaseAccessParameterORACLE());
		
		// ＝＝＝＝＝＝＝＝テーブル作成＝＝＝＝＝＝＝＝
		// テーブルを作成
		target.createTable(createTableSql());
		// 再度同じテーブルを作成しようとした場合、例外が発生すること。
		try {
			target.createTable(createTableSql());
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
		// ＝＝＝＝＝＝＝＝レコードを追加＝＝＝＝＝＝＝＝
		// レコードを追加
		target.insert(insertSql_1234567890());
		// 再度レコードを追加しようとした場合、例外が発生すること。
		try {
			target.insert(insertSql_1234567890());
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
		// ＝＝＝＝＝＝＝＝レコードを更新＝＝＝＝＝＝＝＝
		// レコードを更新
		int updateCount01 = target.update(updateSql_1234567890_to_0987654321());
		assertEquals(updateCount01 , 1);
		
		// 再度更新しようした場合、更新件数が０件で有ること
		int updateCount02 = target.update(updateSql_1234567890_to_0987654321());
		assertEquals(updateCount02 , 0);
		
		// 不正なSQLを実行した場合、例外が発生すること
		try {
			target.update(updateFaileSql());
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
		// ＝＝＝＝＝＝＝＝レコードを取得＝＝＝＝＝＝＝＝
		// レコードを取得
		target.select(selectSql_0987654321());
		
		// 不正なSQLを実行した場合、例外が発生すること
		try {
			target.select(selectFaileSql());
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
		// ＝＝＝＝＝＝＝＝レコードを削除＝＝＝＝＝＝＝＝
		// レコードを削除
		int deleteResult01 = target.delete(deleteSql_0987654321());
		assertEquals(deleteResult01, 1);
		
		// 再度レコードを削除した場合、更新件数が０件であること
		int deleteResult02 = target.delete(deleteSql_0987654321());
		assertEquals(deleteResult02, 0);
		
		// 不正なSQLを実行した場合、例外が発生すること
		try {
			target.delete(deleteFaileSql());
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
		// ＝＝＝＝＝＝＝＝テーブルを削除＝＝＝＝＝＝＝＝
		// テーブルを削除
		target.dropTable(dropTableSql());
		// 再度同じテーブルを削除しようとした場合、例外が発生すること。
		try {
			target.dropTable(dropTableSql());
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
		target.close();
	}
	
	@Test
	public void commit() throws DataStoreManagerException, ParseException, SQLException {
		// ==============================正常系==============================
		// ２つのトランザクションの間でSELECTを行った場合、コミットを行った場合に限り、他のトランザクションから参照可能になること
		DataBaseAccessParameter param = this.getAccessableDataBaseAccessParameterORACLE();
		Transaction target_01 = new Transaction(param);
		Transaction target_02 = new Transaction(param);
		Transaction target_03 = new Transaction(param);
		
		// トランザクション０１にてテーブルを作成
		target_01.createTable(createTableSql());
		
		// トランザクション０２でレコードを追加);
		target_02.insert(insertSql_1234567890());
		
		// トランザクション０２でレコードを取得、同じトランザクションのため、取得できる。
		ResultSet resultSe02_01 = target_02.select(selectCountSql_1234567890());
		int result02_01 = -1;
		while(resultSe02_01.next()) result02_01 = resultSe02_01.getInt("CNT");
		assertEquals(result02_01, 1);
		
			// トランザクション０３でレコードを取得、異なるトランザクションのため、取得できない。
			ResultSet resultSe03_01 = target_03.select(selectCountSql_1234567890());
			int result03_01 = -1;
			while(resultSe03_01.next()) result03_01 = resultSe03_01.getInt("CNT");
			assertEquals(result03_01, 0);
			
		// トランザクション０２でコミットを実施
		target_02.commit();
		
			// トランザクション０３でレコードを取得、コミットされたため、異なるトランザクションのでも取得できる。
			ResultSet resultSe03_02 = target_03.select(selectCountSql_1234567890());
			int result03_02 = -1;
			while(resultSe03_02.next()) result03_02 = resultSe03_02.getInt("CNT");
			assertEquals(result03_02, 1);
		
		// コミット後、継続して処理を継続できること
		// トランザクション０２で削除を実施
		target_02.delete(deleteSql_1234567890());
		
		// トランザクション０２でレコードを取得、同じトランザクションのため、0件が取得される。
		ResultSet resultSe02_03 = target_02.select(selectCountSql_1234567890());
		int result02_03 = -1;
		while(resultSe02_03.next()) result02_03 = resultSe02_03.getInt("CNT");
		assertEquals(result02_03, 0);
			
			// トランザクション０３でレコードを取得、異なるトランザクションのため、取得できること。
			ResultSet resultSe03_03 = target_03.select(selectCountSql_1234567890());
			int result03_03 = -1;
			while(resultSe03_03.next()) result03_03 = resultSe03_03.getInt("CNT");
			assertEquals(result03_03, 1);
		
		// トランザクション０２でコミットを実施
		target_02.commit();
		
			// トランザクション０３でレコードを取得、異なるトランザクションのため、取得できること。
			ResultSet resultSe03_04 = target_03.select(selectCountSql_1234567890());
			int result03_04 = -1;
			while(resultSe03_04.next()) result03_04 = resultSe03_04.getInt("CNT");
			assertEquals(result03_04, 0);
		
		target_02.close();
		target_03.close();
		
		// テーブルを削除
		target_01.dropTable(dropTableSql());
		target_01.close();
		
		// ==============================異常系==============================
		// トランザクション開始後、クローズ後、コミットした場合、例外が発生すること。
		try {
			Transaction target_04 = new Transaction(param);
			target_04.close();
			target_04.commit();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_COMMIT);
		}
		
	}
	
	@Test
	public void rollback() throws DataStoreManagerException, ParseException, SQLException {
		// ==============================正常系==============================
		DataBaseAccessParameter param = this.getAccessableDataBaseAccessParameterORACLE();
		Transaction target_01 = new Transaction(param);
		Transaction target_02 = new Transaction(param);
		Transaction target_03 = new Transaction(param);
		
		// トランザクション０１にてテーブルを作成
		target_01.createTable(createTableSql());
		
		// トランザクション０２でレコードを追加);
		target_02.insert(insertSql_1234567890());
		
		// トランザクション０２でレコードを取得、同じトランザクションのため、取得できる。
		ResultSet resultSe02_01 = target_02.select(selectCountSql_1234567890());
		int result02_01 = -1;
		while(resultSe02_01.next()) result02_01 = resultSe02_01.getInt("CNT");
		assertEquals(result02_01, 1);
		
			// トランザクション０３でレコードを取得、異なるトランザクションのため、取得できない。
			ResultSet resultSe03_01 = target_03.select(selectCountSql_1234567890());
			int result03_01 = -1;
			while(resultSe03_01.next()) result03_01 = resultSe03_01.getInt("CNT");
			assertEquals(result03_01, 0);
		
		// トランザクション０２でロールバックを実施
		target_02.rollback();
		
		// トランザクション０２でレコードを取得、ロールバックしたため、取得できないこと。
		ResultSet resultSe02_02 = target_02.select(selectCountSql_1234567890());
		int result02_02 = -1;
		while(resultSe02_02.next()) result02_02 = resultSe02_02.getInt("CNT");
		assertEquals(result02_02, 0);
		
			// トランザクション０３でレコードを取得、ロールバックしたため、取得できないこと。
			ResultSet resultSe03_02 = target_03.select(selectCountSql_1234567890());
			int result03_02 = -1;
			while(resultSe03_02.next()) result03_02 = resultSe03_02.getInt("CNT");
			assertEquals(result03_02, 0);
		
		target_02.close();
		target_03.close();
		
		// テーブルを削除
		target_01.dropTable(dropTableSql());
		target_01.close();
		
		// ==============================異常系==============================
		// トランザクション開始後、クローズ後、コミットした場合、例外が発生すること。
		try {
			Transaction target_04 = new Transaction(param);
			target_04.close();
			target_04.rollback();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_ROLLBACK);
		}
	}
	
	@Test
	public void close() {
		// ==============================正常系==============================
		// 正常系はcommit、rollbackにて実施済み
		
		// ==============================異常系==============================
		try {
			DataBaseAccessParameter param = this.getAccessableDataBaseAccessParameterORACLE();
			Transaction target_01 = new Transaction(param);
			target_01.close();
			target_01.close();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_CLOSE);
		}
	}
	
	@Test
	public void test_equals() throws DataStoreManagerException {
		DataBaseAccessParameter param = this.getAccessableDataBaseAccessParameterORACLE();
		Transaction target_01 = new Transaction(param);
		
		List<Object> difflist = new ArrayList<Object>();
		Transaction target_02 = new Transaction(param);
		difflist.add(target_02);
		testEquals(target_01, target_01, target_01, difflist);
	}
	
}
