package jp.co.dk.datastoremanager.command.exporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.dk.datastoremanager.core.exception.DataStoreExporterException;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.core.rdb.ColumnMetaData;
import jp.co.dk.datastoremanager.core.rdb.DataBaseRecord;
import jp.co.dk.datastoremanager.core.rdb.DataConvertable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static jp.co.dk.datastoremanager.core.message.DataStoreExporterMessage.*;

public class HtmlDBData {
	
	protected File file;
	
	protected AbstractDataBaseAccessObject dao;
	
	protected Document document;
	
	protected Element headElement;
	
	protected Element metaElement;
	
	protected Element styleElement;
	
	protected Element bodyElement;
	
	protected Element dataElement;
	
	public static HtmlDBData create(File outfile, AbstractDataBaseAccessObject dao) {
		return new HtmlDBData(FileType.CREATE, outfile, dao);
	}
	
	public static HtmlDBData read(File outfile, AbstractDataBaseAccessObject dao) {
		return new HtmlDBData(FileType.READ  , outfile, dao);
	}
	
	public static HtmlDBData auto(File outfile, AbstractDataBaseAccessObject dao) {
		if (outfile.exists() && outfile.isFile()) {
			return new HtmlDBData(FileType.READ  , outfile, dao);
		} else if (outfile.exists() && outfile.isDirectory()) {
			// TODO 
			return null;
		} else {
			return new HtmlDBData(FileType.CREATE  , outfile, dao);
		}
	}
	
	protected HtmlDBData(FileType type, File outfile, AbstractDataBaseAccessObject dao) {
			this.file        = outfile;
			this.dao         = dao;
			
			if (type == FileType.CREATE) {
				this.document     = Jsoup.parse("<html></html>");
				this.headElement  = this.document.head();
				this.bodyElement  = this.document.body();
				
				this.metaElement  = this.document.createElement("meta");
				this.metaElement.attr("http-equiv", "Content-Type");
				this.metaElement.attr("content"   , "text/html; charset=UTF-8");
				this.headElement.appendChild(this.metaElement);
				
				this.styleElement = this.document.createElement("style");
				this.styleElement.appendText(".header_name{background-color: #4682B4;} ");
				this.styleElement.appendText(".header_type{background-color: #B0C4DE;} ");
				this.headElement.appendChild(this.styleElement);
				
				this.dataElement  = this.document.createElement("div");
				this.dataElement.attr("id", "datalist");
				this.bodyElement.appendChild(this.dataElement);
				
			} else {
				try {
					this.document     = Jsoup.parse(this.file, "UTF-8");
					this.headElement  = this.document.head();
					this.bodyElement  = this.document.body();
					
					this.metaElement  = this.document.getElementsByTag("meta").get(0);
					this.styleElement = this.document.getElementsByTag("style").get(0);
					
					this.dataElement  = this.document.getElementById("datalist");
				} catch (IOException e) {
					// TODO 
				}
			}
	}
	
	public void write(jp.co.dk.datastoremanager.command.exporter.SqlFile sqlfile) throws DataStoreManagerException {
		Element dataTable  = this.document.createElement("table");
		
		Element headerTr = this.document.createElement("tr");
		dataTable.appendChild(headerTr);
		
		Element headerTh = this.document.createElement("th");
		headerTr.appendChild(headerTh);
		
		Element underbar = this.document.createElement("u");
		underbar.appendText(sqlfile.toString() + "@" + new SimpleDateFormat("yyyy/MM/dd '-' HH:mm:ss z").format(new Date()));
		headerTh.appendChild(underbar);
	    
		
		for (jp.co.dk.datastoremanager.command.exporter.Sql sql : sqlfile.getSqlList()) {
			Element oneSqlTr = this.document.createElement("tr");
			dataTable.appendChild(oneSqlTr);
			
			Element oneSqlTd = this.document.createElement("td");
			oneSqlTr.appendChild(oneSqlTd);
			
			Element oneSqlDiv   = this.document.createElement("div");
			oneSqlDiv.addClass("onesqldata");
			oneSqlTd.appendChild(oneSqlDiv);
			
			Element sqlStr      = this.document.createElement("label");
			sqlStr.appendText(sql.toString());
			oneSqlDiv.appendChild(sqlStr);
			
			Element oneSqlTable = this.getResult(sql);
			oneSqlDiv.appendChild(oneSqlTable);
			
		}
		
		this.dataElement.appendChild(dataTable);
	}
	
	protected Element getResult(jp.co.dk.datastoremanager.command.exporter.Sql sql) throws DataStoreManagerException {
		
		Element dataTable = this.document.createElement("table");
		dataTable.attr("border", "1");
		
		Element headerTr      = this.document.createElement("tr");
		headerTr.attr("class", "header_name");
		
		Element headerTypeTr  = this.document.createElement("tr");
		headerTypeTr.attr("class", "header_type");
		
		this.dao.selectMulti(sql, new DataConvertable() {
			public DataConvertable convert(DataBaseRecord dataBaseRecord) throws DataStoreManagerException {
				if (dataBaseRecord.getRowIndex() == 1) {
					for (ColumnMetaData columnMetaData : dataBaseRecord.getColumns()) {
						Element headerTd     = document.createElement("td");
						headerTd.appendText(columnMetaData.getColumnname());
						headerTr.appendChild(headerTd);
						
						Element headerTypeTd = document.createElement("td");
						headerTypeTd.appendText(columnMetaData.getColumnType());
						headerTypeTr.appendChild(headerTypeTd);
					}
					dataTable.appendChild(headerTr);
					dataTable.appendChild(headerTypeTr);
				}
				
				Element dataTr = document.createElement("tr");
				for (ColumnMetaData columnMetaData : dataBaseRecord.getColumns()) {
					Element dataTd = document.createElement("td");
					dataTd.appendText(dataBaseRecord.getString(columnMetaData.getColumnname()).toString());
					dataTr.appendChild(dataTd);
					dataTable.appendChild(dataTr);
				}
				
				dataTr.appendChild(document.createElement("td").attr("style", "display:none;").appendText(Integer.toString(dataTr.hashCode())));
				
				return null;
			}
		});
		
		return dataTable;
	}
	
	public void write() throws DataStoreExporterException {
		try (PrintWriter writer = new PrintWriter(this.file, "UTF-8")) {
			writer.write(this.document.html());
			writer.flush();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			throw new DataStoreExporterException(FAILED_TO_CREATE_HTML, this.file.toString());
		}
	}
}

enum FileType {
	CREATE,
	READ,
	;
}
