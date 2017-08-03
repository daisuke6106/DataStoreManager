package jp.co.dk.datastoremanager.core.testdbaccessobjects;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.dk.datastoremanager.core.DataStoreManagerTestFoundation;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.testdbaccessobjects.rdb.UsersRecord;
import jp.co.dk.datastoremanager.core.testdbaccessobjects.rdb.mysql.UserDaoImpl;

import org.junit.Test;

public class DataBaseAccessTest extends DataStoreManagerTestFoundation{
	
	@Test
	public void access() throws DataStoreManagerException {
		UserDaoImpl UserDaoImpl = new UserDaoImpl(super.getAccessableDataBaseAccessParameterMYSQL());
		try {
			//テーブルを作成 
			UserDaoImpl.createTable();
			
			// =========================================================
			// INSERTデータ定義
			String stringData                  = "1234567890";
			int intData                        = 123;
			long longData                      = 1234567890L;
			Date dateData                      = new Date();
			Timestamp timestampData            = new Timestamp(new Date().getTime()) ;
			byte[] bytesData                   = {1,2,3};
			ArrayList<String> objectData       = new ArrayList<String>();
			objectData.add("objectData1");
			objectData.add("objectData2");
			objectData.add("objectData3");
			ArrayList<String> convertBytesData = new ArrayList<String>();
			objectData.add("convertBytesData1");
			objectData.add("convertBytesData2");
			objectData.add("convertBytesData3");
			// =========================================================
			
			// INSERT処理を実行
			UserDaoImpl.insert(stringData, intData, longData, dateData, timestampData, bytesData, objectData, convertBytesData);
			
			// SELECT処理を実行
			UsersRecord userRecord  = UserDaoImpl.select(stringData);
			assertEquals(userRecord.getStringData()   , stringData);
			assertEquals(userRecord.getIntData()      , intData);
			assertEquals(userRecord.getLongData()     , longData);
			assertEquals(super.getStringByDate_YYYYMMDD(userRecord.getDateData()), super.getStringByDate_YYYYMMDD(dateData));
			assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(userRecord.getTimestampData()), super.getStringByDate_YYYYMMDDHH24MMDD(timestampData));
			assertEquals(userRecord.getBytesData(), bytesData);
			List<?> object1 = (ArrayList<?>)userRecord.getSerializableData();
			assertTrue(object1 instanceof ArrayList); 
			assertEquals(object1, objectData);
			List<?> object2 = (ArrayList<?>)userRecord.getObjectData();
			assertTrue(object2 instanceof ArrayList); 
			assertEquals(object2, convertBytesData);
		} finally {
			
			// DROP TABLEを実行
			try {
				UserDaoImpl.dropTable();
			} catch (DataStoreManagerException e) {	}
		}
	}

}
