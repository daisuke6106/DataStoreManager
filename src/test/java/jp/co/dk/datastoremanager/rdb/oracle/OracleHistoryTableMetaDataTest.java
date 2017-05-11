package jp.co.dk.datastoremanager.rdb.oracle;

import java.util.Date;
import java.util.List;

import jp.co.dk.datastoremanager.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.history.HistoryTableMetaData;
import jp.co.dk.datastoremanager.rdb.history.HistoryTableRecordList;
import jp.co.dk.datastoremanager.rdb.history.HistoryTableTmpRecord;

import org.junit.Before;
import org.junit.Test;

public class OracleHistoryTableMetaDataTest extends DataStoreManagerTestFoundation{
	
	protected HistoryTableMetaData target;

	@Before
 	public void init() throws DataStoreManagerException {
		OracleDataBaseDataStore dbs = new OracleDataBaseDataStore(this.getAccessableDataBaseAccessParameterORACLE());
		dbs.startTransaction();
		
		OracleTableMetaData tableMetaData= (OracleTableMetaData)dbs.getTable("EMP");
		tableMetaData.createHistoryTable();
		tableMetaData.createTriggerHistoryTable();
		
		this.target = tableMetaData.getHistoryTable();
		
	}
	
	@Test
	public void getHistoryTable() throws DataStoreManagerException {
		HistoryTableRecordList result = this.target.getRecordAfterSpecifiedDate(new Date(0L));
		System.out.print(result);
	}
}