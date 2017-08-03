package jp.co.dk.datastoremanager.core.gdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.core.DataAccessObject;
import jp.co.dk.datastoremanager.core.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.core.DataStore;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.gdb.DataBaseNode;
import jp.co.dk.datastoremanager.core.gdb.DataConvertable;
import jp.co.dk.logger.Logger;
import jp.co.dk.logger.LoggerFactory;
import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

/**
 * AbstractDataBaseAccessObjectは、データベースにアクセスを行うデータアクセスオブジェクトが実装する基底クラスです。<p/>
 * 
 * このクラスはデータストアオブジェクトを内包し、そのデータストアに対してselect、insert、update、deleteを行います。<br/>
 * 例えば、"ORACLE"の"USERS"テーブルにアクセスする場合、"USERS"テーブルにアクセスするクラスを作成し、本クラスを継承し、その中でSQLオブジェクトを作成、<br/>
 * このメソッドのアクセスメソッドを使用することでSQLを実行、結果の取得をおこなうことができる。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class AbstractDataBaseAccessObject implements DataAccessObject{
	
	/** データストアオブジェクト */
	protected DataBaseDataStore dataStore;
	
	/** ロガーインスタンス */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * コンストラクタ<p/>
	 * 以下のパラメータを元に、データベースアクセスオブジェクトを生成、トランザクションを開始する。<br/>
	 * <br/>
	 * パラメータが不足している場合、またはトランザクション開始に失敗した場合、例外が送出される。<br/>
	 * <br/>
	 * ・データベースドライバー<br/>
	 * ・データベース接続先URLまたは、IPアドレス<br/>
	 * 
	 * @param driver   データベースドライバー
	 * @param url      データベース接続先URLまたは、IPアドレス
	 * @throws DataStoreManagerException パラメータが不足している場合、またはトランザクション開始に失敗した場合
	 */
	protected AbstractDataBaseAccessObject(DataBaseDriverConstants driver, String url) throws DataStoreManagerException {
		this(new DataBaseAccessParameter(driver.getDataStoreKind(), driver, url));
	}
	
	/**
	 * コンストラクタ<p/>
	 * 以下のパラメータを元に、データベースアクセスオブジェクトを生成、トランザクションを開始する。<br/>
	 * <br/>
	 * パラメータが不足している場合、またはトランザクション開始に失敗した場合、例外が送出される。<br/>
	 * <br/>
	 * ・データベースドライバー<br/>
	 * ・データベース接続先URLまたは、IPアドレス<br/>
	 * ・データベース接続先ユーザ<br/>
	 * ・データベース接続先パスワード<br/>
	 * 
	 * @param driver   データベースドライバー
	 * @param url      データベース接続先URLまたは、IPアドレス
	 * @param user     データベース接続先ユーザ
	 * @param password データベース接続先パスワード
	 * @throws DataStoreManagerException パラメータが不足している場合、またはトランザクション開始に失敗した場合
	 */
	protected AbstractDataBaseAccessObject(DataBaseDriverConstants driver, String url, String user, String password) throws DataStoreManagerException {
		this(new DataBaseAccessParameter(driver.getDataStoreKind(), driver, url, user, password));
	}
	
	/**
	 * コンストラクタ<p/>
	 * 指定のデータベースアクセスパラメータを元に、データベースアクセスオブジェクトを生成、トランザクションを開始する。<br/>
	 * <br/>
	 * パラメータが不足している場合、またはトランザクション開始に失敗した場合、例外が送出される。<br/>
	 * 
	 * @param dataBaseAccessParameter データアクセスパラメータ
	 * @throws DataStoreManagerException パラメータが不足している場合、またはトランザクション開始に失敗した場合
	 */
	protected AbstractDataBaseAccessObject(DataBaseAccessParameter dataBaseAccessParameter) throws DataStoreManagerException {
		this.logger.constractor(this.getClass(), dataBaseAccessParameter);
		if (dataBaseAccessParameter == null) throw new DataStoreManagerException(DATA_ACCESS_OBJECT_IS_NOT_SET);
		this.dataStore = new DataBaseDataStore(dataBaseAccessParameter);
		this.dataStore.startTransaction();
	}
	
	/**
	 * コンストラクタ<p/>
	 * 指定のデータストアオブジェクトを元に、データベースアクセスオブジェクトを生成する。<br/>
	 * データストアがデータベース用データストアでなかった場合、例外が送出される。<br/>
	 * <br/>
	 * @param dataStore データストアオブジェクト
	 * @throws DataStoreManagerException データストアがデータベース用データストアでなかった場合
	 */
	protected AbstractDataBaseAccessObject(DataStore dataStore) throws DataStoreManagerException {
		this.logger.constractor(this.getClass(), dataStore);
		if (!(dataStore instanceof DataBaseDataStore)) throw new DataStoreManagerException(FAILE_TO_CAST_DATA_STORE_OBJECT); 
		this.dataStore = (DataBaseDataStore)dataStore;
	}
	
	/**
	 * 指定のCypherを実行し、レコードを作成する。<p/>
	 * レコード追加に失敗した場合、例外を送出する。
	 * 
	 * @param cypher 実行対象のCypherオブジェクト
	 * @throws DataStoreManagerException レコード追加に失敗した場合
	 */
	public void execute(Cypher cypher) throws DataStoreManagerException {
		this.dataStore.execute(cypher);
	}
	
	/**
	 * 指定のCypherを実行し、レコードを作成する。<p/>
	 * レコード追加に失敗した場合、例外を送出する。
	 * 
	 * @param cypher 実行対象のCypherオブジェクト
	 * @throws DataStoreManagerException レコード追加に失敗した場合
	 */
	public <E extends DataConvertable> List<E> executeWithRetuen(Cypher cypher, E convertable) throws DataStoreManagerException {
		List<E> resultList = new ArrayList<E>();
		ResultSet result = this.dataStore.execute(cypher);
		try {
			while (result.next()) {
				resultList.add((E)convertable.convert(new DataBaseNode(result)));
			}
			result.close();
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_RECORD_IS_FAILE, cypher.toString(), e);
		}
		return resultList;
	}
}
