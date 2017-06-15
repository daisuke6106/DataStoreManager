package jp.co.dk.datastoremanager.rdb.oracle;

import java.util.Date;

import jp.co.dk.datastoremanager.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.history.HistoryTableMetaData;
import jp.co.dk.datastoremanager.rdb.history.HistoryTableRecordList;
import jp.co.dk.datastoremanager.rdb.oracle.OracleDataBaseDataStore;
import jp.co.dk.datastoremanager.rdb.oracle.OracleTableMetaData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OracleHistoryTableMetaDataTest extends DataStoreManagerTestFoundation{
	
	protected HistoryTableMetaData target;

	@Before
 	public void init() throws DataStoreManagerException {
		OracleDataBaseDataStore dbs = new OracleDataBaseDataStore(this.getAccessableDataBaseAccessParameterORACLE());
		dbs.startTransaction();
		dbs.createTable(this.createAllTypeColumnsTableSql());
		
		OracleTableMetaData tableMetaData= (OracleTableMetaData)dbs.getTable("ALL_TYPE_COLUMNS");
		tableMetaData.createHistoryTable();
		tableMetaData.createTriggerHistoryTable();
		
		this.target = tableMetaData.getHistoryTable();
		
		dbs.commit();
	}
	
	@Test
	public void getHistoryTable() throws DataStoreManagerException {
		// レコードをINSERT
		OracleDataBaseDataStore dbs = new OracleDataBaseDataStore(this.getAccessableDataBaseAccessParameterORACLE());
		dbs.startTransaction();
		dbs.insert(this.insertAllTypeColumnsTableSql());
		dbs.commit();
		
		HistoryTableRecordList result = this.target.getRecordAfterSpecifiedDate(new Date(0L));
		
		
	}
	
	@After
	public void fin() throws DataStoreManagerException {
		OracleDataBaseDataStore dbs = new OracleDataBaseDataStore(this.getAccessableDataBaseAccessParameterORACLE());
		dbs.startTransaction();
		
		OracleTableMetaData tableMetaData= (OracleTableMetaData)dbs.getTable("ALL_TYPE_COLUMNS");
		tableMetaData.dropHistoryTrigger();
		tableMetaData.dropHistoryTable();
		
		dbs.dropTable(this.dropAllTypeColumnsTableSql());
		
		dbs.commit();
	}
}