package jp.co.dk.datastoremanager.core.rdb;

import static org.junit.Assert.*;
import jp.co.dk.datastoremanager.core.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.core.DataStoreKind;

import org.junit.Test;

public class DataBaseDriverConstantsTest {

	@Test
	public void getUrl() {
		// ==============================正常系==============================
		// 引数にサーバ名、データベース名を指定した場合、正常に置換された値が返却されること
		assertEquals(DataBaseDriverConstants.ORACLE.getUrl("255.255.255.255", "test_db"), "jdbc:oracle:thin:@255.255.255.255:test_db");
		assertEquals(DataBaseDriverConstants.MYSQL.getUrl("255.255.255.255", "test_db"), "jdbc:mysql://255.255.255.255/test_db");
		assertEquals(DataBaseDriverConstants.POSTGRESSQL.getUrl("255.255.255.255", "test_db"), "jdbc:postgresql://255.255.255.255/test_db");
	}
	
	@Test
	public void getDriverClass() {
		// ==============================正常系==============================
		// 正常に設定されているドライバークラス名が返却されること。
		assertEquals(DataBaseDriverConstants.ORACLE.getDriverClass()     , "oracle.jdbc.driver.OracleDriver");
		assertEquals(DataBaseDriverConstants.MYSQL.getDriverClass()      , "com.mysql.jdbc.Driver");
		assertEquals(DataBaseDriverConstants.POSTGRESSQL.getDriverClass(), "org.postgresql.Driver");
	}
	
	@Test
	public void getDataStoreKind() {
		// ==============================正常系==============================
		// 正常に設定されているデータストア種別が返却されること。
		assertEquals(DataBaseDriverConstants.ORACLE.getDataStoreKind()     , DataStoreKind.ORACLE);
		assertEquals(DataBaseDriverConstants.MYSQL.getDataStoreKind()      , DataStoreKind.MYSQL);
		assertEquals(DataBaseDriverConstants.POSTGRESSQL.getDataStoreKind(), DataStoreKind.POSTGRESQL);
	}
}
