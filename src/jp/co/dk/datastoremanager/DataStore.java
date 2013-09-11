package jp.co.dk.datastoremanager;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * DataStoreは、単一のデータストアを表すクラスが実装するインターフェースです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public interface DataStore {
	
	/**
	 * このデータストアのトランザクションを開始します。
	 * @throws DataStoreManagerException トランザクション開始に失敗した場合
	 */
	public void startTransaction() throws DataStoreManagerException;
	
	/**
	 * このデータストアから指定のデータアクセスオブジェクトを取得します。
	 * 
	 * @param daoConstants 取得対象のデータアクセスオブジェクト定数
	 * @return データアクセスオブジェクト
	 * @throws DataStoreManagerException データアクセスオブジェクトの生成に失敗した場合
	 */
	public DataAccessObject getDataAccessObject(DaoConstants daoConstants) throws DataStoreManagerException;
	
	/**
	 * このデータストアのトランザクションを終了します。
	 * @throws DataStoreManagerException トランザクション終了に失敗した場合
	 */
	public void finishTransaction() throws DataStoreManagerException;
	
	/**
	 * このデータストアのトランザクションが開始されているか判定する。
	 * 
	 * @return true=トランザクションが開始済み、false=トランザクションはまだ開始されていない
	 */
	public boolean isTransaction();
}
