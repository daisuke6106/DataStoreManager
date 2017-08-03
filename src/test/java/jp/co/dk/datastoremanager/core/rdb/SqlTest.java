package jp.co.dk.datastoremanager.core.rdb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage;
import jp.co.dk.datastoremanager.core.rdb.Sql;

import org.junit.Test;

public class SqlTest extends DataStoreManagerTestFoundation{
	
	@Test
	public void constractor() {
		// ==============================正常系==============================
		try {
			Sql sql1 = new Sql("SELECT * FROM USERS");
			assertEquals(sql1.toString(), "SQL=[SELECT * FROM USERS] PARAMETER=[NOTHING]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// ==============================異常系==============================
		// 引数にNULLを指定した場合、例外が送出されること
		try {
			new Sql(null);
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.FAILE_TO_CREATE_SQL_OBJECT);
		}
		// 引数に空文字を指定した場合、例外が送出されること
		try {
			new Sql("");
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.FAILE_TO_CREATE_SQL_OBJECT);
		}
		
	}
	
	@Test
	public void setParameter_String() {
		// ==============================正常系==============================
		// SQLにパラメータを設定した際に、正常にパラメータリストに追加されること
		try {
			Sql sql1 = new Sql("SELECT * FROM USERS WHERE USERID=?");
			sql1.setParameter("1234567890");
			assertEquals(sql1.getParameterList().size(), 1);
			assertEquals(sql1.toString(), "SQL=[SELECT * FROM USERS WHERE USERID=?] PARAMETER=[1234567890(string)]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		// ==============================異常系==============================
		// SQLにパラメータをnullを設定した際に、例外が送出されること
//		Sql sql1 = null;
//		try {
//			sql1 = new Sql("SELECT * FROM USERS WHERE USERID=?");
//			String param = null;
//			sql1.setParameter(param);
//			fail();
//		} catch (DataStoreManagerException e) {
//			assertEquals(e.getMessageObj(), DataStoreManagerMessage.SQL_PARAMETER_IS_NOT_SET);
//			assertEquals(sql1.getParameterList().size(), 0);
//			assertEquals(sql1.toString(), "SQL=[SELECT * FROM USERS WHERE USERID=?] PARAMETER=[NOTHING]");
//		}
		
		// SQLにパラメータを空文字を設定した際に、例外が送出されること
//		try {
//			sql1 = new Sql("SELECT * FROM USERS WHERE USERID=?");
//			sql1.setParameter("");
//			fail();
//		} catch (DataStoreManagerException e) {
//			assertEquals(e.getMessageObj(), DataStoreManagerMessage.SQL_PARAMETER_IS_NOT_SET);
//			assertEquals(sql1.getParameterList().size(), 0);
//			assertEquals(sql1.toString(), "SQL=[SELECT * FROM USERS WHERE USERID=?] PARAMETER=[NOTHING]");
//		}
	}
	
	@Test
	public void setParameter_Int() {
		// ==============================正常系==============================
		// SQLにパラメータを設定した際に、正常にパラメータリストに追加されること
		try {
			Sql sql1 = new Sql("SELECT * FROM USERS WHERE AGE=?");
			sql1.setParameter(20);
			assertEquals(sql1.getParameterList().size(), 1);
			assertEquals(sql1.toString(), "SQL=[SELECT * FROM USERS WHERE AGE=?] PARAMETER=[20(int)]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// SQLにパラメータを設定した際に、正常にパラメータリストに追加されること
		try {
			Sql sql1 = new Sql("SELECT * FROM USERS WHERE AGE=?");
			sql1.setParameter(-20);
			assertEquals(sql1.getParameterList().size(), 1);
			assertEquals(sql1.toString(), "SQL=[SELECT * FROM USERS WHERE AGE=?] PARAMETER=[-20(int)]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// SQLにパラメータを設定した際に、正常にパラメータリストに追加されること
		try {
			Sql sql1 = new Sql("SELECT * FROM USERS WHERE AGE=?");
			sql1.setParameter(0);
			assertEquals(sql1.getParameterList().size(), 1);
			assertEquals(sql1.toString(), "SQL=[SELECT * FROM USERS WHERE AGE=?] PARAMETER=[0(int)]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void setParameter_Date() {
		// ==============================正常系==============================
		// SQLにパラメータを設定した際に、正常にパラメータリストに追加されること
		try {
			Sql sql1 = new Sql("SELECT * FROM USERS WHERE BIRTHDAY=?");
			sql1.setParameter(super.createDateByString("20130101235959"));
			assertEquals(sql1.getParameterList().size(), 1);
			assertEquals(sql1.toString(), "SQL=[SELECT * FROM USERS WHERE BIRTHDAY=?] PARAMETER=[20130101235959(date)]");
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (ParseException e) {
			fail(e);
		}
		
		// ==============================異常系==============================
		// SQLにパラメータをnullを設定した際に、例外が送出されること
//		Sql sql1 = null;
//		try {
//			sql1 = new Sql("SELECT * FROM USERS WHERE BIRTHDAY=?");
//			Date param = null;
//			sql1.setParameter(param);
//			fail();
//		} catch (DataStoreManagerException e) {
//			assertEquals(e.getMessageObj(), DataStoreManagerMessage.SQL_PARAMETER_IS_NOT_SET);
//			assertEquals(sql1.getParameterList().size(), 0);
//			assertEquals(sql1.toString(), "SQL=[SELECT * FROM USERS WHERE BIRTHDAY=?] PARAMETER=[NOTHING]");
//		}
	}
	
	@Test
	public void test_equals() throws ParseException {
		// ==============================正常系==============================
		try {
			Sql sql1 = new Sql("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			sql1.setParameter("1234567890");
			sql1.setParameter(20);
			sql1.setParameter(super.createDateByString("19900401000000"));
			
			Sql sql2 = new Sql("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			sql2.setParameter("1234567890");
			sql2.setParameter(20);
			sql2.setParameter(super.createDateByString("19900401000000"));
			
			Sql sql3 = new Sql("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			sql3.setParameter("1234567890");
			sql3.setParameter(20);
			sql3.setParameter(super.createDateByString("19900401000000"));
			
			List<Object> falseList = new ArrayList<Object>();
			Sql failSql_01 = new Sql("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=? ");
			failSql_01.setParameter("1234567890");
			failSql_01.setParameter(20);
			failSql_01.setParameter(super.createDateByString("19900401000000"));
			falseList.add(failSql_01);
			
			Sql failSql_02 = new Sql("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			failSql_02.setParameter("123456789");
			failSql_02.setParameter(20);
			failSql_02.setParameter(super.createDateByString("19900401000000"));
			falseList.add(failSql_02);
			
			Sql failSql_03 = new Sql("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			failSql_03.setParameter("1234567890");
			failSql_03.setParameter(21);
			failSql_03.setParameter(super.createDateByString("19900401000000"));
			falseList.add(failSql_03);
			
			Sql failSql_04 = new Sql("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			failSql_04.setParameter("1234567890");
			failSql_04.setParameter(20);
			failSql_04.setParameter(super.createDateByString("19900401000001"));
			falseList.add(failSql_04);
			
			super.testEquals(sql1, sql2, sql3, falseList);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void test_toString() throws ParseException {
		// ==============================正常系==============================
		// SQLにパラメータを指定しなかった場合、toStringは指定のStringの値を返却すること
		try {
			Sql sql1 = new Sql("SELECT * FROM USERS");
			assertEquals(sql1.toString(), "SQL=[SELECT * FROM USERS] PARAMETER=[NOTHING]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// SQLにパラメータを指定した場合、toStringは指定のStringの値を返却すること
		try {
			Sql sql2 = new Sql("SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?");
			sql2.setParameter("1234567890");
			sql2.setParameter(20);
			sql2.setParameter(createDateByString("19800101000000"));
			assertEquals(sql2.toString(), "SQL=[SELECT * FROM USERS WHERE USERID=? AND AGE=? AND BIRTHDAY=?] PARAMETER=[1234567890(string), 20(int), 19800101000000(date)]");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
}
