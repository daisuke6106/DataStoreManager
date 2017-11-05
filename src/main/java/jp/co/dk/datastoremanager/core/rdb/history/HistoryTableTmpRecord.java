package jp.co.dk.datastoremanager.core.rdb.history;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.ColumnData;
import jp.co.dk.datastoremanager.core.rdb.ColumnMetaData;
import jp.co.dk.datastoremanager.core.rdb.DataBaseRecord;
import jp.co.dk.datastoremanager.core.rdb.DataConvertable;
import jp.co.dk.datastoremanager.core.rdb.TimestampColumnData;

public class HistoryTableTmpRecord implements DataConvertable {
	
	protected HistoryTableMetaData historyTableMetaData;
	
	protected List<ColumnMetaData> columnList;
	
	protected TimestampColumnData operationTime;
	
	protected OperationType operationType;
	
	protected List<ColumnData> columnData = new ArrayList<>();
	
	HistoryTableTmpRecord(HistoryTableMetaData historyTableMetaData, List<ColumnMetaData> columnList, TimestampColumnData operationTime, OperationType operationType, List<ColumnData> columnData) throws DataStoreManagerException {
		this.historyTableMetaData = historyTableMetaData;
		this.columnList           = columnList;
		this.operationTime        = operationTime;
		this.operationType        = operationType;
		this.columnData           = columnData;
	}
	
	public TimestampColumnData getOperationTime() {
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
		TimestampColumnData operationTime = dataBaseRecord.getTimestamp("OPTM");
		OperationType operationType = OperationType.valueOf( dataBaseRecord.getString("OPTP").get() );
		List<ColumnData> columnData = new ArrayList<>();
		for (ColumnMetaData column : columnList) columnData.add(column.getData(dataBaseRecord));
		return new HistoryTableTmpRecord(this.historyTableMetaData, this.columnList, operationTime, operationType, columnData);
	}
	
	Element createTrRecord(Document document, AddHistoryTrRecord addHistoryTrRecord) {
		Element tr = document.createElement("tr");
		if (addHistoryTrRecord != null) addHistoryTrRecord.addLeftSideTd(document, tr);
		for (ColumnData column : columnData) {
			Element td = document.createElement("td");
			if (column == null) {
				td.setTextContent("(NULL)");
			} else {
				td.setTextContent(column.getDataByString());
			}
			tr.appendChild(td);
		}
		if (addHistoryTrRecord != null) addHistoryTrRecord.addRightSideTd(document, tr);
		return tr;
	}
	
	Element createBrankTrRecord(Document document, String dispStr, AddHistoryTrRecord addHistoryTrRecord) {
		Element tr = document.createElement("tr");
		if (addHistoryTrRecord != null) addHistoryTrRecord.addLeftSideTd(document, tr);
		Element td = document.createElement("td");
		td.setTextContent(dispStr);
		td.setAttribute("colspan", Integer.toString(this.columnList.size()));
		tr.appendChild(td);
		if (addHistoryTrRecord != null) addHistoryTrRecord.addRightSideTd(document, tr);
		return tr;
	}
}
