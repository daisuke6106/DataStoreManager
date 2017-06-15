package jp.co.dk.datastoremanager.rdb.oracle;

import jp.co.dk.datastoremanager.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.oracle.OracleDataBaseDataStore;
import jp.co.dk.datastoremanager.rdb.oracle.OracleHistoryTableMetaData;
import jp.co.dk.datastoremanager.rdb.oracle.OracleTableMetaData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsInstanceOf.instanceOf;


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
	public void isExistsHistoryTable() throws DataStoreManagerException {
		// ヒストリテーブルがある状態で実行した場合、true
		this.target.createHistoryTable();
		this.target.createTriggerHistoryTable();
		
		assertThat(this.target.isExistsHistoryTable(), is(true));
		
		// ヒストリテーブルが無い状態で実行した場合、false
		this.target.dropHistoryTrigger();
		this.target.dropHistoryTable();
		
		assertThat(this.target.isExistsHistoryTable(), is(false));
		
	}
	
	@Test
	public void getHistoryTable() throws DataStoreManagerException {
		// ヒストリテーブルがある状態で実行した場合、インスタンスを返却すること。
		this.target.createHistoryTable();
		this.target.createTriggerHistoryTable();
		
		assertThat(this.target.getHistoryTable(), instanceOf(OracleHistoryTableMetaData.class));
		
		// ヒストリテーブルが無い状態で実行した場合、null
		this.target.dropHistoryTrigger();
		this.target.dropHistoryTable();
		
		assertThat(this.target.getHistoryTable(), nullValue());
		
	}
	
	@After
 	public void dest() throws DataStoreManagerException {
		OracleDataBaseDataStore dbs = new OracleDataBaseDataStore(this.getAccessableDataBaseAccessParameterORACLE());
		dbs.startTransaction();
		this.target = (OracleTableMetaData)dbs.getTable("EMP");
		this.target.dropHistoryTrigger();
		this.target.dropHistoryTable();
	}
	
}