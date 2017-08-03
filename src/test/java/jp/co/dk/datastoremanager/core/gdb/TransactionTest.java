package jp.co.dk.datastoremanager.core.gdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.dk.datastoremanager.core.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.gdb.Cypher;
import jp.co.dk.datastoremanager.core.gdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.gdb.Transaction;
import mockit.Expectations;

import org.junit.Test;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

public class TransactionTest extends DataStoreManagerTestFoundation{
	
	public List<Map<String, Object>> getData(ResultSet resultSet, String name) throws DataStoreManagerException, SQLException {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(); 
		while(resultSet.next()) result.add((Map<String, Object>)resultSet.getObject(name));
		return result;
	}
	
	public void deleteAll(Transaction transaction) throws DataStoreManagerException {
		// 全ノード、リレーションを削除
		Cypher cypher = new Cypher("MATCH (n) OPTIONAL MATCH (n)-[r]-()	DELETE n,r");
		transaction.execute(cypher);
		transaction.commit();
	}
	
	@Test
	public void constractor() {
		// ==============================正常系==============================
		// 引数に設定された接続先に接続出来た場合、インスタンス生成に成功し、フィールドに各値を保持していること
		try {
			Transaction target = new Transaction(this.getAccessableDataBaseAccessParameterGDB());
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
			final DataBaseAccessParameter param = this.getAccessFaileDataBaseAccessParameterGDB();
        	new Expectations(param) {{
        		param.getDriver(); result = DataBaseDriverConstants.POSTGRESSQL;
            }};
            new Transaction(param);
            fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_READ_DRIVER);
		}
		
		// 引数に設定された接続先に接続出来なかった場合、例外が発生すること
		try {
			final DataBaseAccessParameter param = this.getAccessFaileDataBaseAccessParameterGDB();
            new Transaction(param);
            fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_CREATE_CONNECTION);
		}
	}
	
	@Test
	public void execute() throws DataStoreManagerException, ParseException {
		
		final Transaction target = new Transaction(this.getAccessableDataBaseAccessParameterGDB());
		
		// 全ノード、リレーションを削除
		deleteAll(target);
		
		try {
			// ノードを追加
			Cypher cypher = new Cypher("CREATE(:USER{name:{1},age:{2}, sex:{3}})");
			cypher.setParameter("test");
			cypher.setParameter(20);
			cypher.setParameter(true);
			target.execute(cypher);
			target.commit();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
		// 不正なCypherで実施した場合、エラーが発生すること
		try {
			target.execute(new Cypher("XXX"));
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_EXECUTE_SQL);
		}
		
		// 全ノード、リレーションを削除
		deleteAll(target);
		
		// トランザクションをクローズ
		target.close();
	}
	
	@Test
	public void commit() throws DataStoreManagerException, ParseException, SQLException {
		// ==============================正常系==============================
		// ２つのトランザクションの間でSELECTを行った場合、コミットを行った場合に限り、他のトランザクションから参照可能になること
		DataBaseAccessParameter param = this.getAccessableDataBaseAccessParameterGDB();
		Transaction target_01 = new Transaction(param);
		Transaction target_02 = new Transaction(param);
		Transaction target_03 = new Transaction(param);
		
		// 全ノード、リレーションを削除
		deleteAll(target_01);
		
		Cypher cypher1 = new Cypher("CREATE(:USER{name:{1},age:{2}, sex:{3}})");
		cypher1.setParameter("test");
		cypher1.setParameter(20);
		cypher1.setParameter(true);
		
		Cypher cypher2 = new Cypher("MATCH(user:USER{name:{1},age:{2}, sex:{3}}) RETURN user");
		cypher2.setParameter("test");
		cypher2.setParameter(20);
		cypher2.setParameter(true);
		
		Cypher cypher3 = new Cypher("MATCH(user:USER{name:{1},age:{2}, sex:{3}}) DELETE user");
		cypher3.setParameter("test");
		cypher3.setParameter(20);
		cypher3.setParameter(true);
		
		// トランザクション０２でレコードを追加);
		target_02.execute(cypher1);
		
		// トランザクション０２でレコードを取得、同じトランザクションのため、取得できる。
		assertEquals(getData(target_02.execute(cypher2), "user").size(), 1);
		
		// トランザクション０３でレコードを取得、異なるトランザクションのため、取得できない。
		assertEquals(getData(target_03.execute(cypher2), "user").size(), 0);
		
		// トランザクション０２でコミットを実施
		target_02.commit();
		
		// トランザクション０３でレコードを取得、コミットされたため、異なるトランザクションのでも取得できる。
		assertEquals(getData(target_03.execute(cypher2), "user").size(), 1);
		
		// コミット後、継続して処理を継続できること
		// トランザクション０２で削除を実施
		target_02.execute(cypher3);
		
		// トランザクション０２でレコードを取得、同じトランザクションのため、0件が取得される。
		assertEquals(getData(target_02.execute(cypher2), "user").size(), 0);
			
		// トランザクション０３でレコードを取得、異なるトランザクションのため、取得できること。
		assertEquals(getData(target_03.execute(cypher2), "user").size(), 1);
		
		// トランザクション０２でコミットを実施
		target_02.commit();
		
		// トランザクション０３でレコードを取得、異なるトランザクションのため、取得できること。
		assertEquals(getData(target_03.execute(cypher2), "user").size(), 0);
	
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
		
		// 全ノード、リレーションを削除
		deleteAll(target_01);
		target_01.close();
		target_02.close();
		target_03.close();
		
	}
	
	@Test
	public void rollback() throws DataStoreManagerException, ParseException, SQLException {
		// ==============================正常系==============================
		DataBaseAccessParameter param = this.getAccessableDataBaseAccessParameterGDB();
		Transaction target_01 = new Transaction(param);
		Transaction target_02 = new Transaction(param);
		Transaction target_03 = new Transaction(param);
		
		// 全ノード、リレーションを削除
		deleteAll(target_01);
		
		Cypher cypher1 = new Cypher("CREATE(:USER{name:{1},age:{2}, sex:{3}})");
		cypher1.setParameter("test");
		cypher1.setParameter(20);
		cypher1.setParameter(true);
		
		Cypher cypher2 = new Cypher("MATCH(user:USER{name:{1},age:{2}, sex:{3}}) RETURN user");
		cypher2.setParameter("test");
		cypher2.setParameter(20);
		cypher2.setParameter(true);
		
		Cypher cypher3 = new Cypher("MATCH(user:USER{name:{1},age:{2}, sex:{3}}) DELETE user");
		cypher3.setParameter("test");
		cypher3.setParameter(20);
		cypher3.setParameter(true);
		
		// トランザクション０２でレコードを追加);
		target_02.execute(cypher1);
		
		// トランザクション０２でレコードを取得、同じトランザクションのため、取得できる。
		assertEquals(getData(target_02.execute(cypher2), "user").size(), 1);
		
		// トランザクション０３でレコードを取得、異なるトランザクションのため、取得できない。
		assertEquals(getData(target_03.execute(cypher2), "user").size(), 0);
		
		// トランザクション０２でロールバックを実施
		target_02.rollback();
		
		// トランザクション０２でレコードを取得、ロールバックしたため、取得できないこと。
		assertEquals(getData(target_02.execute(cypher2), "user").size(), 0);
		
		// トランザクション０３でレコードを取得、ロールバックしたため、取得できないこと。
		assertEquals(getData(target_03.execute(cypher2), "user").size(), 0);
		
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
		
		// 全ノード、リレーションを削除
		deleteAll(target_01);
		target_01.close();
		target_02.close();
		target_03.close();
		
	}
	
	@Test
	public void close() {
		// ==============================正常系==============================
		// 正常系はcommit、rollbackにて実施済み
		
		// ==============================異常系==============================
		try {
			DataBaseAccessParameter param = this.getAccessableDataBaseAccessParameterGDB();
			Transaction target_01 = new Transaction(param);
			target_01.close();
			target_01.close();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), FAILE_TO_CLOSE);
		}
	}
	
	@Test
	public void test_equals() throws DataStoreManagerException {
		DataBaseAccessParameter param = this.getAccessableDataBaseAccessParameterGDB();
		Transaction target_01 = new Transaction(param);
		
		List<Object> difflist = new ArrayList<Object>();
		Transaction target_02 = new Transaction(param);
		difflist.add(target_02);
		testEquals(target_01, target_01, target_01, difflist);
	}
	
}
