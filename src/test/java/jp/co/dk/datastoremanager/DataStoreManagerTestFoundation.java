package jp.co.dk.datastoremanager;

import java.text.ParseException;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.Sql;
import jp.co.dk.test.template.TestCaseTemplate;

public class DataStoreManagerTestFoundation extends TestCaseTemplate{
	
	/**
	 * アクセス可能なDBアクセスパラメータを設定したDataBaseAccessParameterを返却します。
	 * 
	 * @return DBアクセスパラメータ
	 * @throws DataStoreManagerException 引数が不足していた場合
	 */
	protected jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter getAccessableDataBaseAccessParameterORACLE() throws DataStoreManagerException {
		return new jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "jdbc:oracle:thin:@192.168.1.10:1521:XE", "usr01", "12345");
	}
	
	/**
	 * アクセス不可能なDBアクセスパラメータを設定したDataBaseAccessParameterを返却します。
	 * 
	 * @return DBアクセスパラメータ
	 * @throws DataStoreManagerException 引数が不足していた場合
	 */
	protected jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter getAccessFaileDataBaseAccessParameterORACLE() throws DataStoreManagerException {
		return new jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "255.255.255.255:3306", "test_user", "123456");
	}
	
	/**
	 * アクセス可能なDBアクセスパラメータを設定したDataBaseAccessParameterを返却します。
	 * 
	 * @return DBアクセスパラメータ
	 * @throws DataStoreManagerException 引数が不足していた場合
	 */
	protected jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter getAccessableDataBaseAccessParameterMYSQL() throws DataStoreManagerException {
		return new jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter(DataStoreKind.MYSQL, DataBaseDriverConstants.MYSQL, "jdbc:mysql://localhost:3306/test_db?useUnicode=true&characterEncoding=UTF-8", "test_user", "123456");
	}
	
	/**
	 * アクセス不可能なDBアクセスパラメータを設定したDataBaseAccessParameterを返却します。
	 * 
	 * @return DBアクセスパラメータ
	 * @throws DataStoreManagerException 引数が不足していた場合
	 */
	protected jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter getAccessFaileDataBaseAccessParameterMYSQL() throws DataStoreManagerException {
		return new jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter(DataStoreKind.MYSQL, DataBaseDriverConstants.MYSQL, "255.255.255.255:3306", "test_user", "123456");
	}
	
	/**
	 * アクセス可能なDBアクセスパラメータを設定したDataBaseAccessParameterを返却します。
	 * 
	 * @return DBアクセスパラメータ
	 * @throws DataStoreManagerException 引数が不足していた場合
	 */
	protected jp.co.dk.datastoremanager.gdb.DataBaseAccessParameter getAccessableDataBaseAccessParameterGDB() throws DataStoreManagerException {
		return new jp.co.dk.datastoremanager.gdb.DataBaseAccessParameter(DataStoreKind.NEO4J, DataBaseDriverConstants.NEO4J, "localhost:7474");
	}
	
	/**
	 * アクセス不可能なDBアクセスパラメータを設定したDataBaseAccessParameterを返却します。
	 * 
	 * @return DBアクセスパラメータ
	 * @throws DataStoreManagerException 引数が不足していた場合
	 */
	protected jp.co.dk.datastoremanager.gdb.DataBaseAccessParameter getAccessFaileDataBaseAccessParameterGDB() throws DataStoreManagerException {
		return new jp.co.dk.datastoremanager.gdb.DataBaseAccessParameter(DataStoreKind.NEO4J, DataBaseDriverConstants.NEO4J, "255.255.255.255:7474");
	}
	
	public Sql createTableSql() throws DataStoreManagerException {
		return new Sql("CREATE TABLE TEST_USERS( USERID VARCHAR(10) NOT NULL PRIMARY KEY, AGE INT(3), BIRTHDAY DATE );");
	}
	
	public Sql insertSql_1234567890() throws DataStoreManagerException, ParseException {
		Sql insertSql  = new Sql("INSERT INTO TEST_USERS( USERID, AGE, BIRTHDAY ) VALUES (?, ?, ?)");
		insertSql.setParameter("1234567890");
		insertSql.setParameter(20);
		insertSql.setParameter(super.createDateByString("20130101000000"));
		return insertSql;
	}
	
	public Sql updateSql_1234567890_to_0987654321() throws DataStoreManagerException, ParseException {
		Sql uptedaSql  = new Sql("UPDATE TEST_USERS SET USERID=?, AGE=?, BIRTHDAY=? WHERE USERID=?");
		uptedaSql.setParameter("0987654321");
		uptedaSql.setParameter(21);
		uptedaSql.setParameter(super.createDateByString("20130102000000"));
		uptedaSql.setParameter("1234567890");
		return uptedaSql;
	}
	
	public Sql updateFaileSql() throws DataStoreManagerException, ParseException {
		Sql uptedaSql  = new Sql("UPDATE TEST_USERS_ SET USERID=?, AGE=?, BIRTHDAY=? WHERE USERID=?");
		uptedaSql.setParameter("0987654321");
		uptedaSql.setParameter(21);
		uptedaSql.setParameter(super.createDateByString("20130102000000"));
		uptedaSql.setParameter("1234567890");
		return uptedaSql;
	}
	
	public Sql selectSql_1234567890() throws DataStoreManagerException {
		Sql selectSql  = new Sql("SELECT * FROM TEST_USERS WHERE USERID=?");
		selectSql.setParameter("1234567890");
		return selectSql;
	}
	
	public Sql selectCountSql_1234567890() throws DataStoreManagerException {
		Sql selectSql  = new Sql("SELECT COUNT(*) AS CNT FROM TEST_USERS WHERE USERID=?");
		selectSql.setParameter("1234567890");
		return selectSql;
	}
	
	public Sql selectSql_0987654321() throws DataStoreManagerException {
		Sql selectSql  = new Sql("SELECT * FROM TEST_USERS WHERE USERID=?");
		selectSql.setParameter("0987654321");
		return selectSql;
	}
	
	public Sql selectCountSql_0987654321() throws DataStoreManagerException {
		Sql selectSql  = new Sql("SELECT COUNT(*) AS CNT FROM TEST_USERS WHERE USERID=?");
		selectSql.setParameter("0987654321");
		return selectSql;
	}
	
	public Sql selectFaileSql() throws DataStoreManagerException {
		Sql selectSql  = new Sql("SELECT * FROM TEST_USERS_ WHERE USERID=?");
		selectSql.setParameter("1234567890");
		return selectSql;
	}
	
	public Sql deleteSql_1234567890() throws DataStoreManagerException {
		Sql deleteSql  = new Sql("DELETE FROM TEST_USERS WHERE USERID=?");
		deleteSql.setParameter("1234567890");
		return deleteSql;
	}
	
	public Sql deleteSql_0987654321() throws DataStoreManagerException {
		Sql deleteSql  = new Sql("DELETE FROM TEST_USERS WHERE USERID=?");
		deleteSql.setParameter("0987654321");
		return deleteSql;
	}
	
	public Sql deleteFaileSql() throws DataStoreManagerException {
		Sql deleteSql  = new Sql("DELETE FROM TEST_USERS_ WHERE USERID=?");
		deleteSql.setParameter("1234567890");
		return deleteSql;
	}
	
	public Sql dropTableSql() throws DataStoreManagerException {
		return new Sql("DROP TABLE TEST_USERS");
	}
	
	
	public Sql createAllTypeColumnsTableSql() throws DataStoreManagerException {
		Sql sql = new Sql("");
		sql.add("CREATE TABLE ALL_TYPE_COLUMNS (");
		// ==== 文字列 ====
		sql.add(" COL_VARCHAR2  VARCHAR2(10),");      // 可変長の文字列
		sql.add(" COL_NVARCHAR2 NVARCHAR2(10),");     // 可変長のUnicode文字列
		sql.add(" COL_CHAR      CHAR(10),");          // 固定長の文字列
		sql.add(" COL_NCHAR     NCHAR(10),");         // 固定長のUnicode文字列
		// sql.add(" COL_LONG      LONG,");
		
		// 数値
		sql.add(" COL_NUMBER    NUMBER(10, 5),");     // 数値
		sql.add(" COL_BINARY_FLOAT  BINARY_FLOAT,");  // 単精度浮動小数点数
		sql.add(" COL_BINARY_DOUBLE BINARY_DOUBLE,"); // 倍精度浮動小数点数
		
		// 日付と時刻
		sql.add(" COL_DATE DATE,");                   // 日付と時刻
		sql.add(" COL_TIMESTAMP TIMESTAMP,");         // 日付と時刻(ミリ秒)
		// sql.add(" COL_TIMESTAMP_WITH_TIMEZONE       TIMESTAMP WITH TIMEZONE,");       // 
		// sql.add(" COL_TIMESTAMP_WITH_LOCAL_TIMEZONE TIMESTAMP WITH LOCAL TIMEZONE,"); // 
		sql.add(" COL_INTERVAL_YEAR_TO_MONTH INTERVAL YEAR TO MONTH,"); // 2つの日付の差分
		sql.add(" COL_INTERVAL_DAY_TO_SECOND INTERVAL DAY TO SECOND,"); // 2つの日付と時刻の差分
		
		// バイナリ
		sql.add(" COL_RAW RAW(10),");                 // バイナリデータ
		sql.add(" COL_LONG_RAW LONG RAW,");           // 可変長のバイナリデータ
		
		// ラージオブジェクト
		sql.add(" COL_CLOB CLOB,");                   // キャラクタ型ラージオブジェクト
		sql.add(" COL_NCLOB NCLOB,");                 // Unicodeキャラクタ型ラージオブジェクト
		sql.add(" COL_BLOB BLOB,");                   // バイナリ型ラージオブジェクト
		
		// その他
		// sql.add(" COL_ROWID ROWID,");              // 行識別子
		sql.add(" COL_BFILE BFILE");                  // データベース外のバイナリファイル
		sql.add(")");
		
		// 参考：http://itref.fc2web.com/oracle/data-type.html
		return sql;
	}
	
	public Sql insertAllTypeColumnsTableSql() throws DataStoreManagerException {
		Sql sql = new Sql("");
		sql.add("INSERT INTO ALL_COLUMNS VALUES(");
		sql.add("'ABCDEFGHIJ',");                                                        // VARCHAR2 
		sql.add("'abcdefghij',");                                                        // NVARCHAR2
		sql.add("'ABCDEFGHIJ',");                                                        // CHAR     
		sql.add("'abcdefghij',");                                                        // NCHAR    
		sql.add("12345.123,");                                                           // NUMBER
		sql.add("12345.123,");                                                           // BINARY_FLOAT 
		sql.add("12345.123,");                                                           // BINARY_DOUBLE
		sql.add("to_date('2006/02/21 15:35:23','yyyy/mm/dd hh24:mi:ss'),");              // DATE
		sql.add("to_timestamp('2006/02/21 15:35:23.556','yyyy/mm/dd hh24:mi:ss.ff3'),"); // TIMESTAMP
		sql.add("null,");                                                                // INTERVAL YEAR TO MONTH
		sql.add("null,");                                                                // INTERVAL DAY TO SECOND
		sql.add("HEXTORAW('3E00210102CDA000C9'),");                                      // RAW
		sql.add("HEXTORAW('3E00210102CDA000C9'),");                                      // LONG RAW
		sql.add("UTL_RAW.CAST_TO_RAW('太郎'),");                                         // CLOB
		sql.add("TO_NCLOB('太郎') ,");                                                   // NCLOB
		sql.add("UTL_RAW.CAST_TO_RAW('太郎'),");                                         // BLOB
		sql.add("null");                                                                 // BFILE
		sql.add(")");
		return sql;
	}
	
	public Sql dropAllTypeColumnsTableSql() throws DataStoreManagerException {
		return new Sql("DROP TABLE ALL_TYPE_COLUMNS");
	}
}
