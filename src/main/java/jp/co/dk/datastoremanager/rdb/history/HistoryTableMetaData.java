package jp.co.dk.datastoremanager.rdb.history;

import java.util.Date;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.TableMetaData;

public abstract class HistoryTableMetaData {

	protected TableMetaData tableMetaData;
	
	protected HistoryTableMetaData(TableMetaData tableMetaData) {
		this.tableMetaData = tableMetaData;
	}

	public abstract HistoryTableRecordList getRecordAfterSpecifiedDate(Date targetDate) throws DataStoreManagerException;
	
}
