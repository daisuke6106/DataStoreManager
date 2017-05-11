package jp.co.dk.datastoremanager.rdb.history;

public class HistoryTableDeleteRecord extends HistoryTableRecord {

	protected HistoryTableTmpRecord historyTableDeleteRecord;
	
	public HistoryTableDeleteRecord(HistoryTableTmpRecord historyTableDeleteRecord) {
		this.historyTableDeleteRecord = historyTableDeleteRecord;
	}

}
