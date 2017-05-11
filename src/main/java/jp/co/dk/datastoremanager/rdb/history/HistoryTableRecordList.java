package jp.co.dk.datastoremanager.rdb.history;

import java.util.ArrayList;
import java.util.List;

public class HistoryTableRecordList {
	
	protected List<HistoryTableTmpRecord> historyTableTmpRecordList = new ArrayList<>();
	
	protected List<HistoryTableRecord> historyTableRecordList = new ArrayList<>();
	
	public HistoryTableRecordList(List<HistoryTableTmpRecord> historyTableTmpRecordList) {
		this.historyTableTmpRecordList = historyTableTmpRecordList;
		HistoryTableTmpRecord u1Record = null;
		for (HistoryTableTmpRecord historyTableTmpRecord : historyTableTmpRecordList) {
			switch(historyTableTmpRecord.getOperationType()) {
				case IN:
					historyTableRecordList.add(new HistoryTableInsertRecord(historyTableTmpRecord));
					break;
				case U1:
					u1Record = historyTableTmpRecord;
					break;
				case U2:
					historyTableRecordList.add(new HistoryTableUpdateRecord(u1Record, historyTableTmpRecord));
					break;
				case DL:
					historyTableRecordList.add(new HistoryTableDeleteRecord(historyTableTmpRecord));
					break;
			}
		}
	}
}
