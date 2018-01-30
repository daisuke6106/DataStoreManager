package jp.co.dk.datastoremanager.command.daogen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import jp.co.dk.datastoremanager.command.AbtractCommandControler;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage;
import jp.co.dk.datastoremanager.core.rdb.ColumnMetaData;
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.rdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.core.rdb.TableMetaData;
import jp.co.dk.document.ElementName;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.xml.XmlDocument;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class CreateDataAccessObjectControler extends AbtractCommandControler {
	
	@Override
	public void execute(DataBaseAccessParameter dataBaseAccessParameter) {

		try {
			DataBaseDataStore dataStore = (DataBaseDataStore)dataBaseAccessParameter.createDataStore();
			dataStore.startTransaction();
			TableMetaData tableMetaData = dataStore.getTable(this.cmd.getOptionValue("table"));
			if (tableMetaData == null) {
				System.out.println("table is not found.");
				System.exit(1);
			}
			TemplateFile  templateFile  = new TemplateFile(new FileInputStream(new java.io.File(this.cmd.getOptionValue("template"))));
			ColumnSetting columnSetting = templateFile.getColumnSetting();
			for (File file : templateFile.getFileElement()) {
				System.out.println(file.getContent(tableMetaData, columnSetting));
			}
			
		} catch (DataStoreManagerException | FileNotFoundException | DocumentException e) {
			System.out.println(e.toString());
			System.exit(1);
		}
		
	}
	
	
	@Override
	protected String getCommandName() {
		return "daogen";
	}

	@SuppressWarnings("static-access")
	@Override
	protected void getAnyOptions(Options options) {
		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withDescription("テンプレートファイルを指定する。").withLongOpt("template" ).create("tmp"));
//		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withDescription("出力先ディレクトリを指定する。").withLongOpt("output" ).create("o"));
//		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withDescription("パッケージを指定する。").withLongOpt("package" ).create("p"));
		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withDescription("対象テーブル名を指定する。").withLongOpt("table" ).create("t"));
	}
	
	public static void main(String[] args) {
		CreateDataAccessObjectControler controler = new CreateDataAccessObjectControler();
		controler.execute(args);
	}
}

class TemplateFile extends XmlDocument {
	
	TemplateFile(InputStream inputStream) throws DocumentException {
		super(inputStream);
	}
	
	List<File> getFileElement() {
		List<File> fileList = new ArrayList<>();
		for (jp.co.dk.document.Element fileElement : this.getChildElement(new ElementName() {
			@Override
			public String getName() {
				return "file";
			}
		})){
			fileList.add(new File(fileElement));
		}
		return fileList;
	}
	
	ColumnSetting getColumnSetting() {
		for (jp.co.dk.document.Element columnSettingElement : this.getChildElement(new ElementName() {
			@Override
			public String getName() {
				return "column_setting";
			}
		})){
			return new ColumnSetting(columnSettingElement);
		}
		return null;
	}
	
	List<PrimaryKeyColumnList> getPrimaryKeyColumnList() {
		List<PrimaryKeyColumnList> primaryKeyColumnList = new ArrayList<>();
		for (jp.co.dk.document.Element primarykeyColumnSettingElement : this.getChildElement(new ElementName() {
			@Override
			public String getName() {
				return "primarykey_column_list";
			}
		})){
			primaryKeyColumnList.add(new PrimaryKeyColumnList(primarykeyColumnSettingElement));
		}
		return primaryKeyColumnList;
	}
}


class File {
	
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

class ColumnSetting {
	
	protected jp.co.dk.document.Element columnSettingElement;
	
	protected List<jp.co.dk.document.Element> columnList;
	
	ColumnSetting(jp.co.dk.document.Element columnSettingElement) {
		this.columnSettingElement = columnSettingElement;
		this.columnList           = this.columnSettingElement.getChildElement(new ElementName() {
			@Override
			public String getName() {
				return "column";
			}
		});
	}
	
	String convertContent(String beforeConvert, String type) throws DataStoreManagerException {
		for (jp.co.dk.document.Element column : this.columnList) {
			if (type.equals(column.getAttribute("type"))) {
				return beforeConvert.replaceAll("\\$\\{" + "column_setting.class" + "\\}", column.getAttribute("class"));
			}
		}
		throw new DataStoreManagerException(DataStoreManagerMessage.COLUMN_IS_NOT_SET_IN_TEMPLATE, type);
	}
}

class PrimaryKeyColumnList {

	protected jp.co.dk.document.Element primaryKeyColumnListElement;
	
	protected String separator;
	
	PrimaryKeyColumnList(jp.co.dk.document.Element primaryKeyColumnListElement) {
		this.primaryKeyColumnListElement = primaryKeyColumnListElement;
		String separator = this.primaryKeyColumnListElement.getAttribute("separator");
		if (separator == null || separator.isEmpty()) {
			this.separator = "";
		} else {
			this.separator = separator;
		}
	}
	
	String getPrimaryKeyColumnContent(ColumnSetting columnSetting, List<ColumnMetaData> columnMetaDataList) throws DataStoreManagerException {
		StringJoiner primaryKeyListStr = new StringJoiner(this.separator);
		String       content           = this.primaryKeyColumnListElement.getContent();
		for (ColumnMetaData columnMetaData : columnMetaDataList) {
			if (columnMetaData.isPrimaryKey()) {
				for (ColumnVariable columnVariable : ColumnVariable.values()) {
					content = columnVariable.convertContent(content, columnMetaData);
				}
				content = columnSetting.convertContent(content, columnMetaData.getColumnType());
				primaryKeyListStr.add(content);
			}
		}
		return primaryKeyListStr.toString();
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



