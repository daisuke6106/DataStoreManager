import jp.co.dk.datastoremanager.core.rdb.oracle.OracleDataBaseDataStore
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter
import jp.co.dk.datastoremanager.core.DataStoreKind
import jp.co.dk.datastoremanager.core.DataBaseDriverConstants
import jp.co.dk.datastoremanager.core.rdb.oracle.*
import jp.co.dk.datastoremanager.core.rdb.TableMetaData

println("$1")

// 環境定義 =================================================================================
// val url  = "jdbc:oracle:thin:@192.168.42.194:1521:XE"
// val user = "usr01"
// val pass = "12345"
// 
// val param = DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, url, user, pass)
// val dataStore = OracleDataBaseDataStore(param)
// dataStore.startTransaction()
// val tables : List<TableMetaData> = dataStore.getTables()
// println(tables[0])
// 
// fun pprint(args: String) {
//     println("Hello, world!")
// }
// pprint("aaa")


// cd /home/dk/git/DataStoreManager
// kotlinc -cp ./datastoremanager_1.2.4_all.jar:./properties/:./messages/ -Dlogger_property_file=/home/dk/git/DataStoreManager/properties/Logger.properties
// kotlinc -script daogen.kts -cp ./datastoremanager_1.2.4_all.jar:./properties/:./messages/ -Dlogger_property_file=/home/dk/git/DataStoreManager/properties/Logger.properties

// javaClass.classLoader
// javaClass.classLoader.getResource("Logger.properties")
// Thread.currentThread().getContextClassLoader()

	