package jp.co.dk.datastoremanager.command.daogen;

import java.util.List;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage;
import jp.co.dk.document.ElementName;

public class ColumnSetting {
	
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
