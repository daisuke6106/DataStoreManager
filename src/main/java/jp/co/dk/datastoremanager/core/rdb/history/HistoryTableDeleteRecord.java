package jp.co.dk.datastoremanager.core.rdb.history;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HistoryTableDeleteRecord extends HistoryTableRecord {

	protected HistoryTableTmpRecord historyTableDeleteRecord;
	
	public HistoryTableDeleteRecord(HistoryTableTmpRecord historyTableDeleteRecord) {
		this.historyTableDeleteRecord = historyTableDeleteRecord;
	}

	@Override
	Element createBeforeTrRecord(Document document) {
		return this.historyTableDeleteRecord.createTrRecord(document);
	}

	@Override
	Element createAfterTrRecord(Document document) {
		return this.historyTableDeleteRecord.createBrankTrRecord(document, "deleted");
	}

}
