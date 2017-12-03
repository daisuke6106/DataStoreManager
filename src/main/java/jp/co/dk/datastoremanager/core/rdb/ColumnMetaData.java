package jp.co.dk.datastoremanager.core.rdb;

import static jp.co.dk.datastoremanager.core.message.DataStoreExporterMessage.FAILED_TO_ACQUIRE_COLUMN_INFO;

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
	
	/** テーブルメタデータ */
	protected TableMetaData tableMetaData;
	
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
	
	protected ColumnMetaData(TableMetaData tableMetaData, ResultSet resultSet, int index) throws SQLException {
		this.index         = index;
		this.databaseName  = resultSet.getString("TABLE_CAT");
		this.tableName     = resultSet.getString("TABLE_NAME");
		this.columnname    = resultSet.getString("COLUMN_NAME");
		this.remarks       = resultSet.getString("REMARKS");
		this.size          = resultSet.getInt("COLUMN_SIZE");
		this.columnType    = resultSet.getString("TYPE_NAME");
		this.columnIntType = resultSet.getInt("DATA_TYPE");
		this.tableMetaData = tableMetaData;
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
	
	public boolean isPrimaryKey() throws DataStoreManagerException {
		if (this.tableMetaData == null) {
			throw new DataStoreManagerException(FAILED_TO_ACQUIRE_COLUMN_INFO);
		}
		for (PrimaryKeyMetaData primaryKeyMetaData : this.tableMetaData.getPrimaryKey()) {
			if (primaryKeyMetaData.columnName.equals(this.columnname)) {
				return true;
			}
		}
		return false;
	}
	
	public abstract ColumnData getData(DataBaseRecord record) throws DataStoreManagerException ;
	
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
