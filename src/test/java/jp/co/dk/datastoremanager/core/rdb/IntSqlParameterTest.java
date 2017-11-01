package jp.co.dk.datastoremanager.core.rdb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.rdb.IntColumnData;

import org.junit.Test;

public class IntSqlParameterTest extends DataStoreManagerTestFoundation{

	@Test
	public void constractor() throws ParseException {
		// ==============================正常系==============================
		// コンストラクタに日付オブジェクトを設定した場合、正常に値が設定できること。
		IntColumnData parameter = new IntColumnData(0);
		assertEquals(parameter.parameter, 0);
	}
	
	@Test
	public void test_equals() throws ParseException {
		// コンストラクタに日付オブジェクトを設定した場合、正常に値が設定できること。
		IntColumnData parameter1 = new IntColumnData(100);
		IntColumnData parameter2 = new IntColumnData(100);
		IntColumnData parameter3 = new IntColumnData(100);
		
		List<Object> faileList = new ArrayList<Object>();
		IntColumnData faileParam01 = new IntColumnData(99);
		IntColumnData faileParam02 = new IntColumnData(101);
		faileList.add(faileParam01);
		faileList.add(faileParam02);
		testEquals(parameter1, parameter2, parameter3, faileList);
	}
	
	@Test
	public void test_toString() throws ParseException {
		IntColumnData parameter1 = new IntColumnData(123);
		assertEquals(parameter1.toString(), "123(int)");
	}
}
