package jp.co.dk.datastoremanager.core.gdb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.gdb.NumericCypherParameter;

import org.junit.Test;

public class NumericCypherParameterTest extends DataStoreManagerTestFoundation{

	@Test
	public void constractor() throws ParseException {
		// ==============================正常系==============================
		// コンストラクタに日付オブジェクトを設定した場合、正常に値が設定できること。
		NumericCypherParameter parameter = new NumericCypherParameter(0);
		assertEquals(parameter.parameter, 0);
	}
	
	@Test
	public void test_equals() throws ParseException {
		// コンストラクタに日付オブジェクトを設定した場合、正常に値が設定できること。
		NumericCypherParameter parameter1 = new NumericCypherParameter(100);
		NumericCypherParameter parameter2 = new NumericCypherParameter(100);
		NumericCypherParameter parameter3 = new NumericCypherParameter(100);
		
		List<Object> faileList = new ArrayList<Object>();
		NumericCypherParameter faileParam01 = new NumericCypherParameter(99);
		NumericCypherParameter faileParam02 = new NumericCypherParameter(101);
		faileList.add(faileParam01);
		faileList.add(faileParam02);
		testEquals(parameter1, parameter2, parameter3, faileList);
	}
	
	@Test
	public void test_toString() throws ParseException {
		NumericCypherParameter parameter1 = new NumericCypherParameter(123);
		assertEquals(parameter1.toString(), "123(int)");
	}
}
