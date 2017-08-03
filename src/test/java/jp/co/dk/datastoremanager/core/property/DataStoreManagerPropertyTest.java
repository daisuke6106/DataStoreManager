package jp.co.dk.datastoremanager.core.property;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import jp.co.dk.datastoremanager.core.DaoConstants;
import jp.co.dk.datastoremanager.core.DataAccessObjectFactory;
import jp.co.dk.datastoremanager.core.DataStoreKind;
import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.DataStoreParameter;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage;
import jp.co.dk.datastoremanager.core.property.DataStoreManagerProperty;
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;

import org.junit.Test;

public class DataStoreManagerPropertyTest extends DataStoreManagerTestFoundation{
	
	@Test
	public void getDefaultDataStoreParameter() {
		// ===============正常系===============
		// ORACLEの設定値を正常に取得できること
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/success/oracle.properties");
			DataStoreParameter dataStoreParameter = dataStoreManagerProperty.getDefaultDataStoreParameter();
			assertTrue  (dataStoreParameter instanceof DataBaseAccessParameter);
			DataBaseAccessParameter dataBaseAccessParameter = (DataBaseAccessParameter)dataStoreParameter;
			assertEquals(dataStoreParameter.getDataStoreKind(), DataStoreKind.ORACLE);
			assertEquals(dataBaseAccessParameter.getUrl()     , "default_db_server");
			assertEquals(dataBaseAccessParameter.getUser()    , "default_user");
			assertEquals(dataBaseAccessParameter.getPassword(), "default_password");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// MYSQLの設定値を正常に取得できること
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/success/mysql.properties");
			DataStoreParameter dataStoreParameter = dataStoreManagerProperty.getDefaultDataStoreParameter();
			assertTrue  (dataStoreParameter instanceof DataBaseAccessParameter);
			DataBaseAccessParameter dataBaseAccessParameter = (DataBaseAccessParameter)dataStoreParameter;
			assertEquals(dataBaseAccessParameter.getUrl()     , "default_db_server");
			assertEquals(dataBaseAccessParameter.getUser()    , "default_user");
			assertEquals(dataBaseAccessParameter.getPassword(), "default_password");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// POSTGRESQLの設定値を正常に取得できること
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/success/postgresql.properties");
			DataStoreParameter dataStoreParameter = dataStoreManagerProperty.getDefaultDataStoreParameter();
			assertTrue  (dataStoreParameter instanceof DataBaseAccessParameter);
			DataBaseAccessParameter dataBaseAccessParameter = (DataBaseAccessParameter)dataStoreParameter;
			assertEquals(dataStoreParameter.getDataStoreKind(), DataStoreKind.POSTGRESQL);
			assertEquals(dataBaseAccessParameter.getUrl()     , "default_db_server");
			assertEquals(dataBaseAccessParameter.getUser()    , "default_user");
			assertEquals(dataBaseAccessParameter.getPassword(), "default_password");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// ===============異常系===============
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/error/datastore_type_not_set.properties");
			dataStoreManagerProperty.getDefaultDataStoreParameter();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.DATASTORE_KIND_VALUE_IS_FAILE); 
		}
		

		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/error/url_not_set.properties");
			dataStoreManagerProperty.getDefaultDataStoreParameter();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.URL_IS_NOT_SET); 
		}
		
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/error/sid_not_set.properties");
			dataStoreManagerProperty.getDefaultDataStoreParameter();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.SID_IS_NOT_SET); 
		}
		
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/error/user_not_set.properties");
			dataStoreManagerProperty.getDefaultDataStoreParameter();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.USER_IS_NOT_SET); 
		}
		

		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/error/password_not_set.properties");
			dataStoreManagerProperty.getDefaultDataStoreParameter();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.PASSWORD_IS_NOT_SET); 
		}
	}
	
	@Test
	public void getDataStoreParameters() {
		// ===============正常系===============
		// デフォルトのみしか定義されていない場合、空のマップが返却されること。
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/success/defaultonly.properties");
			Map<String, DataStoreParameter> dataStoreParameterMap = dataStoreManagerProperty.getDataStoreParameters();
			assertEquals(dataStoreParameterMap.size(), 0);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// ORACLEの設定値を正常に取得できること
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/success/oracle.properties");
			Map<String, DataStoreParameter> dataStoreParameterMap = dataStoreManagerProperty.getDataStoreParameters();
			assertEquals(dataStoreParameterMap.size(), 1);
			DataStoreParameter dataStoreParameter = dataStoreParameterMap.get("USERS");
			assertTrue  (dataStoreParameter instanceof DataBaseAccessParameter);
			DataBaseAccessParameter dataBaseAccessParameter = (DataBaseAccessParameter)dataStoreParameter;
			assertEquals(dataStoreParameter.getDataStoreKind(), DataStoreKind.ORACLE);
			assertEquals(dataBaseAccessParameter.getUrl()     , "users_db_server");
			assertEquals(dataBaseAccessParameter.getUser()    , "users_user");
			assertEquals(dataBaseAccessParameter.getPassword(), "users_password");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// MYSQLの設定値を正常に取得できること
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/success/mysql.properties");
			Map<String, DataStoreParameter> dataStoreParameterMap = dataStoreManagerProperty.getDataStoreParameters();
			assertEquals(dataStoreParameterMap.size(), 1);
			DataStoreParameter dataStoreParameter = dataStoreParameterMap.get("USERS");
			assertTrue  (dataStoreParameter instanceof DataBaseAccessParameter);
			DataBaseAccessParameter dataBaseAccessParameter = (DataBaseAccessParameter)dataStoreParameter;
			assertEquals(dataStoreParameter.getDataStoreKind(), DataStoreKind.MYSQL);
			assertEquals(dataBaseAccessParameter.getUrl()     , "users_db_server");
			assertEquals(dataBaseAccessParameter.getUser()    , "users_user");
			assertEquals(dataBaseAccessParameter.getPassword(), "users_password");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// POSTGRESQLの設定値を正常に取得できること
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/success/postgresql.properties");
			Map<String, DataStoreParameter> dataStoreParameterMap = dataStoreManagerProperty.getDataStoreParameters();
			assertEquals(dataStoreParameterMap.size(), 1);
			DataStoreParameter dataStoreParameter = dataStoreParameterMap.get("USERS");
			assertTrue  (dataStoreParameter instanceof DataBaseAccessParameter);
			DataBaseAccessParameter dataBaseAccessParameter = (DataBaseAccessParameter)dataStoreParameter;
			assertEquals(dataStoreParameter.getDataStoreKind(), DataStoreKind.POSTGRESQL);
			assertEquals(dataBaseAccessParameter.getUrl()     , "users_db_server");
			assertEquals(dataBaseAccessParameter.getUser()    , "users_user");
			assertEquals(dataBaseAccessParameter.getPassword(), "users_password");
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// ===============異常系===============
		// データストア種別が設定されていない場合、例外が発生すること。
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/error/datastore_type_not_set.properties");
			dataStoreManagerProperty.getDataStoreParameters();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.DATASTORE_KIND_VALUE_IS_FAILE); 
		}
		
		
		// URLが設定されていない場合、例外が発生すること。
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/error/url_not_set.properties");
			dataStoreManagerProperty.getDataStoreParameters();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.URL_IS_NOT_SET); 
		}
		
		// SIDが設定されていない場合、例外が発生すること。
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/error/sid_not_set.properties");
			dataStoreManagerProperty.getDataStoreParameters();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.SID_IS_NOT_SET); 
		}
		
		// ユーザが設定されていない場合、例外が発生すること。
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/error/user_not_set.properties");
			dataStoreManagerProperty.getDataStoreParameters();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.USER_IS_NOT_SET); 
		}
		
		// パスワードが設定されていない場合、例外が発生すること。
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/error/password_not_set.properties");
			dataStoreManagerProperty.getDataStoreParameters();
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.PASSWORD_IS_NOT_SET); 
		}
		
	}
	
	@Test
	public void getNameList() {
		DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/success/getnamelist.properties");
		
		// 引数に指定された名称がnullの場合、空のリストを返却すること。
		assertEquals(dataStoreManagerProperty.getNameList(null).size(), 0);
		
		// 引数に指定された名称がプロパティに存在しないキーの場合、空のリストを返却すること。
		assertEquals(dataStoreManagerProperty.getNameList("property_has_not_key").size(), 0);
		
		// 引数に指定されたキーに続く名称がプロパティファイルに存在しなかった場合、空のリストを返却すること。
		assertEquals(dataStoreManagerProperty.getNameList("getnamelist_non").size(), 0);
		
		// 引数に指定されたキーに続く名称がプロパティファイルに存在しなかった場合、空のリストを返却すること。
		List<String> result01 = dataStoreManagerProperty.getNameList("getnamelist_single");
		assertEquals(result01.size(), 1);
		assertEquals(result01.get(0), "NAME01");
		
		// 引数に指定されたキーに続く名称がプロパティファイルに存在しなかった場合、空のリストを返却すること。
		List<String> result02 = dataStoreManagerProperty.getNameList("getnamelist_multi");
		assertEquals(result02.size(), 2);
		assertEquals(result02.get(0), "NAME01");
		assertEquals(result02.get(1), "NAME02");
	}
	
	@Test
	public void getStringWithName() {
		DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty("datastoremanager/success/getstringwithname.properties");
		// プロパティキー、名称ともにnullの場合、nullが返却されること。
		assertNull(dataStoreManagerProperty.getStringWithName(null, null));
		// 名称のみnullの場合、nullが返却されること。
		assertNull(dataStoreManagerProperty.getStringWithName("getstringwithname", null));
		// 名称にプロパティファイルに存在しない名称が指定された場合、nullが返却されること。
		assertNull(dataStoreManagerProperty.getStringWithName("getstringwithname", "NAME02"));
		// 名称にプロパティファイルに存在する名称が指定された場合、値が返却されること。
		assertEquals(dataStoreManagerProperty.getStringWithName("getstringwithname", "NAME01"), "name01");
		
	}
}

enum FakeDaiConstants implements DaoConstants{
	
	/** サンプルユーザテーブル */
	USERS("USERS", null),
	
	/** サンプルユーザテーブル */
	NO_USERS("NO_USERS", null),
	;
	
	private String name;
	
	private DataAccessObjectFactory factory;
	
	private FakeDaiConstants(String name, DataAccessObjectFactory factory) {
		this.name    = name;
		this.factory = factory;
	}
	
	@Override
	public DataAccessObjectFactory getDataAccessObjectFactory() {
		return this.factory;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
}