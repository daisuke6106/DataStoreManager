package jp.co.dk.datastoremanager.core.rdb.oracle;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.ColumnMetaData;
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.rdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.core.rdb.DataBaseRecord;
import jp.co.dk.datastoremanager.core.rdb.Sql;
import jp.co.dk.datastoremanager.core.rdb.TableMetaData;
import jp.co.dk.datastoremanager.core.rdb.Transaction;
import jp.co.dk.datastoremanager.core.rdb.history.HistoryTableMetaData;
import jp.co.dk.datastoremanager.core.rdb.history.HistoryTableRecordList;
import jp.co.dk.datastoremanager.core.rdb.history.HistoryTableTmpRecord;
import jp.co.dk.datastoremanager.core.rdb.history.OperationType;
import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

public class OracleDataBaseDataStore extends DataBaseDataStore {

	public OracleDataBaseDataStore(DataBaseAccessParameter dataBaseAccessParameter) {
		super(dataBaseAccessParameter);
	}
	
	@Override
	protected Transaction createTransaction(DataBaseAccessParameter dataBaseAccessParameter) throws DataStoreManagerException {
		return new OracleTransaction(dataBaseAccessParameter);
	}

	@Override
	protected TableMetaData createTableMetaData(DataBaseDataStore dataBaseDataStore, String schma, String tableName) {
		return new OracleTableMetaData(dataBaseDataStore, schma, tableName);
	}

	@Override
	protected DataBaseRecord createDataBaseRecord(ResultSet resultSet) {
		return new OracleDataBaseRecord(resultSet);
	}

	@Override
	public Date getDataBaseTime() throws DataStoreManagerException {
		Sql sql = new Sql("SELECT SYSDATE FROM DUAL");
		try {
			ResultSet rs = this.select(sql);
			rs.next();
			return rs.getDate("SYSDATE");
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_GET_SYSDATE, e);
		}
	}
}

class OracleTransaction extends Transaction {

	OracleTransaction(DataBaseAccessParameter dataBaseAccessParameter) throws DataStoreManagerException {
		super(dataBaseAccessParameter);
	}
	
}

class OracleDataBaseRecord extends DataBaseRecord {

	OracleDataBaseRecord(ResultSet resultSet) {
		super(resultSet);
	}

	@Override
	protected ColumnMetaData createColumnMetaData(ResultSetMetaData rs, int i) throws SQLException {
		return new OracleColumnMetaData(rs, i);
	}
	
}

class OracleTableMetaData extends TableMetaData {

	OracleTableMetaData(DataBaseDataStore dataBaseDataStore, String schemaName, String tableName) {
		super(dataBaseDataStore, schemaName, tableName);
	}

	@Override
	public boolean isExistsHistoryTable() throws DataStoreManagerException {
		return this.dataBaseDataStore.isExistsTable(this.getHistoryTableName());
	}
	
	@Override
	public boolean createHistoryTable() throws DataStoreManagerException {
		if (this.isExistsHistoryTable()) return false;
		Sql sql = new Sql("CREATE TABLE ");
		sql.add(this.getHistoryTableName()).add(" AS ");
		sql.add("SELECT SYSDATE AS OPTM, '  ' AS OPTP, ");
		sql.add(this.tableName).add(".*").add(" FROM ").add(this.tableName);
		sql.add(" WHERE ").add("ROWNUM = 0");
		this.dataBaseDataStore.createTable(sql);
		return true;
	}
	
	@Override
	public boolean dropHistoryTable() throws DataStoreManagerException {
		if (!this.isExistsHistoryTable()) return false;
		Sql sql = new Sql("DROP TABLE ").add(this.getHistoryTableName());
		this.dataBaseDataStore.dropTable(sql);
		return true;
	}
	
	@Override
	public boolean createTriggerHistoryTable() throws DataStoreManagerException {
		if (!this.isExistsHistoryTable()) return false;
		this.createInsertTrigerForHistoryTable();
		this.createUpdateTrigerForHistoryTable();
		this.createDeleteTrigerForHistoryTable();
		return true;
	}
	
	protected void createInsertTrigerForHistoryTable() throws DataStoreManagerException {
		// 通常どおりCREATE TRIGGERでトリガーを作成した場合、:NEWのコロン部でエラー発生
		// それを回避する為、CREATE TRIGGER文を文字列化し、その文をPLSQLにて実行することで回避する。
		Sql sql = new Sql("BEGIN EXECUTE IMMEDIATE 'CREATE OR REPLACE TRIGGER ").add(this.getHistoryTableName()).add("_INS_TRG").add(" ");
		sql.add("AFTER INSERT ON ").add(this.tableName).add(" ").add("FOR EACH ROW ");
		sql.add("BEGIN ");
		sql.add("INSERT INTO ").add(this.getHistoryTableName()).add(" VALUES( SYSDATE, ''").add(OperationType.IN).add("''");
		for (ColumnMetaData column : this.getColumns()) sql.add(", ").add(":NEW.").add(column.getColumnname());
		sql.add(");");
		sql.add("END;'; END;");
		this.dataBaseDataStore.createTable(sql);
	}
	
	protected void createUpdateTrigerForHistoryTable() throws DataStoreManagerException {
		Sql sql = new Sql("BEGIN EXECUTE IMMEDIATE 'CREATE OR REPLACE TRIGGER ").add(this.getHistoryTableName()).add("_UPD_TRG").add(" ");
		sql.add("AFTER UPDATE ON ").add(this.tableName).add(" ").add("FOR EACH ROW ");
		sql.add("BEGIN ");
		sql.add("INSERT INTO ").add(this.getHistoryTableName()).add(" VALUES( SYSDATE, ''").add(OperationType.U1).add("''");
		for (ColumnMetaData column : this.getColumns()) sql.add(", ").add(":OLD.").add(column.getColumnname());
		sql.add(");");
		sql.add("INSERT INTO ").add(this.getHistoryTableName()).add(" VALUES( SYSDATE, ''").add(OperationType.U2).add("''");
		for (ColumnMetaData column : this.getColumns()) sql.add(", ").add(":NEW.").add(column.getColumnname());
		sql.add(");");
		sql.add("END;'; END;");
		this.dataBaseDataStore.createTable(sql);
	}
	
	protected void createDeleteTrigerForHistoryTable() throws DataStoreManagerException {
		Sql sql = new Sql("BEGIN EXECUTE IMMEDIATE 'CREATE OR REPLACE TRIGGER ").add(this.getHistoryTableName()).add("_DEL_TRG").add(" ");
		sql.add("AFTER DELETE ON ").add(this.tableName).add(" ").add("FOR EACH ROW ");
		sql.add("BEGIN ");
		sql.add("INSERT INTO ").add(this.getHistoryTableName()).add(" VALUES( SYSDATE, ''").add(OperationType.DL).add("''");
		for (ColumnMetaData column : this.getColumns()) sql.add(", ").add(":OLD.").add(column.getColumnname());
		sql.add(");");
		sql.add("END;'; END;");
		this.dataBaseDataStore.createTable(sql);
	}
	
	@Override
	public boolean dropHistoryTrigger() throws DataStoreManagerException {
		if (!this.isExistsHistoryTable()) return false;
		this.dropInsertHistoryTrigger();
		this.dropUpdateHistoryTrigger();
		this.dropDeleteHistoryTrigger();
		return true;
	}
	
	protected void dropInsertHistoryTrigger() throws DataStoreManagerException {
		Sql sql = new Sql("DROP TRIGGER ").add(this.getHistoryTableName()).add("_INS_TRG");
		this.dataBaseDataStore.dropTable(sql);
	}
	
	protected void dropUpdateHistoryTrigger() throws DataStoreManagerException {
		Sql sql = new Sql("DROP TRIGGER ").add(this.getHistoryTableName()).add("_UPD_TRG");
		this.dataBaseDataStore.dropTable(sql);
	}
	
	protected void dropDeleteHistoryTrigger() throws DataStoreManagerException {
		Sql sql = new Sql("DROP TRIGGER ").add(this.getHistoryTableName()).add("_DEL_TRG");
		this.dataBaseDataStore.dropTable(sql);
	}

	@Override
	public HistoryTableMetaData getHistoryTable() throws DataStoreManagerException {
		if (!this.isExistsHistoryTable()) return null;
		return new OracleHistoryTableMetaData(this);
	}

	@Override
	protected ColumnMetaData createColumnMetaData(ResultSet rs, int i) throws SQLException {
		return new OracleColumnMetaData(rs, i);
	}
}

class OracleHistoryTableMetaData extends HistoryTableMetaData {

	protected OracleHistoryTableMetaData(TableMetaData tableMetaData) {
		super(tableMetaData);
	}
	
	@Override
	public HistoryTableRecordList getRecordAfterSpecifiedDate(Date targetDate) throws DataStoreManagerException {
		Sql sql = new Sql("SELECT * FROM ").add(this.tableMetaData.getHistoryTableName());
		sql.add(" WHERE");
		sql.add(" OPTM >= ?").setParameter(targetDate);
		sql.add(" ORDER BY OPTM ASC, OPTP ASC");
		List<HistoryTableTmpRecord> historyTableRecordList = new jp.co.dk.datastoremanager.core.rdb.AbstractDataBaseAccessObject(
				this.tableMetaData.getDataBaseDataStore()){}.selectMulti(sql, new HistoryTableTmpRecord(this, this.tableMetaData.getColumns()));
		
		return new HistoryTableRecordList(this, historyTableRecordList);
	}
}

class OracleColumnMetaData extends ColumnMetaData {

	protected OracleColumnMetaData(ResultSetMetaData metaData, int index) throws SQLException {
		super(metaData, index);
	}

	protected OracleColumnMetaData(ResultSet resultSet, int index) throws SQLException {
		super(resultSet, index);
	}
	
	@Override
	public Object getData(DataBaseRecord record) throws DataStoreManagerException {
		
		switch (columnIntType) {
			// === CHAR ===
			case java.sql.Types.CHAR:
				return record.getString(columnname);

			// === NCHAR ===
			// Types.NCHAR (12.1.0.2) 
			case java.sql.Types.NCHAR:
			// Types.OTHER (12.1.0.1以前) 
			// case java.sql.Types.OTHER:
				return record.getString(columnname);

			// === VARCHAR2 ===
			case java.sql.Types.VARCHAR:
				return record.getString(columnname);

			// === NVARCHAR2 ===
			// Types.NVARCHAR (12.1.0.2) 
			// case java.sql.Types.NVARCHAR:
			// Types.OTHER (12.1.0.1以前)
			case java.sql.Types.OTHER:
				return record.getString(columnname);
				
			// === NUMBER ===
			case java.sql.Types.DECIMAL:
				return record.getBigDecimal(columnname);

			// === FLOAT ===
			case java.sql.Types.FLOAT:
				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
			
			// === BINARY_FLOAT ===
			case 100:	
				// return record.getBigDecimal(columnname);
				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());

			// === BINARY_DOUBLE ===
			case 101:
				// return record.getBigDecimal(columnname);
				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
			
			// === LONG ===
			case java.sql.Types.LONGVARCHAR:
				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());

			// === DATE/TIMESTAMP ===
			case java.sql.Types.TIMESTAMP:
				return record.getDate(columnname);
			
			// TIMESTAMP WITH TIME ZONE
			case -101:
			// TIMESTAMP WITH LOCAL TIME ZONE
			case -102:
			// INTERVAL YEAR TO MONTH
			case -103:
			// INTERVAL DAY TO SECOND
			case -104:
				// return record.getDate(columnname);
				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
			
			// === RAW ===
			case java.sql.Types.VARBINARY:
				// return record.getBytes(columnname);
				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
				
			// === LONG RAW ===
			case java.sql.Types.LONGVARBINARY:
				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
				
			// === BFILE ===
			case -13:
				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
			
			// === BLOB ===
			case java.sql.Types.BLOB:
				// throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
				return record.getBytes(columnname);
			
			// === CLOB ===
			case java.sql.Types.CLOB:
				return record.getString(columnname);
			
			// === NCLOB ===
			// Types.NCLOB (12.1.0.2) 
			// Types.OTHER (12.1.0.1以前)
			case java.sql.Types.NCLOB:
				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());

			// === ROWID/UROWID ===
			// Types.ROWID (12.1.0.2) 
			// Types.OTHER (12.1.0.1以前)
			case java.sql.Types.ROWID:
				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
				
			default:
				throw new DataStoreManagerException(NOT_SUPPORT, this.toString());
			
			// 参考
			// http://hito4-t.hatenablog.com/entry/2015/03/07/220621
			// http://d.hatena.ne.jp/amutan/20090222/1235300058
		}
	}
	
}

//  lk;++zxzzzzbvn yumlc                gf ,kn....... nm,. 