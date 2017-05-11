package jp.co.dk.datastoremanager.rdb.history;

public class HistoryTableInsertRecord extends HistoryTableRecord {

	protected HistoryTableTmpRecord historyTableInsertRecord;
	
	public HistoryTableInsertRecord(HistoryTableTmpRecord historyTableInsertRecord) {
		this.historyTableInsertRecord = historyTableInsertRecord;
	}

}
