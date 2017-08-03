package jp.co.dk.datastoremanager.core.gdb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.gdb.Cypher;
import jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage;

import org.junit.Test;

public class CypherTest extends DataStoreManagerTestFoundation{
	
	@Test
	public void constractor() {
		// ==============================正常系==============================
		try {
			Cypher Cypher1 = new Cypher("MATCH(N) RETURN N");
			assertEquals(Cypher1.toString(), "CYPHER=[MATCH(N) RETURN N] PARAMETER=[NOTHING]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// ==============================異常系==============================
		// 引数にNULLを指定した場合、例外が送出されること
		try {
			new Cypher(null);
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.FAILE_TO_CREATE_SQL_OBJECT);
		}
		// 引数に空文字を指定した場合、例外が送出されること
		try {
			new Cypher("");
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.FAILE_TO_CREATE_SQL_OBJECT);
		}
		
	}
	
	@Test
	public void setParameter_String() {
		// ==============================正常系==============================
		// Cypherにパラメータを設定した際に、正常にパラメータリストに追加されること
		try {
			Cypher Cypher1 = new Cypher("MATCH(N) RETURN N");
			Cypher1.setParameter("1234567890");
			assertEquals(Cypher1.getParameterList().size(), 1);
			assertEquals(Cypher1.toString(), "CYPHER=[MATCH(N) RETURN N] PARAMETER=[1234567890(string)]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		// ==============================異常系==============================
		// Cypherにパラメータをnullを設定した際に、例外が送出されること
//		Cypher Cypher1 = null;
//		try {
//			Cypher1 = new Cypher("SELECT * FROM USERS WHERE USERID=?");
//			String param = null;
//			Cypher1.setParameter(param);
//			fail();
//		} catch (DataStoreManagerException e) {
//			assertEquals(e.getMessageObj(), DataStoreManagerMessage.Cypher_PARAMETER_IS_NOT_SET);
//			assertEquals(Cypher1.getParameterList().size(), 0);
//			assertEquals(Cypher1.toString(), "CYPHER=[SELECT * FROM USERS WHERE USERID=?] PARAMETER=[NOTHING]");
//		}
		
		// Cypherにパラメータを空文字を設定した際に、例外が送出されること
//		try {
//			Cypher1 = new Cypher("SELECT * FROM USERS WHERE USERID=?");
//			Cypher1.setParameter("");
//			fail();
//		} catch (DataStoreManagerException e) {
//			assertEquals(e.getMessageObj(), DataStoreManagerMessage.Cypher_PARAMETER_IS_NOT_SET);
//			assertEquals(Cypher1.getParameterList().size(), 0);
//			assertEquals(Cypher1.toString(), "CYPHER=[SELECT * FROM USERS WHERE USERID=?] PARAMETER=[NOTHING]");
//		}
	}
	
	@Test
	public void setParameter_Int() {
		// ==============================正常系==============================
		// Cypherにパラメータを設定した際に、正常にパラメータリストに追加されること
		try {
			Cypher Cypher1 = new Cypher("MATCH(N) RETURN N");
			Cypher1.setParameter(20);
			assertEquals(Cypher1.getParameterList().size(), 1);
			assertEquals(Cypher1.toString(), "CYPHER=[MATCH(N) RETURN N] PARAMETER=[20(int)]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// Cypherにパラメータを設定した際に、正常にパラメータリストに追加されること
		try {
			Cypher Cypher1 = new Cypher("MATCH(N) RETURN N");
			Cypher1.setParameter(-20);
			assertEquals(Cypher1.getParameterList().size(), 1);
			assertEquals(Cypher1.toString(), "CYPHER=[MATCH(N) RETURN N] PARAMETER=[-20(int)]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// Cypherにパラメータを設定した際に、正常にパラメータリストに追加されること
		try {
			Cypher Cypher1 = new Cypher("MATCH(N) RETURN N");
			Cypher1.setParameter(0);
			assertEquals(Cypher1.getParameterList().size(), 1);
			assertEquals(Cypher1.toString(), "CYPHER=[MATCH(N) RETURN N] PARAMETER=[0(int)]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void test_equals() throws ParseException {
		// ==============================正常系==============================
		try {
			Cypher Cypher1 = new Cypher("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			Cypher1.setParameter("1234567890");
			Cypher1.setParameter(20);
			Cypher1.setParameter(true);
			
			Cypher Cypher2 = new Cypher("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			Cypher2.setParameter("1234567890");
			Cypher2.setParameter(20);
			Cypher2.setParameter(true);
			
			Cypher Cypher3 = new Cypher("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			Cypher3.setParameter("1234567890");
			Cypher3.setParameter(20);
			Cypher3.setParameter(true);
			
			List<Object> falseList = new ArrayList<Object>();
			Cypher failCypher_01 = new Cypher("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=? ");
			failCypher_01.setParameter("1234567890");
			failCypher_01.setParameter(20);
			failCypher_01.setParameter(true);
			falseList.add(failCypher_01);
			
			Cypher failCypher_02 = new Cypher("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			failCypher_02.setParameter("123456789");
			failCypher_02.setParameter(20);
			failCypher_02.setParameter(true);
			falseList.add(failCypher_02);
			
			Cypher failCypher_03 = new Cypher("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			failCypher_03.setParameter("1234567890");
			failCypher_03.setParameter(21);
			failCypher_03.setParameter(true);
			falseList.add(failCypher_03);
			
			Cypher failCypher_04 = new Cypher("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			failCypher_04.setParameter("1234567890");
			failCypher_04.setParameter(20);
			failCypher_04.setParameter(false);
			falseList.add(failCypher_04);
			
			super.testEquals(Cypher1, Cypher2, Cypher3, falseList);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void test_toString() throws ParseException {
		// ==============================正常系==============================
		// Cypherにパラメータを指定しなかった場合、toStringは指定のStringの値を返却すること
		try {
			Cypher Cypher1 = new Cypher("SELECT * FROM USERS");
			assertEquals(Cypher1.toString(), "CYPHER=[SELECT * FROM USERS] PARAMETER=[NOTHING]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// Cypherにパラメータを指定した場合、toStringは指定のStringの値を返却すること
		try {
			Cypher Cypher2 = new Cypher("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			Cypher2.setParameter("1234567890");
			Cypher2.setParameter(20);
			Cypher2.setParameter(true);
			assertEquals(Cypher2.toString(), "CYPHER=[SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?] PARAMETER=[1234567890(string), 20(int), true(boolean)]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
}
