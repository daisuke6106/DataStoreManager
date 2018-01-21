// cd /home/dk/git/DataStoreManager
// jshell -s --execution local --class-path datastoremanager_1.2.4_all.jar:./properties/:./messages/ -J-Dlogger_property=/home/dk/git/DataStoreManager/properties/Logger.properties
// jshell -s --execution local --class-path datastoremanager_1.2.4_all.jar:./properties/:./messages/ 

// 実行 =====================================================================================
import jp.co.dk.datastoremanager.core.rdb.oracle.OracleDataBaseDataStore;
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.DataStoreKind;
import jp.co.dk.datastoremanager.core.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.core.rdb.oracle.OracleDataBaseDataStore;
import jp.co.dk.datastoremanager.core.rdb.TableMetaData;

// 環境定義 =================================================================================
String url  = "jdbc:oracle:thin:@192.168.1.151:1521:XE";
String user = "usr01";
String pass = "12345";

DataBaseAccessParameter param = new DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, url, user, pass);
OracleDataBaseDataStore dataStore = new OracleDataBaseDataStore(param);
dataStore.startTransaction();
List<TableMetaData> tables = dataStore.getTables();
for (TableMetaData table : tables) System.out.println( table.toString() );


