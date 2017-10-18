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
		return this.historyTableDeleteRecord.createTrRecord(document, new AddHistoryTrRecord() {
			@Override
			public void addLeftSideTd(Document document, Element trElement) {
				Element opeTimeTd = document.createElement("td");
				opeTimeTd.setTextContent(historyTableDeleteRecord.getOperationTime().toString());
				opeTimeTd.setAttribute("rowspan", "2");
				trElement.appendChild(opeTimeTd);
				Element kindTd = document.createElement("td");
				kindTd.setTextContent(historyTableDeleteRecord.getOperationType().toString());
				kindTd.setAttribute("rowspan", "2");
				trElement.appendChild(kindTd);
			}
			@Override
			public void addRightSideTd(Document document, Element trElement) {
			}
		});
	}

	@Override
	Element createAfterTrRecord(Document document) {
		return this.historyTableDeleteRecord.createBrankTrRecord(document, "deleted", new AddHistoryTrRecord() {
			@Override
			public void addLeftSideTd(Document document, Element trElement) {
			}
			@Override
			public void addRightSideTd(Document document, Element trElement) {
			}
		});
	}

}
