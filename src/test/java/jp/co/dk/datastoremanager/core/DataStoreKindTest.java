package jp.co.dk.datastoremanager.core;

import jp.co.dk.datastoremanager.core.DataStoreKind;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;

import org.junit.Test;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

public class DataStoreKindTest extends DataStoreManagerTestFoundation{
	
	/**
	 * convertに"oracle"を渡した場合、DataStoreKindが返却されること。
	 */
	@Test
	public void convert_success01() throws DataStoreManagerException{
		assertEquals(DataStoreKind.convert("oracle"), DataStoreKind.ORACLE);
	}
	
	/**
	 * convertに"mysql"を渡した場合、DataStoreKindが返却されること。
	 */
	@Test
	public void convert_success02() throws DataStoreManagerException{
		assertEquals(DataStoreKind.convert("mysql"), DataStoreKind.MYSQL);
	}
	
	/**
	 * convertに"postgressql"を渡した場合、DataStoreKindが返却されること。
	 */
	@Test
	public void convert_success03() throws DataStoreManagerException{
		assertEquals(DataStoreKind.convert("postgresql"), DataStoreKind.POSTGRESQL);
	}
	
	/**
	 * convertに"csv"を渡した場合、DataStoreKindが返却されること。
	 */
	@Test
	public void convert_success04() throws DataStoreManagerException{
		assertEquals(DataStoreKind.convert("csv"), DataStoreKind.CSV);
	}
	
	/**
	 * convertにnullを渡した場合、例外が出力されること。
	 */
	@Test
	public void convert_error01() {
		try {
			DataStoreKind.convert(null);
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DATASTORE_KIND_VALUE_IS_FAILE);
			assertEquals(e.getMessage(), "データストア種別に設定されている値が不正です。デタストア種別設定値=[null]");
		}
	}
	
	/**
	 * convertに不正な文字列を渡した場合、例外が出力されること。
	 */
	@Test
	public void convert_error02() {
		try {
			DataStoreKind.convert("");
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DATASTORE_KIND_VALUE_IS_FAILE);
			assertEquals(e.getMessage(), "データストア種別に設定されている値が不正です。デタストア種別設定値=[]");
		}
	}
	
	/**
	 * convertに不正な文字列を渡した場合、例外が出力されること。
	 */
	@Test
	public void convert_error03() {
		try {
			DataStoreKind.convert("test");
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DATASTORE_KIND_VALUE_IS_FAILE);
			assertEquals(e.getMessage(), "データストア種別に設定されている値が不正です。デタストア種別設定値=[test]");
		}
	}
}
