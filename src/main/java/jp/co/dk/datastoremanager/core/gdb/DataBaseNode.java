package jp.co.dk.datastoremanager.core.gdb;

import java.sql.ResultSet;
import java.sql.SQLException;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

/**
 * DataBaseRecordは、単一のデータベースレコードを表すオブジェクトが実装する抽象クラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class DataBaseNode implements Node {
	
	/** レコードオブジェクト */
	protected ResultSet resultSet;
	
	/**
	 * コンストラクタ<p/>
	 * 指定のレコードオブジェクトを基にデータベースレコードオブジェクトを生成します。
	 * 
	 * @param resultSet
	 */
	DataBaseNode(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	
	/**
	 * このレコードから指定のカラム名の文字列を取得します。
	 * 
	 * @param column カラム名
	 * @return 文字列
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public String getString(String key) throws DataStoreManagerException {
		try {
			return this.resultSet.getString(key);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_NAME, key);
		}
	}
	
	/**
	 * このレコードから指定のカラム名の数値を取得します。
	 * 
	 * @param column カラム名
	 * @return 数値
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public int getInt(String key) throws DataStoreManagerException {
		try {
			return this.resultSet.getInt(key);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_NAME, key);
		}
	}

	@Override
	public boolean getBoolean(String key) throws DataStoreManagerException {
		try {
			return this.resultSet.getBoolean(key);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_NAME, key);
		}
	}
	
}
