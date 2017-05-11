package jp.co.dk.datastoremanager.rdb.history;

public class HistoryTableUpdateRecord extends HistoryTableRecord {

	protected HistoryTableTmpRecord historyTableUpdateFromRecord;
	
	protected HistoryTableTmpRecord historyTableUpdateToRecord;
	
	public HistoryTableUpdateRecord(HistoryTableTmpRecord historyTableUpdateFromRecord, HistoryTableTmpRecord historyTableUpdateToRecord) {
		this.historyTableUpdateFromRecord = historyTableUpdateFromRecord;
		this.historyTableUpdateToRecord   = historyTableUpdateToRecord;
	}
}
