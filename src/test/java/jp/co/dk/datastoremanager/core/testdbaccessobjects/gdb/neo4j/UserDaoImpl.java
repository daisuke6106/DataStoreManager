package jp.co.dk.datastoremanager.core.testdbaccessobjects.gdb.neo4j;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import jp.co.dk.datastoremanager.core.DataStore;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.gdb.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.core.gdb.Cypher;
import jp.co.dk.datastoremanager.core.gdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.testdbaccessobjects.gdb.UserNode;

public class UserDaoImpl extends AbstractDataBaseAccessObject implements UsersDao{

	public UserDaoImpl(DataBaseAccessParameter dataBaseAccessParameter) throws DataStoreManagerException {
		super(dataBaseAccessParameter);
	}
	
	public UserDaoImpl(DataStore dataStore) throws DataStoreManagerException {
		super(dataStore);
	}
	
	@Override
	public UserNode select(String stringData) throws DataStoreManagerException {
		Cypher cypher = new Cypher("MATCH (user:User{name:{1}}) RETURN user");
		cypher.setParameter(stringData);
		return executeWithRetuen(cypher, new UserNode()).get(0);
	}

	@Override
	public void insert(String stringData, int intData, long longData, Date dateData, Timestamp timestampData, byte[] byteDate, Serializable objectData, Object convertByteData) throws DataStoreManagerException {
		Cypher cypher = new Cypher("CREATE (User{name:{1},age:{2},sex:{3}})");
		cypher.setParameter(stringData);
		cypher.setParameter(intData);
		cypher.setParameter(true);
		execute(cypher);
	}

	@Override
	public int update(String stringData, int intData) throws DataStoreManagerException {
		Cypher cypher = new Cypher("MATCH (user:User{name:{1}}) SET user.age = {2}");
		cypher.setParameter(stringData);
		cypher.setParameter(intData);
		return 0;
	}

	@Override
	public int delete(String stringData) throws DataStoreManagerException {
		Cypher cypher = new Cypher("MATCH (user:User{name:{1}}) REMOVE user");
		cypher.setParameter(stringData);
		return 0;
	}
}
