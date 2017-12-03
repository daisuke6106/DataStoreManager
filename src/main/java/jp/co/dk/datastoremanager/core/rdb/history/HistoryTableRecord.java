package jp.co.dk.datastoremanager.core.rdb.history;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class HistoryTableRecord {

	abstract Element createBeforeTrRecord(Document document);
	
	abstract Element createAfterTrRecord(Document document);
	
}

