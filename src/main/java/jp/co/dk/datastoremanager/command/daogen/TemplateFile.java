package jp.co.dk.datastoremanager.command.daogen;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.document.ElementName;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.xml.XmlDocument;

public class TemplateFile extends XmlDocument {
	
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
}
