package jp.co.dk.datastoremanager.rdb.history;

import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.ColumnMetaData;
import jp.co.dk.datastoremanager.rdb.DataBaseRecord;
import jp.co.dk.datastoremanager.rdb.DataConvertable;

public class HistoryTableRecord implements DataConvertable {
	
	protected HistoryTableMetaData historyTableMetaData;
	
	protected List<ColumnMetaData> columns;
	
	protected List<Object> columnData = new ArrayList<>();
	
	public HistoryTableRecord(HistoryTableMetaData historyTableMetaData) throws DataStoreManagerException {
		this.historyTableMetaData = historyTableMetaData;
		this.columns              = this.historyTableMetaData.tableMetaData.getColumns();
	}
	
	private HistoryTableRecord(HistoryTableMetaData historyTableMetaData, List<ColumnMetaData> columns) throws DataStoreManagerException {
		this.historyTableMetaData = historyTableMetaData;
		this.columns              = columns;
	}
	
	public DataConvertable convert(DataBaseRecord dataBaseRecord) throws DataStoreManagerException {
		HistoryTableRecord record = new HistoryTableRecord(this.historyTableMetaData, this.columns);
		for (ColumnMetaData column : columns) record.columnData.add(column.getData(dataBaseRecord));
		return record;
	}
	
}
