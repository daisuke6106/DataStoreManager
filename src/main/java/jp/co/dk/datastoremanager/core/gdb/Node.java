package jp.co.dk.datastoremanager.core.gdb;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;

/**
 * Recordは、単一のレコードを表すオブジェクトが実装するインターフェースです。
 * 
 * @version 1.1
 * @author D.Kanno
 */
public interface Node {
	
	/**
	 * このレコードから指定のキー名称の文字列を取得します。
	 * 
	 * @param key キー名称
	 * @return 文字列
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public String getString(String key) throws DataStoreManagerException ;
	
	/**
	 * このレコードから指定のキー名称の数値をInt型で取得します。
	 * 
	 * @param key キー名称
	 * @return 数値(Int)
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public int getInt(String key) throws DataStoreManagerException ;
	
	/**
	 * このレコードから指定のキー名称の数値をBoolean型で取得します。
	 * 
	 * @param key キー名称
	 * @return 真偽値(Boolean)
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public boolean getBoolean(String key) throws DataStoreManagerException ;
	
}
