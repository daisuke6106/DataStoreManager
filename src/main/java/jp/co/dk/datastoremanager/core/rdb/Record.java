package jp.co.dk.datastoremanager.core.rdb;

import java.sql.Timestamp;
import java.util.Date;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;

/**
 * Recordは、単一のレコードを表すオブジェクトが実装するインターフェースです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public interface Record {
	
	/**
	 * このレコードから指定のインデックスの文字列を取得します。
	 * 
	 * @param index インデックス
	 * @return 文字列
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public String getString(int index) throws DataStoreManagerException ;
	
	/**
	 * このレコードから指定のインデックスの数値をInt型で取得します。
	 * 
	 * @param index インデックス
	 * @return 数値(Int)
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public int getInt(int index) throws DataStoreManagerException ;
	
	/**
	 * このレコードから指定のインデックスの数値をLong型で取得します。
	 * 
	 * @param index インデックス
	 * @return 数値(long)
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public long getLong(int index) throws DataStoreManagerException ;
	
	/**
	 * このレコードから指定のインデックスの日付を取得します。
	 * 
	 * @param index インデックス
	 * @return 日付
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public Date getDate(int index) throws DataStoreManagerException ;
	
	/**
	 * このレコードから指定のインデックスのタイムスタンプを取得します。
	 * 
	 * @param index インデックス
	 * @return 日付
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public Timestamp getTimestamp(int index) throws DataStoreManagerException ;
	
	/**
	 * このレコードから指定のインデックスのバイト配列を取得します。
	 * 
	 * @param index インデックス
	 * @return バイト配列
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public byte[] getBytes(int index) throws DataStoreManagerException ;
	
	/**
	 * このレコードから指定のインデックスのオブジェクトを取得します。
	 * 
	 * @param index インデックス
	 * @return オブジェクト
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public Object getObject(int index) throws DataStoreManagerException ;
}
