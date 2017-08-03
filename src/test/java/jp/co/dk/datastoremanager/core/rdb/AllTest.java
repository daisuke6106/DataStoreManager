package jp.co.dk.datastoremanager.core.rdb;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	DataBaseDriverConstantsTest.class,
	DataBaseDataStoreTest.class,
	TransactionTest.class,
	SqlTest.class,
	
	DataBaseRecordTest.class,
	
	StringSqlParameterTest.class,
	IntSqlParameterTest.class,
//	BytesSqlParameterTest.class,
	DataBaseAccessParameterTest.class,
//	TimestampSqlParameterTest.class,
//	DateSqlParameterTest.class,
//	ObjectSqlParameterTest.class,
	
})
public class AllTest {}
 