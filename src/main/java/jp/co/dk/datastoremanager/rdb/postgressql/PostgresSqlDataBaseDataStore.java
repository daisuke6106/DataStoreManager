package jp.co.dk.datastoremanager.rdb.postgressql;

import java.sql.ResultSet;

import jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.rdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.rdb.DataBaseRecord;
import jp.co.dk.datastoremanager.rdb.TableMetaData;

public class PostgresSqlDataBaseDataStore extends DataBaseDataStore {

	public PostgresSqlDataBaseDataStore(DataBaseAccessParameter dataBaseAccessParameter) {
		super(dataBaseAccessParameter);
	}

	@Override
	protected TableMetaData createTableMetaData(
			DataBaseDataStore dataBaseDataStore, String schma, String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected DataBaseRecord createDataBaseRecord(ResultSet resultSet) {
		// TODO Auto-generated method stub
		return null;
	}
}

//  lk;++zxzzzzbvn yumlc                gf ,kn....... nm,. 