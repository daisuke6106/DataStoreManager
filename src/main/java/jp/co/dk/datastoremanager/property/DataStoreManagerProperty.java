package jp.co.dk.datastoremanager.property;

import static jp.co.dk.datastoremanager.message.DataStoreManagerMessage.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.co.dk.datastoremanager.DataStoreKind;
import jp.co.dk.datastoremanager.DataStoreParameter;
import jp.co.dk.datastoremanager.database.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.property.PropertiesFile;
import jp.co.dk.property.exception.PropertyException;

/**
 * データストアに関するプロパティを定義するクラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class DataStoreManagerProperty extends PropertiesFile {
	
	/**
	 * コンストラクタ<p>
	 * デフォルトのプロパティファイルを元にプロパティファイルオブジェクトのインスタンスを生成する。<br/>
	 * デフォルトのプロパティファイルは"properties/DataStoreManager.properties"を参照します。<br/>
	 * <br/>
	 * プロパティファイルが存在しない場合、例外が発生します。
	 * 
	 * @throws PropertyException プロパティファイルオブジェクトのインスタンス作成に失敗した場合
	 */
	public DataStoreManagerProperty () throws PropertyException {
		this("DataStoreManager.properties");
	}
	
	/**
	 * コンストラクタ<p>
	 * 指定のプロパティファイルを元にプロパティファイルオブジェクトのインスタンスを生成する。<br/>
	 * <br/>
	 * プロパティファイルが存在しない場合、例外が発生します。
	 * 
	 * @param file プロパティファイル
	 * @throws PropertyException プロパティファイルオブジェクトのインスタンス作成に失敗した場合
	 */
	public DataStoreManagerProperty (String file) throws PropertyException {
		super(file);
	}
	
	/**
	 * このプロパティファイルからデフォルトのデータストアパラメータオブジェクトを生成し、返却する。<p/>
	 * このプロパティから"datastore.type"の値を取得し、その設定値に紐づくデータストアへの設定パラメータを収集し、データストアパラメータインスタンスを作成、返却します。<br/>
	 * <br/>
	 * たとえば、<br/>
	 * datastore.type=oracleの場合、<br/>
	 * <br/>
	 * ・"datastore.oracle.url"<br/>
	 * ・"datastore.oracle.sid"<br/>
	 * ・"datastore.oracle.user"<br/>
	 * ・"datastore.oracle.password"<br/>
	 * <br/>
	 * の値を収集し、データストアパラメータへ設定し、そのインスタンスを返却します。<br/>
	 * <br/>
	 * プロパティファイルに値が設定されていないなど、プロパティファイルからデータストアパラメータクラスの生成に失敗した場合、例外が送出されます。<br/>
	 * 
	 * @return データストアパラメータ
	 * @throws DataStoreManagerException データストアパラメータの生成に失敗した場合
	 */
	public DataStoreParameter getDefaultDataStoreParameter() throws DataStoreManagerException {
		DataStoreKind dataStoreKind = DataStoreKind.convert(this.getString("datastore.type"));
		switch (dataStoreKind) {
			case ORACLE:
				String oracleurl      = this.getString("datastore.oracle.url"     );
				String oraclesid      = this.getString("datastore.oracle.sid"     );
				String oracleuser     = this.getString("datastore.oracle.user"    );
				String oraclepassword = this.getString("datastore.oracle.password");
				return new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, oracleurl, oraclesid, oracleuser, oraclepassword);
				
			case MYSQL:
				String mysqlurl      = this.getString("datastore.mysql.url"     );
				String mysqlsid      = this.getString("datastore.mysql.sid"     );
				String mysqluser     = this.getString("datastore.mysql.user"    );
				String mysqlpassword = this.getString("datastore.mysql.password");
				return new DataBaseAccessParameter(DataStoreKind.MYSQL, DataBaseDriverConstants.MYSQL, mysqlurl, mysqlsid, mysqluser, mysqlpassword);
				
			case POSTGRESQL:
				String postgresqlurl      = this.getString("datastore.postgresql.url"     );
				String postgresqlsid      = this.getString("datastore.postgresql.sid"     );
				String postgresqluser     = this.getString("datastore.postgresql.user"    );
				String postgresqlpassword = this.getString("datastore.postgresql.password");
				return new DataBaseAccessParameter(DataStoreKind.POSTGRESQL, DataBaseDriverConstants.POSTGRESSQL, postgresqlurl, postgresqlsid, postgresqluser, postgresqlpassword);
				
			default :
				throw new DataStoreManagerException(NO_SUPPORT_DATA_STORE_KIND, dataStoreKind.toString());
		}
	}
	
	/**
	 * このプロパティファイルから
	 * 
	 * @return 名称をキーとデータストアパラメータインスタンスが紐づくマップオブジェクト
	 * @throws DataStoreManagerException　データストアパラメータの生成に失敗した場合
	 */
	public Map<String, DataStoreParameter> getDataStoreParameters() throws DataStoreManagerException {
		Map<String, DataStoreParameter> configurationMap = new HashMap<String, DataStoreParameter>();
		List<String> nameList = this.getNameList("datastore.type");
		for (String name : nameList) {
			configurationMap.put(name, this.getDataStoreParameter(name));
		}
		return configurationMap;
	}
	

	/**
	 * このプロパティファイルから指定の名称のデータストアパラメータオブジェクトを生成し、返却する。<p/>
	 * 引数に"USERS"が設定されていた場合、このプロパティから"datastore.type.USERS"の値を取得し、その設定値に紐づくデータストアへの設定パラメータを収集し、データストアパラメータインスタンスを作成、返却します。<br/>
	 * <br/>
	 * たとえば、<br/>
	 * datastore.type.USERS=oracleの場合、<br/>
	 * <br/>
	 * ・"datastore.oracle.url.USERS"<br/>
	 * ・"datastore.oracle.sid.USERS"<br/>
	 * ・"datastore.oracle.user.USERS"<br/>
	 * ・"datastore.oracle.password.USERS"<br/>
	 * <br/>
	 * の値を収集し、データストアパラメータへ設定し、そのインスタンスを返却します。<br/>
	 * <br/>
	 * プロパティファイルに値が設定されていないなど、プロパティファイルからデータストアパラメータクラスの生成に失敗した場合、例外が送出されます。<br/>
	 * 
	 * @return データストアパラメータ
	 * @throws DataStoreManagerException データストアパラメータの生成に失敗した場合
	 */
	protected DataStoreParameter getDataStoreParameter(String name) throws DataStoreManagerException {
		DataStoreKind dataStoreKind = DataStoreKind.convert(this.getString("datastore.type", name));
		switch (dataStoreKind) {
			case ORACLE:
				String oracleurl      = this.getStringWithName("datastore.oracle.url"     , name);
				String oraclesid      = this.getStringWithName("datastore.oracle.sid"     , name);
				String oracleuser     = this.getStringWithName("datastore.oracle.user"    , name);
				String oraclepassword = this.getStringWithName("datastore.oracle.password", name);
				return new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, oracleurl, oraclesid, oracleuser, oraclepassword);
				
			case MYSQL:
				String mysqlurl      = this.getStringWithName("datastore.mysql.url"     , name);
				String mysqlsid      = this.getStringWithName("datastore.mysql.sid"     , name);
				String mysqluser     = this.getStringWithName("datastore.mysql.user"    , name);
				String mysqlpassword = this.getStringWithName("datastore.mysql.password", name);
				return new DataBaseAccessParameter(DataStoreKind.MYSQL, DataBaseDriverConstants.MYSQL, mysqlurl, mysqlsid, mysqluser, mysqlpassword);
				
			case POSTGRESQL:
				String postgresqlurl      = this.getStringWithName("datastore.postgresql.url"     , name);
				String postgresqlsid      = this.getStringWithName("datastore.postgresql.sid"     , name);
				String postgresqluser     = this.getStringWithName("datastore.postgresql.user"    , name);
				String postgresqlpassword = this.getStringWithName("datastore.postgresql.password", name);
				return new DataBaseAccessParameter(DataStoreKind.POSTGRESQL, DataBaseDriverConstants.POSTGRESSQL, postgresqlurl, postgresqlsid, postgresqluser, postgresqlpassword);
				
			default :
				throw new DataStoreManagerException(NO_SUPPORT_DATA_STORE_KIND, dataStoreKind.toString());
		}
	}
	
	/**
	 * 指定のプロパティキーに紐づく名称を取得します。<p/>
	 * プロパティファイルに以下のような定義がなされていた場合、以下のようなリストを返却します。<br/>
	 * <br/>
	 * 引数に"propertykey"<br/>
	 * <br/>
	 * propertykey=aaa<br/>
	 * propertykey.TEST01=bbb<br/>
	 * propertykey.TEST02=ccc<br/>
	 * <br/>
	 * が定義されていた場合、返却のリストには<br/>
	 * TEST01,TEST02を保持したリストを返却します。<br/>
	 * <br/>
	 * 引数にnullが設定された、存在しないプロパティキーを指定された場合、ドットのあとに続く名称が存在しない場合、空のリストを返却します。
	 * 
	 * @param key プロパティキー
	 * @return プロパティキーに続く名称の一覧
	 */
	protected List<String> getNameList(String key) {
		List<String> list = new ArrayList<String>();
		if (key == null || key.equals("")) return list; 
		Iterator<String> keys = getKeys(key).iterator();
		while(keys.hasNext()) {
			String getKey = keys.next();
			String replaceKey = getKey.replaceAll(key+".", "");
			if (!key.equals(replaceKey) && !replaceKey.equals("")) list.add(replaceKey); 
		}
		return list;
	}
	
	/**
	 * このプロパティキーを指定の名称で補完した値を返却します。<p/>
	 * 例えば、"datastore.type"、nameの値が"USERS"とした場合、<br/>
	 * プロパティファイルから"datastore.type.USERS"のキーに設定されている値を取得して返却します。<br/>
	 * <br/>
	 * 取得出来なかった場合、nullが取得される。
	 * 
	 * @param key プロパティキー
	 * @param name 名称
	 * @return プロパティ設定値
	 */
	protected String getStringWithName(String key, String name) {
		if (name == null) return null;
		String value = this.getString(new StringBuilder(key).append('.').append(name).toString());
		if (value == null ) return null;
		return value;
	}
	
}
