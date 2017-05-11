package jp.co.dk.datastoremanager.rdb.history;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.ColumnMetaData;
import jp.co.dk.datastoremanager.rdb.DataBaseRecord;
import jp.co.dk.datastoremanager.rdb.DataConvertable;

public class HistoryTableTmpRecord implements DataConvertable {
	
	protected HistoryTableMetaData historyTableMetaData;
	
	protected List<ColumnMetaData> columnList;
	
	protected Date operationTime;
	
	protected OperationType operationType;
	
	protected List<Object> columnData = new ArrayList<>();
	
	HistoryTableTmpRecord(HistoryTableMetaData historyTableMetaData, List<ColumnMetaData> columnList, Date operationTime, OperationType operationType, List<Object> columnData) throws DataStoreManagerException {
		this.historyTableMetaData = historyTableMetaData;
		this.columnList           = columnList;
		this.operationTime        = operationTime;
		this.operationType        = operationType;
		this.columnData           = columnData;
	}
	
	public Date getOperationTime() {
		return this.operationTime;
	}
	
	public OperationType getOperationType() {
		return this.operationType;
	}

	public HistoryTableTmpRecord(HistoryTableMetaData historyTableMetaData, List<ColumnMetaData> columnList) {
		this.historyTableMetaData = historyTableMetaData;
		this.columnList           = columnList;
	}
	
	public DataConvertable convert(DataBaseRecord dataBaseRecord) throws DataStoreManagerException {
		Date          operationTime = dataBaseRecord.getDate("OPTM");
		OperationType operationType = OperationType.valueOf( dataBaseRecord.getString("OPTP") );
		List<Object> columnData = new ArrayList<>();
		for (ColumnMetaData column : columnList) columnData.add(column.getData(dataBaseRecord));
		return new HistoryTableTmpRecord(this.historyTableMetaData, this.columnList, operationTime, operationType, columnData);
	}
	
	
}
