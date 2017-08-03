package jp.co.dk.datastoremanager.core.rdb.history;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.ColumnMetaData;
import jp.co.dk.datastoremanager.core.rdb.DataBaseRecord;
import jp.co.dk.datastoremanager.core.rdb.DataConvertable;

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
	
	Element createTrRecord(Document document) {
		Element tr = document.createElement("tr");
		for (Object column : columnData) {
			Element td = document.createElement("td");
			td.setTextContent(column.toString());
			tr.appendChild(td);
		}
		return tr;
	}
	
	Element createBrankTrRecord(Document document, String dispStr) {
		Element tr = document.createElement("tr");
		Element td = document.createElement("td");
		td.setTextContent(dispStr);
		td.setAttribute("colspan", Integer.toString(this.columnList.size()));
		tr.appendChild(td);
		return tr;
	}
}
