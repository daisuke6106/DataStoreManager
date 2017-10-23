package jp.co.dk.datastoremanager.core.rdb.history;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class HistoryTableRecord {

	abstract Element createBeforeTrRecord(Document document);
	
	abstract Element createAfterTrRecord(Document document);
	
}

enum DateFormat {
	YYYYMMDD(new SimpleDateFormat("yyyy/MM/dd")),
	YYYYMMDD_HH24MISS(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss SSS")),
	;
	protected SimpleDateFormat format;
	private DateFormat(SimpleDateFormat format) {
		this.format = format;
	}
	String parse(Date date) {
		return this.format.format(date);
	}
}
