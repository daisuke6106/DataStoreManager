package jp.co.dk.datastoremanager.rdb.oracle;

import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jp.co.dk.datastoremanager.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.TableMetaData;
import jp.co.dk.datastoremanager.rdb.history.HistoryTableMetaData;
import jp.co.dk.datastoremanager.rdb.history.HistoryTableRecordList;
import jp.co.dk.datastoremanager.rdb.oracle.OracleDataBaseDataStore;
import jp.co.dk.datastoremanager.rdb.oracle.OracleTableMetaData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	public void getTableMetaData() throws DataStoreManagerException {
		TableMetaData result = this.target.getTableMetaData();
		assertThat(result.getHistoryTableName(), is("H$ALL_TYPE_COLUMNS"));
	}
	
	@Test
	public void getRecordAfterSpecifiedDate() throws DataStoreManagerException {
		// レコードをINSERT
		OracleDataBaseDataStore dbs = new OracleDataBaseDataStore(this.getAccessableDataBaseAccessParameterORACLE());
		dbs.startTransaction();
		dbs.insert(this.insertAllTypeColumnsTableSql());
		dbs.commit();
		HistoryTableRecordList result = this.target.getRecordAfterSpecifiedDate(new Date(0L));
		assertThat(result, notNullValue());
	}
	
	@Test
	public void createTrHeader() throws DataStoreManagerException, ParserConfigurationException {
		// レコードをINSERT
		OracleDataBaseDataStore dbs = new OracleDataBaseDataStore(this.getAccessableDataBaseAccessParameterORACLE());
		dbs.startTransaction();
		dbs.insert(this.insertAllTypeColumnsTableSql());
		dbs.commit();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder        docbuilder             = documentBuilderFactory.newDocumentBuilder();
		Document               document               = docbuilder.newDocument();
		Element tr = this.target.createTrHeader(document);
		assertThat(tr, notNullValue());
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