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
		return this.historyTableInsertRecord.createBrankTrRecord(document, "nothing", new AddHistoryTrRecord() {
			@Override
			public void addLeftSideTd(Document document, Element trElement) {
				Element opeTimeTd = document.createElement("td");
				opeTimeTd.setTextContent(historyTableInsertRecord.getOperationTime().toString());
				opeTimeTd.setAttribute("rowspan", "2");
				trElement.appendChild(opeTimeTd);
				Element kindTd = document.createElement("td");
				kindTd.setAttribute("rowspan", "2");
				kindTd.setTextContent(historyTableInsertRecord.getOperationType().toString());
				trElement.appendChild(kindTd);
			}
			@Override
			public void addRightSideTd(Document document, Element trElement) {
			}
		});
	}

	@Override
	Element createAfterTrRecord(Document document) {
		return this.historyTableInsertRecord.createTrRecord(document, new AddHistoryTrRecord() {
			@Override
			public void addLeftSideTd(Document document, Element trElement) {
			}
			@Override
			public void addRightSideTd(Document document, Element trElement) {
			}
		});
	}
}
