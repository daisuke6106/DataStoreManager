package jp.co.dk.datastoremanager.rdb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import static jp.co.dk.datastoremanager.message.DataStoreManagerMessage.*;

/**
 * DataBaseRecordは、単一のデータベースレコードを表すオブジェクトが実装する抽象クラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class DataBaseRecord implements Record {
	
	/** カラムメタデータ一覧 */
	protected List<ColumnMetaData> columnMetaData;
	
	/** レコードオブジェクト */
	protected ResultSet resultSet;
	
	/**
	 * コンストラクタ<p/>
	 * 指定のレコードオブジェクトを基にデータベースレコードオブジェクトを生成します。
	 * 
	 * @param resultSet
	 */
	protected DataBaseRecord(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	
	boolean next() throws SQLException {
		return this.resultSet.next();
	}
	
	void close() throws SQLException {
		this.resultSet.close();
	}
	
	/**
	 * 現在参照している行番号を取得します。
	 * @return 現在参照している行番号
	 * @throws DataStoreManagerException 現在参照している行番号の取得に失敗した場合
	 */
	public int getRowIndex() throws DataStoreManagerException {
		try {
			return this.resultSet.getRow();
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_GET_COLUMN_NAME);
		}
	}
	
	/**
	 * カラムのメタデータ一覧を返却します。
	 * @return カラムメタデータ一覧
	 * @throws DataStoreManagerException カラムメタデータの取得に失敗した場合
	 */
	public List<ColumnMetaData> getColumns() throws DataStoreManagerException {
		try{
			if (columnMetaData == null) {
				this.columnMetaData = new ArrayList<>();
				ResultSetMetaData metaData= this.resultSet.getMetaData();
				for (int i = 1; i <= metaData.getColumnCount(); i++) this.columnMetaData.add(this.createColumnMetaData(metaData, i));
			}
			return this.columnMetaData;
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_GET_COLUMN_NAME);
		}
	}
	
	protected abstract ColumnMetaData createColumnMetaData(ResultSetMetaData rs, int i) throws SQLException ;
	
	/**
	 * このレコードから指定のカラム名の文字列を取得します。
	 * 
	 * @param column カラム名
	 * @return 文字列
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public String getString(String column) throws DataStoreManagerException {
		try {
			return this.resultSet.getString(column);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_NAME, column);
		}
	}
	
	/**
	 * このレコードから指定のカラム名の数値を取得します。
	 * 
	 * @param column カラム名
	 * @return 数値
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public int getInt(String column) throws DataStoreManagerException {
		try {
			return this.resultSet.getInt(column);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_NAME, column);
		}
	}
	
	/**
	 * このレコードから指定のカラム名の数値を取得します。
	 * 
	 * @param column カラム名
	 * @return 数値
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public BigDecimal getBigDecimal(String column) throws DataStoreManagerException {
		try {
			return this.resultSet.getBigDecimal(column);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_NAME, column);
		}
	}
	
	/**
	 * このレコードから指定のカラム名の数値を取得します。
	 * 
	 * @param column カラム名
	 * @return 数値
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public long getLong(String column) throws DataStoreManagerException {
		try {
			return this.resultSet.getLong(column);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_NAME, column);
		}
	}
	
	/**
	 * このレコードから指定のカラム名の日付を取得します。
	 * 
	 * @param column カラム名
	 * @return 日付
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public java.util.Date getDate(String column) throws DataStoreManagerException {
		try {
			return this.resultSet.getDate(column);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_NAME, column);
		}
	}
	
	/**
	 * このレコードから指定のカラム名のタイムスタンプを取得します。
	 * 
	 * @param column カラム名
	 * @return タイムスタンプ
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public java.util.Date getTimestamp(String column) throws DataStoreManagerException {
		try {
			return this.resultSet.getTimestamp(column);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_NAME, column);
		}
	}
	
	/**
	 * このレコードから指定のカラム名のバイト配列を取得します。
	 * 
	 * @param column カラム名
	 * @return バイト配列
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public byte[] getBytes(String column) throws DataStoreManagerException {
		try {
			return this.resultSet.getBytes(column);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_NAME, column);
		}
	}
	
	/**
	 * このレコードから指定のカラム名のオブジェクトを取得します。
	 * 
	 * @param column カラム名
	 * @return オブジェクト
	 * @throws DataStoreManagerException 値の取得に失敗した場合
	 */
	public Object getObject(String column) throws DataStoreManagerException {
		byte[] bytes = null;
		try {
			bytes = this.resultSet.getBytes(column);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_NAME, column);
		}
		return this.convertBytesToObject(bytes);
	}
	
	@Override
	public String getString(int index) throws DataStoreManagerException {
		try {
			return this.resultSet.getString(index);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_INDEX, Integer.toString(index));
		}
	}
	
	@Override
	public int getInt(int index) throws DataStoreManagerException {
		try {
			return this.resultSet.getInt(index);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_INDEX, Integer.toString(index));
		}
	}
	
	@Override
	public long getLong(int index) throws DataStoreManagerException {
		try {
			return this.resultSet.getLong(index);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_INDEX, Integer.toString(index));
		}
	}
	
	@Override
	public java.util.Date getDate(int index) throws DataStoreManagerException {
		try {
			return this.resultSet.getDate(index);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_INDEX, Integer.toString(index));
		}
	}

	@Override
	public Timestamp getTimestamp(int index) throws DataStoreManagerException {
		try {
			return this.resultSet.getTimestamp(index);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_INDEX, Integer.toString(index));
		}
	}
	
	@Override
	public byte[] getBytes(int index) throws DataStoreManagerException {
		try {
			return this.resultSet.getBytes(index);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_INDEX, Integer.toString(index));
		}
	}
	
	@Override
	public Object getObject(int index) throws DataStoreManagerException {
		try {
			return this.resultSet.getObject(index);
		} catch (SQLException e) {
			throw new DataStoreManagerException(GET_COLUMN_IS_FAILE_BY_INDEX, Integer.toString(index));
		}
	}
	
	/**
	 * 指定のバイト配列をインスタンスに変換して返却します。<p/>
	 * 入出力例外が発生した、バイト配列がインスタンスでなくクラスの変換に失敗した場合、例外を送出します。
	 * 
	 * @param bytes バイト配列
	 * @return インスタンス
	 * @throws CrawlerException 入出力例外が発生した、クラスの変換に失敗した場合
	 */
	protected Object convertBytesToObject(byte[] bytes) throws DataStoreManagerException {
		if (bytes == null || bytes.length == 0) return null;
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			Object object = objectInputStream.readObject();
			objectInputStream.close();
			byteArrayInputStream.close();
			return object;
		} catch (IOException e) {
			throw new DataStoreManagerException(FAILE_TO_AN_ATTEMPT_WAS_MADE_TO_CONVERT_TO_OBJECT, e);
		} catch (ClassNotFoundException e) {
			throw new DataStoreManagerException(FAILE_TO_AN_ATTEMPT_WAS_MADE_TO_CONVERT_TO_OBJECT, e);
		}
	}
}
