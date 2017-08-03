package jp.co.dk.datastoremanager.core.rdb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.rdb.IntSqlParameter;

import org.junit.Test;

public class IntSqlParameterTest extends DataStoreManagerTestFoundation{

	@Test
	public void constractor() throws ParseException {
		// ==============================正常系==============================
		// コンストラクタに日付オブジェクトを設定した場合、正常に値が設定できること。
		IntSqlParameter parameter = new IntSqlParameter(0);
		assertEquals(parameter.parameter, 0);
	}
	
	@Test
	public void test_equals() throws ParseException {
		// コンストラクタに日付オブジェクトを設定した場合、正常に値が設定できること。
		IntSqlParameter parameter1 = new IntSqlParameter(100);
		IntSqlParameter parameter2 = new IntSqlParameter(100);
		IntSqlParameter parameter3 = new IntSqlParameter(100);
		
		List<Object> faileList = new ArrayList<Object>();
		IntSqlParameter faileParam01 = new IntSqlParameter(99);
		IntSqlParameter faileParam02 = new IntSqlParameter(101);
		faileList.add(faileParam01);
		faileList.add(faileParam02);
		testEquals(parameter1, parameter2, parameter3, faileList);
	}
	
	@Test
	public void test_toString() throws ParseException {
		IntSqlParameter parameter1 = new IntSqlParameter(123);
		assertEquals(parameter1.toString(), "123(int)");
	}
}
