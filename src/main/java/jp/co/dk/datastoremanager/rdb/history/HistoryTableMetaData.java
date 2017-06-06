package jp.co.dk.datastoremanager.rdb.history;

import java.util.Date;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.ColumnMetaData;
import jp.co.dk.datastoremanager.rdb.TableMetaData;

public abstract class HistoryTableMetaData {

	protected TableMetaData tableMetaData;
	
	protected HistoryTableMetaData(TableMetaData tableMetaData) {
		this.tableMetaData = tableMetaData;
	}
	
	public TableMetaData getTableMetaData() {
		return this.tableMetaData;
	}

	public abstract HistoryTableRecordList getRecordAfterSpecifiedDate(Date targetDate) throws DataStoreManagerException;
	
	public Element createTrHeader(Document document) {
		
		try {
			Element tr = document.createElement("tr");
			for (ColumnMetaData columnMetaData : this.tableMetaData.getColumns()) {
				Element th = document.createElement("th");
				th.setTextContent(columnMetaData.getColumnname());
				tr.appendChild(th);
			}
			return tr;
		} catch (DataStoreManagerException | DOMException e) {
			return null;
		}
	}
}
