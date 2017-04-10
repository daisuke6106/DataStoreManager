package jp.co.dk.datastoremanager.rdb;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.DaoConstants;
import jp.co.dk.datastoremanager.DataAccessObject;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.DataStoreKind;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.logger.Logger;
import jp.co.dk.logger.LoggerFactory;
import static jp.co.dk.datastoremanager.message.DataStoreManagerMessage.*;

/**
 * DataBaseDataStoreは、単一のデータベースのデータストアを表すクラスです。<p/>
 * 単一の接続先に対するトランザクション管理、SQLの実行、実行されたSQLの履歴保持を行う。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class DataBaseDataStore implements DataStore {
	
	/** データベースアクセスパラメータ */
	protected DataBaseAccessParameter dataBaseAccessParameter;
	
	/** トランザクション */
	protected Transaction transaction;
	
	/** SQLリスト */
	protected List<Sql> sqlList = new ArrayList<Sql>();
	
	/** 発生例外一覧 */
	protected List<DataStoreManagerException> exceptionList = new ArrayList<DataStoreManagerException>();
	
	/** ロガーインスタンス */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * コンストラクタ<p/>
	 * 指定のデータベースアクセスパラメータを基にデータベースデータストアを生成します。
	 * 
	 * @param dataBaseAccessParameter データベースアクセスパラメータ
	 */
	public DataBaseDataStore(DataBaseAccessParameter dataBaseAccessParameter) {
		this.logger.constractor(this.getClass(), dataBaseAccessParameter);
		this.dataBaseAccessParameter = dataBaseAccessParameter;
	}
	
	@Override
	public void startTransaction() throws DataStoreManagerException {
		this.transaction = this.createTransaction(dataBaseAccessParameter);
	}

	@Override
	public DataAccessObject getDataAccessObject(DaoConstants daoConstants) throws DataStoreManagerException {
		if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_START);
		DataStoreKind dataStoreKind = this.dataBaseAccessParameter.getDataStoreKind();
		return daoConstants.getDataAccessObjectFactory().getDataAccessObject(dataStoreKind, this);
	}
	

	@Override
	public void commit() throws DataStoreManagerException {
		if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_START);
		this.transaction.commit();
	}

	@Override
	public void rollback() throws DataStoreManagerException {
		if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_START);
		this.transaction.rollback();
	}
	
	@Override
	public void finishTransaction() throws DataStoreManagerException {
		if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_START);
		this.transaction.close();
		this.transaction = null;
	}
	
	@Override
	public boolean isTransaction() {
		if (this.transaction != null) return true;
		return false;
	}
	
	@Override
	public boolean hasError() {
		if (this.exceptionList.size() != 0) return true;
		return false;
	}
	
	public List<TableMetaData> getTable() throws DataStoreManagerException {
		if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_START);
		return this.transaction.getTables();
	}
	
	public TableMetaData getTable(String tableName) throws DataStoreManagerException {
		if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_START);
		List<TableMetaData> tables = this.transaction.getTables();
		for (TableMetaData table : tables) {
			if (table.tableName.equals(tableName)) return table;
		}
		return null;
	}
	
	/**
	 * 指定のSQLを実行し、テーブルを作成する。<p/>
	 * テーブル作成に失敗した場合、例外を送出する。
	 * 
	 * @param sql 実行対象のSQLオブジェクト
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	void createTable(Sql sql) throws DataStoreManagerException {
		try {
			if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_STARTED);
			this.transaction.createTable(sql);
			this.sqlList.add(sql);
		} catch (DataStoreManagerException e) {
			this.exceptionList.add(e);
			throw e;
		}
	}
	
	/**
	 * 指定のSQLを実行し、テーブルを削除する。<p/>
	 * テーブル削除に失敗した場合、例外を送出する。
	 * 
	 * @param sql 実行対象のSQLオブジェクト
	 * @throws DataStoreManagerException テーブル削除に失敗した場合
	 */
	void dropTable(Sql sql) throws DataStoreManagerException {
		try {
			if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_STARTED);
			this.transaction.dropTable(sql);
			this.sqlList.add(sql);
		} catch (DataStoreManagerException e) {
			this.exceptionList.add(e);
			throw e;
		}
	}
	
	/**
	 * 指定のSQLを実行し、レコードを作成する。<p/>
	 * レコード追加に失敗した場合、例外を送出する。
	 * 
	 * @param sql 実行対象のSQLオブジェクト
	 * @throws DataStoreManagerException レコード追加に失敗した場合
	 */
	void insert(Sql sql) throws DataStoreManagerException {
		try {
			if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_STARTED);
			this.transaction.insert(sql);
			this.sqlList.add(sql);
		} catch (DataStoreManagerException e) {
			this.exceptionList.add(e);
			throw e;
		}
	}
	
	/**
	 * 指定のSQLを実行し、レコードの更新を実行する。<p/>
	 * レコードの更新に失敗した場合、例外を送出する。
	 * 
	 * @param sql 実行対象のSQLオブジェクト
	 * @return 更新結果の件数
	 * @throws DataStoreManagerException レコード更新に失敗した場合
	 */
	int update(Sql sql) throws DataStoreManagerException {
		try {
			if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_STARTED);
			int result = this.transaction.update(sql);
			this.sqlList.add(sql);
			return result;
		} catch (DataStoreManagerException e) {
			this.exceptionList.add(e);
			throw e;
		}
	}
	
	/**
	 * 指定のSQLを実行し、レコードの削除を実行する。<p/>
	 * レコードの削除に失敗した場合、例外を送出する。
	 * 
	 * @param sql 実行対象のSQLオブジェクト
	 * @return 削除結果の件数
	 * @throws DataStoreManagerException レコード削除に失敗した場合
	 */
	int delete(Sql sql) throws DataStoreManagerException {
		try {
			if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_STARTED);
			int result = this.transaction.delete(sql);
			this.sqlList.add(sql);
			return result;
		} catch (DataStoreManagerException e) {
			this.exceptionList.add(e);
			throw e;
		}
	}
	
	/**
	 * 指定のSQLを元に、レコード取得を実施します。<p/>
	 * SQLの実行に失敗した場合、例外が送出される。<br/>
	 * 
	 * @param sql 実行対象のSELECT文 
	 * @return 実行結果
	 * @throws DataStoreManagerException SQLの実行に失敗した場合
	 */
	ResultSet select(Sql sql) throws DataStoreManagerException {
		try {
			if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_STARTED);
			ResultSet result = this.transaction.select(sql);
			this.sqlList.add(sql);
			return result;
		} catch (DataStoreManagerException e) {
			this.exceptionList.add(e);
			throw e;
		}
	}
	
	/**
	 * トランザクションを生成します</p>
	 * 
	 * @return トランザクション
	 * @throws DataStoreManagerException トランザクション生成に失敗した場合
	 */
	protected Transaction createTransaction(DataBaseAccessParameter dataBaseAccessParameter) throws DataStoreManagerException {
		return new Transaction(dataBaseAccessParameter);
	}
	
	@Override
	public int hashCode() {
		int hashcode = this.dataBaseAccessParameter.hashCode();
		return hashcode;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof DataBaseDataStore)) return false;
		DataBaseDataStore thisClassObj = (DataBaseDataStore) object;
		if (this.transaction == null && thisClassObj.transaction == null) {
			if(thisClassObj.dataBaseAccessParameter.hashCode() == this.dataBaseAccessParameter .hashCode()) return true;
		} else if (this.transaction == null && thisClassObj.transaction != null) {
			return false;
		} else if (this.transaction != null && thisClassObj.transaction == null) {
			return false;
		} else {
			if(thisClassObj.transaction.hashCode() == this.transaction.hashCode()) return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		if (this.transaction == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("CONNECTION_HASH=[Transaction has not been started]").append(',');
			sb.append(this.dataBaseAccessParameter.toString());
			return sb.toString();
		} else {
			return this.transaction.toString();
		}
		
	}
}
