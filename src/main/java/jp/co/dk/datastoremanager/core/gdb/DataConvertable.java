package jp.co.dk.datastoremanager.core.gdb;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.METHOD_TO_CONVERT_A_RESULT_IS_UNDEFINED;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;

/**
 * DataConvertableは、レコードの変換を行うクラスが実装するインターフェースです。<p/>
 * データベースから取得したレコード、CSVファイルではCSVファイル内の１行から値を取り出し、自身のクラス内に設定するクラスが実装するインターフェースです。
 * 
 * @version 1.1
 * @author D.Kanno
 */
public interface DataConvertable extends jp.co.dk.datastoremanager.core.DataConvertable {
	
	/**
	 * データベースから取得した単一のレコードを自身のクラスへ設定します。
	 * 
	 * @param dataBaseRecord 単一のデータベースレコード
	 * @throws DataStoreManagerException 変換に失敗した場合
	 */
	public default DataConvertable convert(DataBaseNode dataBaseRecord) throws DataStoreManagerException {
		throw new DataStoreManagerException(METHOD_TO_CONVERT_A_RESULT_IS_UNDEFINED);
	}
	
}
