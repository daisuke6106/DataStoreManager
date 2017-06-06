package jp.co.dk.datastoremanager.rdb.history;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HistoryTableUpdateRecord extends HistoryTableRecord {

	protected HistoryTableTmpRecord historyTableUpdateFromRecord;
	
	protected HistoryTableTmpRecord historyTableUpdateToRecord;
	
	public HistoryTableUpdateRecord(HistoryTableTmpRecord historyTableUpdateFromRecord, HistoryTableTmpRecord historyTableUpdateToRecord) {
		this.historyTableUpdateFromRecord = historyTableUpdateFromRecord;
		this.historyTableUpdateToRecord   = historyTableUpdateToRecord;
	}

	@Override
	Element createBeforeTrRecord(Document document) {
		return this.historyTableUpdateFromRecord.createTrRecord(document);
	}

	@Override
	Element createAfterTrRecord(Document document) {
		return this.historyTableUpdateToRecord.createTrRecord(document);
	}

}
