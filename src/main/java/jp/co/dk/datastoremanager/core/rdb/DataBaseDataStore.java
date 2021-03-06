package jp.co.dk.datastoremanager.core.rdb;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jp.co.dk.datastoremanager.core.DaoConstants;
import jp.co.dk.datastoremanager.core.DataAccessObject;
import jp.co.dk.datastoremanager.core.DataStore;
import jp.co.dk.datastoremanager.core.DataStoreKind;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.rdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.core.rdb.DataBaseRecord;
import jp.co.dk.datastoremanager.core.rdb.Sql;
import jp.co.dk.datastoremanager.core.rdb.Transaction;
import jp.co.dk.logger.Logger;
import jp.co.dk.logger.LoggerFactory;

import static jp.co.dk.datastoremanager.core.message.DataStoreExporterMessage.FAILED_TO_ACQUIRE_COLUMN_INFO;
import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

/**
 * DataBaseDataStoreは、単一のデータベースのデータストアを表すクラスです。<p/>
 * 単一の接続先に対するトランザクション管理、SQLの実行、実行されたSQLの履歴保持を行う。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class DataBaseDataStore implements DataStore {
	
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
	
	/** プライマリーキー情報一覧のキャッシュ */
	protected List<PrimaryKeyMetaData> primaryKeyMetaDataList;
	
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
	
	/**
	 * 指定のSQLを実行し、テーブルを作成する。<p/>
	 * テーブル作成に失敗した場合、例外を送出する。
	 * 
	 * @param sql 実行対象のSQLオブジェクト
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void createTable(Sql sql) throws DataStoreManagerException {
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
	public void dropTable(Sql sql) throws DataStoreManagerException {
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
	public void insert(Sql sql) throws DataStoreManagerException {
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
	public int update(Sql sql) throws DataStoreManagerException {
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
	public int delete(Sql sql) throws DataStoreManagerException {
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
	public ResultSet select(Sql sql) throws DataStoreManagerException {
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
	
	public boolean isExistsTable(String tableName) throws DataStoreManagerException {
		if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_STARTED);
		return this.transaction.isExistsTable(tableName);
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
	
	/**
	 * このトランザクションが接続できるテーブルの一覧をメタデータとして取得する。<p/>
	 * 取得できなかった場合、空のリストを返却する。
	 * 
	 * @return テーブル一覧 
	 * @throws DataStoreManagerException テーブル情報の取得に失敗した場合
	 */
	public List<TableMetaData> getTables() throws DataStoreManagerException {
		if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_START);
		List<TableMetaData> tableMetaDataList = new ArrayList<>();
		List<String> tableNames = this.transaction.getAllTableName();
		tableNames = tableNames.stream().filter(N -> !N.startsWith(TableMetaData.HISTRY_TABLE_NAME_HEADER)).collect(Collectors.toList());
		String schema = this.dataBaseAccessParameter.getUser().toUpperCase();
		for (String tableName : tableNames) tableMetaDataList.add(this.createTableMetaData(this, schema, tableName));
		return tableMetaDataList;
	}
	
	/**
	 * このトランザクションが接続できるテーブルから指定の名称のテーブルをメタデータとして取得する。<p/>
	 * 取得できなかった場合、NULLを返却する。
	 * 
	 * @param tableName 取得対象のテーブル
	 * @return テーブル一覧 
	 * @throws DataStoreManagerException テーブル情報の取得に失敗した場合
	 */
	public TableMetaData getTable(String tableName) throws DataStoreManagerException {
		if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_START);
		List<String> tableNames = this.transaction.getTableName(tableName);
		tableNames = tableNames.stream().filter(N -> !N.startsWith(TableMetaData.HISTRY_TABLE_NAME_HEADER)).collect(Collectors.toList());
		String schema = this.dataBaseAccessParameter.getUser().toUpperCase();
		for (String tablename : tableNames) return this.createTableMetaData(this, schema, tablename);
		return null;
	}
	
	/**
	 * <p>このデータベースが持つ全プライマリーキー情報を取得する。</p>
	 * 一度取得したらキャッシュ化され、以降はキャッシュ化された情報を返却する。
	 * 
	 * @param tableName 取得対象のテーブル名
	 * @return プライマリーキー情報一覧
	 * @throws DataStoreManagerException プライマリーキー情報の取得に失敗した場合
	 */
	public List<PrimaryKeyMetaData> getPrimaryKey(String tableName) throws DataStoreManagerException {
		if (this.primaryKeyMetaDataList == null) {
			if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_START);
			List<PrimaryKeyMetaData> primaryKeyMetaDataList = new ArrayList<>();
			String schema = this.dataBaseAccessParameter.getUser().toUpperCase();
			try {
				DatabaseMetaData dbmd = this.transaction.connection.getMetaData();
				ResultSet primaryKeyResultSet = dbmd.getPrimaryKeys(null, schema, tableName);
				while (primaryKeyResultSet.next()) {
					primaryKeyMetaDataList.add( new PrimaryKeyMetaData(
						primaryKeyResultSet.getString("TABLE_NAME"),
						primaryKeyResultSet.getString("PK_NAME"),
						primaryKeyResultSet.getShort ("KEY_SEQ"),
						primaryKeyResultSet.getString("COLUMN_NAME")
					));
				}
			} catch (SQLException e) {
				throw new DataStoreManagerException(FAILED_TO_ACQUIRE_COLUMN_INFO, e);
			}
			this.primaryKeyMetaDataList = primaryKeyMetaDataList;
		}
		return new ArrayList<>(this.primaryKeyMetaDataList);
	}
	
	/**
	 * <p>DBから現在日時を取得する。</p>
	 * DBサーバに設定された現在日時を取得する。<br/>
	 * ORACLEで有ればSYSDATEの値を取得する。<br/>
	 * @return 現在日時
	 * @throws DataStoreManagerException 現在日時の取得に失敗した場合
	 */
	public abstract Date getDataBaseTime() throws DataStoreManagerException;
	
	protected abstract TableMetaData createTableMetaData(DataBaseDataStore dataBaseDataStore, String schma, String tableName) throws DataStoreManagerException;
	
	protected abstract DataBaseRecord createDataBaseRecord(ResultSet resultSet);
	
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
