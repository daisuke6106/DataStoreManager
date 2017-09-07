package jp.co.dk.datastoremanager.core.rdb.postgressql;

import java.sql.ResultSet;
import java.util.Date;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.rdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.core.rdb.DataBaseRecord;
import jp.co.dk.datastoremanager.core.rdb.TableMetaData;

public class PostgresSqlDataBaseDataStore extends DataBaseDataStore {

	public PostgresSqlDataBaseDataStore(DataBaseAccessParameter dataBaseAccessParameter) {
		super(dataBaseAccessParameter);
	}

	@Override
	protected TableMetaData createTableMetaData(DataBaseDataStore dataBaseDataStore, String schma, String tableName) {
		return null;
	}

	@Override
	protected DataBaseRecord createDataBaseRecord(ResultSet resultSet) {
		return null;
	}

	@Override
	public Date getDataBaseTime() throws DataStoreManagerException {
		// TODO Auto-generated method stub
		return null;
	}
}

//  lk;++zxzzzzbvn yumlc                gf ,kn....... nm,. 