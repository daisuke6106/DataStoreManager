package jp.co.dk.datastoremanager.core.rdb.history.junit;

import static org.junit.Assert.fail;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.core.rdb.Record;
import jp.co.dk.datastoremanager.core.rdb.TableMetaData;
import jp.co.dk.datastoremanager.core.rdb.history.HistoryTableMetaData;
import jp.co.dk.datastoremanager.core.rdb.history.HistoryTableRecordList;

public class TableMatcher {
	
	protected DataBaseDataStore dataStore;
	
	protected String tableName;
	
	protected HistoryTableMetaData historyTableMetaData;
	
	protected java.util.Date startDate;
	
	private TableMatcher(DataBaseDataStore dataStore, String tableName, java.util.Date startDate) throws DataStoreManagerException {
		this.dataStore = dataStore;
		this.tableName = tableName;
		this.startDate = startDate;
		
		TableMetaData tableMetaData = this.dataStore.getTable( tableName );
		if ( tableMetaData == null ) {
			fail( "Table is not exists. TABLE=[" + tableName + "]" );
		}
		this.historyTableMetaData = tableMetaData.getHistoryTable();
		if ( this.historyTableMetaData == null ) {
			fail( "History Table is not exists. TABLE=[" + tableName + "]" );
		}
		
	}
	
//	public static TableMatcher startMonitoring(DataBaseDataStore dataStore, String tableName) throws DataStoreManagerException {
//		return new TableMatcher( dataStore, tableName, dataStore.getDataBaseTime() );
//	}
	
	public void tablecheck(RecordMatcher[] recordMatchers) throws DataStoreManagerException {
		HistoryTableRecordList historyTableRecordList = this.historyTableMetaData.getRecordAfterSpecifiedDate( this.startDate );
		
	}
}

class RecordMatcher extends BaseMatcher<Record> {
	
	/** テーブル名 */
	protected String tableName;
	
	/** カラム名 */
	protected String columnName;
	
	/** 実測値 */
	protected Object actual;
	
	@Override
	public boolean matches(Object actual) {
		
		return false;
	}

	@Override
	public void describeTo(Description arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}