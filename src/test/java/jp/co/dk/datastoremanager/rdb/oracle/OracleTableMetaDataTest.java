package jp.co.dk.datastoremanager.rdb.oracle;

import jp.co.dk.datastoremanager.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import org.junit.Before;
import org.junit.Test;

public class OracleTableMetaDataTest extends DataStoreManagerTestFoundation{
	
	protected OracleTableMetaData target;

	@Before
 	public void init() throws DataStoreManagerException {
		OracleDataBaseDataStore dbs = new OracleDataBaseDataStore(this.getAccessableDataBaseAccessParameterORACLE());
		dbs.startTransaction();
		this.target = (OracleTableMetaData)dbs.getTable("EMP");
		this.target.createHistoryTable();
		this.target.createTriggerHistoryTable();
	}
	
	@Test
	public void getHistoryTable() throws DataStoreManagerException {
		this.target.getHistoryTable();
	}
}