package jp.co.dk.datastoremanager.rdb.oracle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import jp.co.dk.datastoremanager.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.history.HistoryTableMetaData;
import jp.co.dk.datastoremanager.rdb.history.HistoryTableRecordList;
import jp.co.dk.datastoremanager.rdb.oracle.OracleDataBaseDataStore;
import jp.co.dk.datastoremanager.rdb.oracle.OracleTableMetaData;

import org.junit.Before;
import org.junit.Test;

public class OracleHistoryTableRecordListTest extends DataStoreManagerTestFoundation{
	
	protected HistoryTableRecordList target;

	@Before
 	public void init() throws DataStoreManagerException {
		OracleDataBaseDataStore dbs = new OracleDataBaseDataStore(this.getAccessableDataBaseAccessParameterORACLE());
		dbs.startTransaction();
		
		OracleTableMetaData tableMetaData= (OracleTableMetaData)dbs.getTable("EMP");
		tableMetaData.createHistoryTable();
		tableMetaData.createTriggerHistoryTable();
		HistoryTableMetaData historyTableMetaDatat = tableMetaData.getHistoryTable();
		this.target = historyTableMetaDatat.getRecordAfterSpecifiedDate(new Date(0L));
	}
	
	@Test
	public void writeHtml() throws DataStoreManagerException {
		try {
			File file = this.createFileToTmpDir("EMP_history.html");
			OutputStream ops = new FileOutputStream(file);
			this.target.writeHtml(ops);
		} catch (FileNotFoundException e) {
			fail(e);
		}
	}
}