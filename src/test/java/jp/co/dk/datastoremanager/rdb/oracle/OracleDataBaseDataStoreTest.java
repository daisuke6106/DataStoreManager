package jp.co.dk.datastoremanager.rdb.oracle;

import java.util.List;

import jp.co.dk.datastoremanager.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.TableMetaData;
import jp.co.dk.datastoremanager.rdb.Transaction;
import jp.co.dk.datastoremanager.rdb.oracle.OracleDataBaseDataStore;
import jp.co.dk.datastoremanager.rdb.oracle.OracleTransaction;

import org.junit.Before;
import org.junit.Test;

public class OracleDataBaseDataStoreTest extends DataStoreManagerTestFoundation{
	
	protected OracleDataBaseDataStore target;

	@Before
	public void init() throws DataStoreManagerException {
		this.target = new OracleDataBaseDataStore(this.getAccessableDataBaseAccessParameterORACLE());
	}
	
	@Test
	public void createTransaction() throws DataStoreManagerException {
		Transaction transaction = this.target.createTransaction(this.getAccessableDataBaseAccessParameterORACLE());
		assertEquals((transaction instanceof OracleTransaction), true);
	}

}

