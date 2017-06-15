package jp.co.dk.datastoremanager.rdb;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.history.HistoryTableMetaData;
import static jp.co.dk.datastoremanager.message.DataStoreExporterMessage.*;

public abstract class TableMetaData {
	
	protected static final String HISTRY_TABLE_NAME_HEADER = "H$";
	
	protected DataBaseDataStore dataBaseDataStore;
	
	protected String schemaName;
	
	protected String tableName;
	
	protected TableMetaData(DataBaseDataStore dataBaseDataStore, String schemaName, String tableName) {
		this.dataBaseDataStore = dataBaseDataStore;
		this.schemaName = schemaName;
		this.tableName = tableName;
	}

	public List<ColumnMetaData> getColumns() throws DataStoreManagerException {
		try {
			List<ColumnMetaData> columnMetaDataList = new ArrayList<>();
			DatabaseMetaData dbmd = this.dataBaseDataStore.transaction.connection.getMetaData();
			ResultSet rs = dbmd.getColumns(null, this.schemaName, this.tableName, "%");
			
//			ResultSetMetaData metaData= rs.getMetaData();
//			for (int i = 1; i <= metaData.getColumnCount(); i++) System.out.println(metaData.getColumnName(i));
			
			for (int i=0; rs.next(); i++) columnMetaDataList.add(this.createColumnMetaData(rs, i));
			return columnMetaDataList;
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILED_TO_ACQUIRE_COLUMN_INFO, e);
		}
	}
	
	protected abstract ColumnMetaData createColumnMetaData(ResultSet rs, int i) throws SQLException;
	
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
