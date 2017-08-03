package jp.co.dk.datastoremanager.core.gdb;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	CypherTest.class,
	BooleanCypherParameterTest.class,
	NumericCypherParameterTest.class,
//	StringCypherParameterTest.class,
	DataBaseDataStoreTest.class,
	TransactionTest.class,
})
public class AllTest {}
 