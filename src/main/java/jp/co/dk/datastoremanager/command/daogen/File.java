package jp.co.dk.datastoremanager.command.daogen;

import java.util.List;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.ColumnMetaData;
import jp.co.dk.datastoremanager.core.rdb.TableMetaData;
import jp.co.dk.document.ElementName;

public class File {
	
	protected jp.co.dk.document.Element fileElement;
	
	File(jp.co.dk.document.Element fileElement) {
		this.fileElement = fileElement;
	}
	
	String getContent(TableMetaData tableMetaData, ColumnSetting columnSetting) throws DataStoreManagerException {
		String content = this.fileElement.getChildElement(new ElementName(){
			@Override
			public String getName() {
				return "content";
			}
		}).get(0).getContent();
		
		content = content.replaceAll("\\$\\{TABLE_NAME\\}", tableMetaData.toString());
		
		List<ColumnMetaData> columnMetaDataList = tableMetaData.getColumns();
		
		for (jp.co.dk.document.Element columnElement : this.fileElement.getChildElement(new ElementName(){
			@Override
			public String getName() {
				return "column";
			}
		})) {
			String columnContent = this.getColumnContent(columnElement, columnSetting, columnMetaDataList);
			content = content.replaceAll("\\$\\{" + "column." + columnElement.getAttribute("name") + "\\}", columnContent);
		}
		
		return content;
	}
	
	private String getColumnContent(jp.co.dk.document.Element columnElement, ColumnSetting columnSetting, List<ColumnMetaData> columnMetaDataList) throws DataStoreManagerException {
		StringBuilder columnListStr = new StringBuilder();
		
		for (ColumnMetaData columnMetaData : columnMetaDataList) {
			String content = columnElement.getContent();
			for (ColumnVariable columnVariable : ColumnVariable.values()) {
				content = columnVariable.convertContent(content, columnMetaData);
			}
			content = columnSetting.convertContent(content, columnMetaData.getColumnType());
			columnListStr.append(content);
		}
		return columnListStr.toString();
	}
}

enum ColumnVariable {
	COLUMN_COMMENT("COLUMN_COMMENT", new Replacer(){
		@Override
		public String replace(ColumnMetaData columnMetaData) {
			return columnMetaData.getColumnname();
		}
	}),
	
	COLUMN_NAME("COLUMN_NAME", new Replacer(){
		@Override
		public String replace(ColumnMetaData columnMetaData) {
			return columnMetaData.getColumnname();
		}
	}),
	
	COLUMN_TYPE("COLUMN_TYPE", new Replacer(){
		@Override
		public String replace(ColumnMetaData columnMetaData) {
			return columnMetaData.getColumnType();
		}
	})
	;
	
	private String variable;
	
	private Replacer replacer;
	
	private ColumnVariable(String variable, Replacer replacer) {
		this.variable = variable;
		this.replacer = replacer;
	}
	
	public String convertContent(String content, ColumnMetaData columnMetaData) {
		return content.replaceAll("\\$\\{" + this.variable + "\\}", this.replacer.replace(columnMetaData));
	}
	
	private interface Replacer { 
		String replace(ColumnMetaData columnMetaData);
	}
}