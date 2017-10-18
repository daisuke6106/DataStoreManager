package jp.co.dk.datastoremanager.core.rdb.history;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface AddHistoryTrRecord {
	
	void addLeftSideTd (Document document, Element trElement);
	
	void addRightSideTd(Document document, Element trElement);
	
}
