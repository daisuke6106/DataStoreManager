package jp.co.dk.datastoremanager.rdb.mysql;

import java.sql.ResultSet;

import jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.rdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.rdb.DataBaseRecord;
import jp.co.dk.datastoremanager.rdb.TableMetaData;

public class MySqlDataBaseDataStore extends DataBaseDataStore {

	public MySqlDataBaseDataStore(DataBaseAccessParameter dataBaseAccessParameter) {
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
}

//  lk;++zxzzzzbvn yumlc                gf ,kn....... nm,. 