# -*- coding: utf-8 -*-
import sys
sys.path.append("datastoremanager_1.2.4_all.jar")

import jp.co.dk.datastoremanager.core.rdb.oracle.OracleDataBaseDataStore as OracleDataBaseDataStore
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter as DataBaseAccessParameter
import jp.co.dk.datastoremanager.core.DataStoreKind as DataStoreKind
import jp.co.dk.datastoremanager.core.DataBaseDriverConstants as DataBaseDriverConstants
import jp.co.dk.datastoremanager.core.rdb.TableMetaData as TableMetaData


def daogen( datastore ):
	

if __name__ == "__main__":
	param = DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "jdbc:oracle:thin:@192.168.42.194:1521:XE", "usr01", "12345")
	dataStore = OracleDataBaseDataStore(param)
	dataStore.startTransaction()
	print(dataStore.getTables()[0])
	sys.exit()
	