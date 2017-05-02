package jp.co.dk.datastoremanager.rdb.oracle;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.ColumnMetaData;
import jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.rdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.rdb.Sql;
import jp.co.dk.datastoremanager.rdb.TableMetaData;
import jp.co.dk.datastoremanager.rdb.Transaction;

public class OracleDataBaseDataStore extends DataBaseDataStore {

	public OracleDataBaseDataStore(DataBaseAccessParameter dataBaseAccessParameter) {
		super(dataBaseAccessParameter);
	}
	
	@Override
	protected Transaction createTransaction(DataBaseAccessParameter dataBaseAccessParameter) throws DataStoreManagerException {
		return new OracleTransaction(dataBaseAccessParameter);
	}
}

class OracleTransaction extends Transaction {

	OracleTransaction(DataBaseAccessParameter dataBaseAccessParameter) throws DataStoreManagerException {
		super(dataBaseAccessParameter);
	}
	
	@Override
	protected TableMetaData createTableMetaData(Transaction transaction, String schma, String tableName) {
		return new OracleTableMetaData(transaction, schma, tableName);
	}
}

class OracleTableMetaData extends TableMetaData {

	OracleTableMetaData(Transaction transaction, String schemaName, String tableName) {
		super(transaction, schemaName, tableName);
	}

	@Override
	public boolean isExistsHistoryTable() throws DataStoreManagerException {
		return this.transaction.isExistsTable(this.getHistoryTableName());
	}
	
	@Override
	public boolean createHistoryTable() throws DataStoreManagerException {
		if (this.isExistsHistoryTable()) return false;
		Sql sql = new Sql("CREATE TABLE ");
		sql.add(this.getHistoryTableName()).add(" AS ");
		sql.add("SELECT SYSDATE AS OPTM, '  ' AS OPTP, ");
		sql.add(this.tableName).add(".*").add(" FROM ").add(this.tableName);
		sql.add(" WHERE ").add("ROWNUM = 0");
		this.transaction.createTable(sql);
		return true;
	}
	
	@Override
	public boolean dropHistoryTable() throws DataStoreManagerException {
		if (!this.isExistsHistoryTable()) return false;
		Sql sql = new Sql("DROP TABLE ").add(this.getHistoryTableName());
		this.transaction.dropTable(sql);
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
		sql.add("INSERT INTO ").add(this.getHistoryTableName()).add(" VALUES( SYSDATE, ''IS''");
		for (ColumnMetaData column : this.getColumns()) sql.add(", ").add(":NEW.").add(column.getColumnname());
		sql.add(");");
		sql.add("END;'; END;");
		this.transaction.createTable(sql);
	}
	
	protected void createUpdateTrigerForHistoryTable() throws DataStoreManagerException {
		Sql sql = new Sql("BEGIN EXECUTE IMMEDIATE 'CREATE OR REPLACE TRIGGER ").add(this.getHistoryTableName()).add("_UPD_TRG").add(" ");
		sql.add("AFTER UPDATE ON ").add(this.tableName).add(" ").add("FOR EACH ROW ");
		sql.add("BEGIN ");
		sql.add("INSERT INTO ").add(this.getHistoryTableName()).add(" VALUES( SYSDATE, ''UO''");
		for (ColumnMetaData column : this.getColumns()) sql.add(", ").add(":OLD.").add(column.getColumnname());
		sql.add(");");
		sql.add("INSERT INTO ").add(this.getHistoryTableName()).add(" VALUES( SYSDATE, ''UN''");
		for (ColumnMetaData column : this.getColumns()) sql.add(", ").add(":NEW.").add(column.getColumnname());
		sql.add(");");
		sql.add("END;'; END;");
		this.transaction.createTable(sql);
	}
	
	protected void createDeleteTrigerForHistoryTable() throws DataStoreManagerException {
		Sql sql = new Sql("BEGIN EXECUTE IMMEDIATE 'CREATE OR REPLACE TRIGGER ").add(this.getHistoryTableName()).add("_DEL_TRG").add(" ");
		sql.add("AFTER DELETE ON ").add(this.tableName).add(" ").add("FOR EACH ROW ");
		sql.add("BEGIN ");
		sql.add("INSERT INTO ").add(this.getHistoryTableName()).add(" VALUES( SYSDATE, ''DL''");
		for (ColumnMetaData column : this.getColumns()) sql.add(", ").add(":OLD.").add(column.getColumnname());
		sql.add(");");
		sql.add("END;'; END;");
		this.transaction.createTable(sql);
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
		this.transaction.dropTable(sql);
	}
	
	protected void dropUpdateHistoryTrigger() throws DataStoreManagerException {
		Sql sql = new Sql("DROP TRIGGER ").add(this.getHistoryTableName()).add("_UPD_TRG");
		this.transaction.dropTable(sql);
	}
	
	protected void dropDeleteHistoryTrigger() throws DataStoreManagerException {
		Sql sql = new Sql("DROP TRIGGER ").add(this.getHistoryTableName()).add("_DEL_TRG");
		this.transaction.dropTable(sql);
	}
}

//  lk;++zxzzzzbvn yumlc                gf ,kn....... nm,. 