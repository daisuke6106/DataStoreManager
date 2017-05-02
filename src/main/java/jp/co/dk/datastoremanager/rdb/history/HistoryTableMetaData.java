package jp.co.dk.datastoremanager.rdb.history;

import java.util.Date;
import java.util.List;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.TableMetaData;


public abstract class HistoryTableMetaData {

	protected TableMetaData tableMetaData;
	
	protected HistoryTableMetaData(TableMetaData tableMetaData) {
		this.tableMetaData = tableMetaData;
	}

	public abstract List<HistoryTableRecord> getRecordAfterSpecifiedDate(Date targetDate) throws DataStoreManagerException ;
}
