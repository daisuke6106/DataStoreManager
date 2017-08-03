package jp.co.dk.datastoremanager.core.gdb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.gdb.BooleanCypherParameter;

import org.junit.Test;

public class BooleanCypherParameterTest extends DataStoreManagerTestFoundation{

	@Test
	public void constractor() throws ParseException {
		// ==============================正常系==============================
		// コンストラクタに文字列オブジェクトを設定した場合、正常に値が設定できること。
		BooleanCypherParameter parameter = new BooleanCypherParameter(true);
		assertEquals(parameter.parameter, true);
	}
	
	@Test
	public void test_equals() throws ParseException {
		// コンストラクタに文字列オブジェクトを設定した場合、正常に値が設定できること。
		BooleanCypherParameter parameter1 = new BooleanCypherParameter(true);
		BooleanCypherParameter parameter2 = new BooleanCypherParameter(true);
		BooleanCypherParameter parameter3 = new BooleanCypherParameter(true);
		
		List<Object> faileList = new ArrayList<Object>();
		BooleanCypherParameter faileParam01 = new BooleanCypherParameter(false);
		BooleanCypherParameter faileParam02 = new BooleanCypherParameter(false);
		faileList.add(faileParam01);
		faileList.add(faileParam02);
		testEquals(parameter1, parameter2, parameter3, faileList);
	}
	
	@Test
	public void test_toString() throws ParseException {
		BooleanCypherParameter parameter1 = new BooleanCypherParameter(false);
		assertEquals(parameter1.toString(), "false(boolean)");
	}
}
