package jp.co.dk.datastoremanager.core;

public interface DataStoreFactory {
	
	DataStore createDataStore(DataStoreParameter dataStoreParameter);
}
