package jp.co.dk.datastoremanager.core.rdb;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.DataBaseRecord;

/**
 * カラムに付随するメタデータを表すクラスです。
 * カラムが所属するデータベース名、カラム名、カラムサイズ、カラムタイプなどを保持します。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class ColumnMetaData {
	
	/** インデックス */
	protected int index;
	
	/** データベース名 */
	protected String databaseName;
	
	/** テーブル名 */
	protected String tableName;

	/** カラム名 */
	protected String columnname;
	
	/** カラムサイズ */
	protected int size;
	
	/** カラムタイプ */
	protected String columnType;
	
	/** カラムタイプ（数値） */
	protected int columnIntType;
	
	/** コメント */
	protected String remarks;
	
	/**
	 * <p>コンストラクタ</p>
	 * ResultSetMetaDataと、インデックスを基にカラムメタデータオブジェクトを生成します。
	 * 
	 * @param metaData メタデータオブジェト
	 * @param index インデックス 
	 * @throws SQLException データベースアクセスエラーが発生した場合
	 */
	protected ColumnMetaData(ResultSetMetaData metaData, int index) throws SQLException {
		this.index        = index;
		this.databaseName = metaData.getCatalogName(index);
		this.tableName    = metaData.getTableName(index);
		this.columnname   = metaData.getColumnName(index);
		this.remarks      = metaData.getColumnLabel(index);
		this.size         = metaData.getColumnDisplaySize(index);
		this.columnType   = metaData.getColumnTypeName(index);
		this.columnIntType= metaData.getColumnType(index);
	}
	
	protected ColumnMetaData(ResultSet resultSet, int index) throws SQLException {
		this.index        = index;
		this.databaseName = resultSet.getString("TABLE_CAT");
		this.tableName    = resultSet.getString("TABLE_NAME");
		this.columnname   = resultSet.getString("COLUMN_NAME");
		this.remarks      = resultSet.getString("REMARKS");
		this.size         = resultSet.getInt("COLUMN_SIZE");
		this.columnType   = resultSet.getString("TYPE_NAME");
		this.columnIntType= resultSet.getInt("DATA_TYPE");
		
	}
	
	/**
	 * データベース名を取得します。
	 * @return データベース名
	 */
	public String getDatabaseName() {
		return databaseName;
	}
	
	/**
	 * テーブル名を取得します。
	 * @return テーブル名
	 */
	public String getTableName() {
		return tableName;
	}
	
	/**
	 * カラム名を取得します。
	 * @return カラム名
	 */
	public String getColumnname() {
		return columnname;
	}
	
	/**
	 * カラムサイズを取得します。
	 * @return カラムサイズ
	 */
	public int getSize() {
		return size;
	}

	/**
	 * カラムのＳＱＬ型を取得します。
	 * @return カラムのＳＱＬ型
	 */
	public String getColumnType() {
		return columnType;
	}

	public abstract Object getData(DataBaseRecord record) throws DataStoreManagerException ;
	
//	public Object getData(DataBaseRecord record) throws DataStoreManagerException {
//		switch (columnIntType) {
//			case java.sql.Types.ARRAY:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.BIGINT:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.BINARY:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.BIT:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.BLOB:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.BOOLEAN:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.CHAR:
//				return record.getString(columnname);
//			case java.sql.Types.CLOB:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.DATALINK:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.DATE:
//				return record.getDate(columnname);
//			case java.sql.Types.DECIMAL:
//				return record.getBigDecimal(columnname);
//			case java.sql.Types.DISTINCT:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.DOUBLE:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.FLOAT:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.INTEGER:
//				return new Integer(record.getInt(columnname));
//			case java.sql.Types.JAVA_OBJECT:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.LONGNVARCHAR:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.LONGVARBINARY:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.LONGVARCHAR:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.NCHAR:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.NCLOB:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.NULL:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.NUMERIC:
//				return new Integer(record.getInt(columnname));
//			case java.sql.Types.NVARCHAR:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.OTHER:
//				return record.getString(columnname);
//			case java.sql.Types.REAL:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.REF:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.ROWID:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.SMALLINT:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.SQLXML:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.STRUCT:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.TIME:
//				return record.getDate(columnname);
//			case java.sql.Types.TIMESTAMP:
//				return record.getDate(columnname);
//			case java.sql.Types.TINYINT:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.VARBINARY:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//			case java.sql.Types.VARCHAR:
//				return record.getString(columnname);
//			default:
//				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
//		}
//	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ColumnMetaData [databaseName=");
		builder.append(databaseName);
		builder.append(", tableName=");
		builder.append(tableName);
		builder.append(", columnname=");
		builder.append(columnname);
		builder.append(", size=");
		builder.append(size);
		builder.append(", columnType=");
		builder.append(columnType);
		builder.append("]");
		return builder.toString();
	}
}