package jp.co.dk.datastoremanager.command.daogen;

import java.util.List;

import org.junit.Test;

import jp.co.dk.datastoremanager.core.DataStoreManager;
import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.property.DataStoreManagerProperty;
import jp.co.dk.datastoremanager.core.rdb.DataBaseDataStore;
import jp.co.dk.document.exception.DocumentException;
import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;
import static org.junit.Assert.*;

public class TemplateFileTest extends DataStoreManagerTestFoundation{
	
	@Test
	public void constractor() throws DocumentException {
		TemplateFile target = new TemplateFile(this.getInputStreamByOwnClass("dao_template.xml"));
		System.out.println(target.getTagName());
		for (jp.co.dk.document.Element element : target.getChildElement()) {
			System.out.println(element.getTagName());
		}
	}
}
