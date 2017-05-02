package jp.co.dk.datastoremanager.rdb;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import static jp.co.dk.datastoremanager.message.DataStoreExporterMessage.*;

public abstract class TableMetaData {
	
	protected static final String HISTRY_TABLE_NAME_HEADER = "H$";
	
	protected Transaction transaction;
	
	protected String schemaName;
	
	protected String tableName;
	
	protected TableMetaData(Transaction transaction, String schemaName, String tableName) {
		this.transaction = transaction;
		this.schemaName = schemaName;
		this.tableName = tableName;
	}

	public List<ColumnMetaData> getColumns() throws DataStoreManagerException {
		try {
			List<ColumnMetaData> columnMetaDataList = new ArrayList<>();
			DatabaseMetaData dbmd = this.transaction.connection.getMetaData();
			ResultSet rs = dbmd.getColumns(null, this.schemaName, this.tableName, "%");
			for (int i=0; rs.next(); i++) columnMetaDataList.add(new ColumnMetaData(rs, i));
			return columnMetaDataList;
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILED_TO_ACQUIRE_COLUMN_INFO, e);
		}
	}
	
	protected String getHistoryTableName() {
		return HISTRY_TABLE_NAME_HEADER + this.tableName;
	}
	
	public abstract boolean isExistsHistoryTable() throws DataStoreManagerException;
	
	public abstract boolean createHistoryTable() throws DataStoreManagerException;

	public abstract boolean dropHistoryTable() throws DataStoreManagerException;
	
	public abstract boolean createTriggerHistoryTable() throws DataStoreManagerException;
	
	public abstract boolean dropHistoryTrigger() throws DataStoreManagerException;
	
	@Override
	public String toString() {
		return this.tableName;
	}
	
}
