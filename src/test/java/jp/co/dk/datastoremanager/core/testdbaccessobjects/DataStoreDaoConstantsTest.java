package jp.co.dk.datastoremanager.core.testdbaccessobjects;

import jp.co.dk.datastoremanager.core.DaoConstants;
import jp.co.dk.datastoremanager.core.DataAccessObject;
import jp.co.dk.datastoremanager.core.DataAccessObjectFactory;
import jp.co.dk.datastoremanager.core.DataStore;
import jp.co.dk.datastoremanager.core.DataStoreKind;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.testdbaccessobjects.rdb.mysql.UserDaoImpl;
import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

public enum DataStoreDaoConstantsTest implements DaoConstants{
	USERS("USERS", new DataAccessObjectFactory() {
		@Override
		public DataAccessObject getDataAccessObject(DataStoreKind dataStoreKind, DataStore dataStore) throws DataStoreManagerException{
			switch (dataStoreKind) {
			case ORACLE:
			case MYSQL:
			case POSTGRESQL:
				return new UserDaoImpl(dataStore);
			case NEO4J:
				return new UserDaoImpl(dataStore);
			default:
				throw new DataStoreManagerException(FAILE_TO_CREATE_DATA_ACCESS_OBJECT);
			}
		}
	}),
	;

	private String name;
	
	private DataAccessObjectFactory factory;
	
	private DataStoreDaoConstantsTest(String name, DataAccessObjectFactory factory) {
		this.name = name;
		this.factory = factory;
	}
	
	@Override
	public DataAccessObjectFactory getDataAccessObjectFactory() {
		return this.factory;
	}

	@Override
	public String getName() {
		return this.name;
	}

}
