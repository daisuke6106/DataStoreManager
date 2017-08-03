package jp.co.dk.datastoremanager.core.rdb.oracle;

import java.util.List;

import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.TableMetaData;
import jp.co.dk.datastoremanager.core.rdb.Transaction;
import jp.co.dk.datastoremanager.core.rdb.oracle.OracleDataBaseDataStore;
import jp.co.dk.datastoremanager.core.rdb.oracle.OracleTransaction;

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

