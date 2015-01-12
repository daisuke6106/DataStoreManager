package jp.co.dk.datastoremanager;

import java.util.HashMap;
import java.util.Map;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;

import static jp.co.dk.datastoremanager.message.DataStoreManagerMessage.*;

/**
 * DataStoreManagerは、Oracle、Mysql、PostgreSqlなどのデータストアに対する管理を行うクラスです。<p/>
 * <br/>
 * このクラスは指定のデータストアへのトランザクション制御、各種テーブルへのDAOの取得などデータストアへの操作を管理する制御クラスとなります。<br/>
 * <br/>
 * 対応しているデータストアは以下の 通りとなります。<br/>
 * 
 * ===RDB===<br/>
 * ・Oracle<br/>
 * ・MySql<br/>
 * ・PostgreSql<br/>
 * <br/>
 * ===ファイル===<br/>
 * ・csvファイル<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class DataStoreManager {
	
	/** デフォルトのデータストア */
	protected DataStore defaultDataStore;
	
	/** 各種データストア */
	protected Map<String, DataStore> dataStores = new HashMap<String, DataStore>();
	
	/** データストアプロパティ */
	protected DataStoreManagerProperty dataStoreManagerProperty;
	
	/**
	 * コンストラクタ<p/>
	 * 
	 * 
	 * @param dataStoreManagerProperty データストアマネージャプロパティ
	 * @throws DataStoreManagerException インスタンスの生成に失敗した場合
	 */
	public DataStoreManager(DataStoreManagerProperty dataStoreManagerProperty) throws DataStoreManagerException{
		if (dataStoreManagerProperty == null) throw new DataStoreManagerException(PROPERTY_IS_NOT_SET);
		this.dataStoreManagerProperty               = dataStoreManagerProperty;
		this.defaultDataStore                       = dataStoreManagerProperty.getDefaultDataStoreParameter().createDataStore();
		Map<String, DataStoreParameter> parameterMap = dataStoreManagerProperty.getDataStoreParameters();
		for (String name : parameterMap.keySet()) {
			this.dataStores.put(name, parameterMap.get(name).createDataStore());
		}
	}
	
	/**
	 * このデータストア管理クラスが管理しているすべてのデータストアに対してトランザクションを開始します。<p/>
	 * トランザクション開始処理に失敗した場合、例外を送出します。
	 * 
	 * @throws DataStoreManagerException トランザクション開始に失敗した場合
	 */
	public void startTrunsaction() throws DataStoreManagerException {
		this.defaultDataStore.startTransaction();
		for (DataStore dataStore : this.dataStores.values()) {
			dataStore.startTransaction();
		}
	}
	
	/**
	 * 指定のDAO定義クラスオブジェクトを元にデータアクセスオブジェクトを生成し、返却します。
	 * @param daoConstants DAO定義クラス
	 * @return データアクセスオブジェクトのインスタンス
	 * @throws DataStoreManagerException データアクセスオブジェクトの生成、または取得に失敗した場合
	 */
	public DataAccessObject getDataAccessObject(DaoConstants daoConstants) throws DataStoreManagerException {
		DataStore dataStore = this.dataStores.get(daoConstants.getName());
		if (dataStore != null) { 
			return dataStore.getDataAccessObject(daoConstants);
		}
		return this.defaultDataStore.getDataAccessObject(daoConstants);
	}
	
	/**
	 * このトランザクションに対してコミットを実行します。
	 * 
	 * @throws DataStoreManagerException コミットに失敗した場合
	 */
	public void commit() throws DataStoreManagerException {
		this.defaultDataStore.commit();
		for (String name : dataStores.keySet()) {
			this.dataStores.get(name).commit();
		}
	}
	
	/**
	 * このトランザクションに対してロールバックを実行します。
	 * 
	 * @throws DataStoreManagerException ロールバックに失敗した場合
	 */
	public void rollback() throws DataStoreManagerException {
		this.defaultDataStore.rollback();
		for (String name : dataStores.keySet()) {
			this.dataStores.get(name).rollback();
		}
	}
	
	/**
	 * このデータストアにてエラーが発生したかを判定します。
	 * 
	 * @return 判定結果（true=エラーが発生した、false=エラーは発生していない）
	 */
	public boolean hasError() {
		if (this.defaultDataStore.hasError()) return true;
		for (String name : dataStores.keySet()) {
			if (this.dataStores.get(name).hasError()) return true;
		}
		return false;
	}
	
	/**
	 * このデータストア管理クラスが管理しているすべてのデータストアに対してトランザクションを終了します。<p/>
	 * トランザクション終了処理に失敗した場合、例外を送出します。<br/>
	 * 
	 * @throws DataStoreManagerException トランザクション終了に失敗した場合
	 */
	public void finishTrunsaction() throws DataStoreManagerException {
		if (this.hasError()) {
			this.defaultDataStore.rollback();
			for (String name : dataStores.keySet()) {
				this.dataStores.get(name).rollback();
			}
		} else {
			this.defaultDataStore.commit();
			for (String name : dataStores.keySet()) {
				this.dataStores.get(name).commit();
			}
		}
		this.defaultDataStore.finishTransaction();
		for (DataStore dataStore : dataStores.values()) {
			dataStore.finishTransaction();
		}
	}
}