package jp.co.dk.datastoremanager.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	DataStoreKindTest.class,
	DataStoreManagerTest.class,
	DataStoreParameterTest.class,
	
	jp.co.dk.datastoremanager.core.gdb.AllTest.class,
	jp.co.dk.datastoremanager.core.rdb.AllTest.class,
	
	jp.co.dk.datastoremanager.core.exception.AllTest.class,
	jp.co.dk.datastoremanager.core.property.AllTest.class,
})
public class AllTest {}
 