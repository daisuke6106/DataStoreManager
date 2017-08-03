package jp.co.dk.datastoremanager.core;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.core.DataStore;
import jp.co.dk.datastoremanager.core.DataStoreKind;
import jp.co.dk.datastoremanager.core.DataStoreParameter;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.test.template.TestCaseTemplate;

import org.junit.Test;

public class DataStoreParameterTest extends TestCaseTemplate {
	
	@Test
	public void constractor_success01() throws DataStoreManagerException{
		new FakeDataStoreParameter(DataStoreKind.ORACLE);
	}
	
	@Test
	public void constractor_error01(){
		try {
			new FakeDataStoreParameter(null);
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DATA_STORE_KIND_IS_NOT_SET);
		}
	}
	
	@Test
	public void test_equals() {
		try {
			FakeDataStoreParameter target_01 = new FakeDataStoreParameter(DataStoreKind.ORACLE);
			FakeDataStoreParameter target_02 = new FakeDataStoreParameter(DataStoreKind.ORACLE);
			FakeDataStoreParameter target_03 = new FakeDataStoreParameter(DataStoreKind.ORACLE);
			
			List<Object> faileList = new ArrayList<Object>();
			faileList.add(new FakeDataStoreParameter(DataStoreKind.MYSQL));
			faileList.add(new FakeDataStoreParameter(DataStoreKind.POSTGRESQL));
			faileList.add(new FakeDataStoreParameter(DataStoreKind.CSV));
			
			testEquals(target_01, target_02, target_03, faileList);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
}

class FakeDataStoreParameter extends DataStoreParameter {

	protected FakeDataStoreParameter(DataStoreKind dataStoreKind) throws DataStoreManagerException {
		super(dataStoreKind);
	}

	@Override
	public DataStore createDataStore() {
		return null;
	}

	@Override
	public String toString() {
		return this.dataStoreKind.toString();
	}
	
}