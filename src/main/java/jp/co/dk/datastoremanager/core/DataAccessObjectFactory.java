package jp.co.dk.datastoremanager.core;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;

/**
 * DataStoreObjectFactoryは、データアクセスオブジェクトを生成するデータアクセスオブジェクトファクトリクラスが実装するインターフェースです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public interface DataAccessObjectFactory {
	
	/**
	 * 指定のデータベースデータストアパラメータからデータアクセスオブジェクトを取得します。
	 * 
	 * @param dataStoreKind     データストア種別
	 * @return データアクセスオブジェクト
	 * @throws DataStoreManagerException データアクセスオブジェクトの生成に失敗した場合
	 */
	public DataAccessObject getDataAccessObject(DataStoreKind dataStoreKind, DataStore dataStore) throws DataStoreManagerException;
}
