package jp.co.dk.datastoremanager.core.rdb.history;

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
		return this.historyTableUpdateFromRecord.createTrRecord(document, new AddHistoryTrRecord() {
			@Override
			public void addLeftSideTd(Document document, Element trElement) {
				Element opeTimeTd = document.createElement("td");
				opeTimeTd.setAttribute("rowspan", "2");
				opeTimeTd.setTextContent(DateFormat.YYYYMMDD_HH24MISS.parse(historyTableUpdateFromRecord.getOperationTime()));
				trElement.appendChild(opeTimeTd);
				
				Element kindTd = document.createElement("td");
				kindTd.setTextContent(historyTableUpdateFromRecord.getOperationType().getName());
				trElement.appendChild(kindTd);
			}
			@Override
			public void addRightSideTd(Document document, Element trElement) {
			}
		});
	}

	@Override
	Element createAfterTrRecord(Document document) {
		return this.historyTableUpdateToRecord.createTrRecord(document, new AddHistoryTrRecord() {
			@Override
			public void addLeftSideTd(Document document, Element trElement) {
				// Element opeTimeTd = document.createElement("td");
				// opeTimeTd.setAttribute("rowspan", "2");
				// opeTimeTd.setTextContent(historyTableUpdateToRecord.getOperationTime().toString());
				// trElement.appendChild(opeTimeTd);
				
				Element kindTd = document.createElement("td");
				kindTd.setTextContent(historyTableUpdateToRecord.getOperationType().getName());
				trElement.appendChild(kindTd);
			}
			@Override
			public void addRightSideTd(Document document, Element trElement) {
			}
		});
	}

}
