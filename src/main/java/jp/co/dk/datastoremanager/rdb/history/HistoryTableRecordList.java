package jp.co.dk.datastoremanager.rdb.history;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HistoryTableRecordList {
	
	protected HistoryTableMetaData historyTableMetaData;
	
	protected List<HistoryTableTmpRecord> historyTableTmpRecordList = new ArrayList<>();
	
	protected List<HistoryTableRecord> historyTableRecordList = new ArrayList<>();
	
	public HistoryTableRecordList(HistoryTableMetaData historyTableMetaData, List<HistoryTableTmpRecord> historyTableTmpRecordList) {
		this.historyTableMetaData = historyTableMetaData;
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
	
	public void writeHtml(OutputStream ops) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        docbuilder             = documentBuilderFactory.newDocumentBuilder();
			Document               document               = docbuilder.newDocument();
			Element                htmlElement            = document.createElement("html");
			
			Element                titleElement           = document.createElement("title");
			titleElement.setTextContent(this.historyTableMetaData.getTableMetaData().toString());
			htmlElement.appendChild(titleElement);
			
			
			// カラム出力
			Element trHeader = this.historyTableMetaData.createTrHeader(document);
			
			// データ出力
			for (HistoryTableRecord historyTableRecord : historyTableRecordList) {
				Element tableElement = document.createElement("table");
				tableElement.setAttribute("border", "1");
				tableElement.appendChild(trHeader.cloneNode(true));
				tableElement.appendChild(historyTableRecord.createBeforeTrRecord(document));
				tableElement.appendChild(historyTableRecord.createAfterTrRecord(document));
				htmlElement.appendChild(tableElement);
			}
			
            TransformerFactory tfactory = TransformerFactory.newInstance();
            Transformer transformer = tfactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "html");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(htmlElement), new StreamResult(ops));
 
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
						e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public void writeExcel(WorkSheet worksheet) throws ExcelDocumentException {
//		
//		Workbook workbook = HSSFWorkbook();
//	}
}
