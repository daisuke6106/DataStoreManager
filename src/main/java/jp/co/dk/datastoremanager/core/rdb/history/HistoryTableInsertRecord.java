package jp.co.dk.datastoremanager.core.rdb.history;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HistoryTableInsertRecord extends HistoryTableRecord {

	protected HistoryTableTmpRecord historyTableInsertRecord;
	
	public HistoryTableInsertRecord(HistoryTableTmpRecord historyTableInsertRecord) {
		this.historyTableInsertRecord = historyTableInsertRecord;
	}

	@Override
	Element createBeforeTrRecord(Document document) {
		return this.historyTableInsertRecord.createBrankTrRecord(document, "nothing");
	}

	@Override
	Element createAfterTrRecord(Document document) {
		return this.historyTableInsertRecord.createTrRecord(document);
	}
}
