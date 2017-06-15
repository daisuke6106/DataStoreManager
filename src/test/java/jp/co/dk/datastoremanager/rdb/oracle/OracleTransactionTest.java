package jp.co.dk.datastoremanager.rdb.oracle;

import static org.junit.Assert.*;

import java.util.List;

import jp.co.dk.datastoremanager.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.TableMetaData;
import jp.co.dk.datastoremanager.rdb.oracle.OracleDataBaseDataStore;
import jp.co.dk.datastoremanager.rdb.oracle.OracleTableMetaData;

import org.junit.Before;
import org.junit.Test;

public class OracleTransactionTest extends DataStoreManagerTestFoundation{
	
	protected OracleDataBaseDataStore target;

	@Before
	public void init() throws DataStoreManagerException {
		this.target = new OracleDataBaseDataStore(this.getAccessableDataBaseAccessParameterORACLE());
	}
	
	@Test
	public void createTableMetaData() throws DataStoreManagerException {
		List<TableMetaData> tableMetaDataList = this.target.getTables();
		for (TableMetaData tableMetaData : tableMetaDataList) {
			assertEquals((tableMetaData instanceof OracleTableMetaData), true);
		}
	}

}
