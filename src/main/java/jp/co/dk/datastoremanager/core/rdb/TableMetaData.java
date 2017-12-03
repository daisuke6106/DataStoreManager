package jp.co.dk.datastoremanager.core.rdb;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.history.HistoryTableMetaData;
import jp.co.dk.datastoremanager.core.rdb.DataBaseDataStore;
import static jp.co.dk.datastoremanager.core.message.DataStoreExporterMessage.*;

public abstract class TableMetaData {
	
	protected static final String HISTRY_TABLE_NAME_HEADER = "H$";
	
	protected DataBaseDataStore dataBaseDataStore;
	
	protected String schemaName;
	
	protected String tableName;

	/** プライマリーキー情報一覧のキャッシュ */
	protected List<PrimaryKeyMetaData> primaryKeyMetaDataList;
	
	protected TableMetaData(DataBaseDataStore dataBaseDataStore, String schemaName, String tableName) throws DataStoreManagerException {
		this.dataBaseDataStore = dataBaseDataStore;
		this.schemaName = schemaName;
		this.tableName = tableName;
	}

	public List<ColumnMetaData> getColumns() throws DataStoreManagerException {
		try {
			List<ColumnMetaData> columnMetaDataList = new ArrayList<>();
			DatabaseMetaData dbmd = this.dataBaseDataStore.transaction.connection.getMetaData();
			ResultSet columnsResultSet    = dbmd.getColumns(null, this.schemaName, this.tableName, "%");
			for (int i=0; columnsResultSet.next(); i++) columnMetaDataList.add(this.createColumnMetaData(this, columnsResultSet, i));
			return columnMetaDataList;
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILED_TO_ACQUIRE_COLUMN_INFO, e);
		}
	}
	
	/**
	 * <p>このテーブルが持つ全プライマリーキー情報を取得する。</p>
	 * 一度取得したらキャッシュ化され、以降はキャッシュ化された情報を返却する。
	 * 
	 * @return プライマリーキー情報一覧
	 * @throws DataStoreManagerException プライマリーキー情報の取得に失敗した場合
	 */
	public List<PrimaryKeyMetaData> getPrimaryKey() throws DataStoreManagerException {
		if (this.primaryKeyMetaDataList == null) {
			List<PrimaryKeyMetaData> primaryKeyMetaDataList = new ArrayList<>();
			for (PrimaryKeyMetaData primaryKeyMetaData : this.dataBaseDataStore.getPrimaryKey()) {
				if (primaryKeyMetaData.tableName.equals(this.tableName)) primaryKeyMetaDataList.add(primaryKeyMetaData);
			}
			this.primaryKeyMetaDataList = primaryKeyMetaDataList;
		}
		return new ArrayList<>(this.primaryKeyMetaDataList);
	}
	
	protected abstract ColumnMetaData createColumnMetaData(TableMetaData tableMetaData, ResultSet rs, int i) throws SQLException;
	
	public String getHistoryTableName() {
		return HISTRY_TABLE_NAME_HEADER + this.tableName;
	}
	
	public abstract boolean isExistsHistoryTable() throws DataStoreManagerException;
	
	public abstract boolean createHistoryTable() throws DataStoreManagerException;

	public abstract boolean dropHistoryTable() throws DataStoreManagerException;
	
	public abstract boolean createTriggerHistoryTable() throws DataStoreManagerException;
	
	public abstract boolean dropHistoryTrigger() throws DataStoreManagerException;
	
	public abstract HistoryTableMetaData getHistoryTable() throws DataStoreManagerException ;
	
	public DataBaseDataStore getDataBaseDataStore() {
		return this.dataBaseDataStore;
	}
	
	@Override
	public String toString() {
		return this.tableName;
	}
	
}
