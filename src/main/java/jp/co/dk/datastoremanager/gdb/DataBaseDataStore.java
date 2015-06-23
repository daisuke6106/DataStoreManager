package jp.co.dk.datastoremanager.gdb;

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
 * 単一の接続先に対するトランザクション管理、Cypherの実行、実行されたCypherの履歴保持を行う。<br/>
 * 
 * @version 1.1
 * @author D.Kanno
 */
public class DataBaseDataStore implements DataStore {
	
	/** データベースアクセスパラメータ */
	protected DataBaseAccessParameter dataBaseAccessParameter;
	
	/** トランザクション */
	protected Transaction transaction;
	
	/** Cypherリスト */
	protected List<Cypher> cypherList = new ArrayList<Cypher>();
	
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
	DataBaseDataStore(DataBaseAccessParameter dataBaseAccessParameter) {
		this.logger.constractor(this.getClass(), dataBaseAccessParameter);
		this.dataBaseAccessParameter = dataBaseAccessParameter;
	}
	
	@Override
	public void startTransaction() throws DataStoreManagerException {
		this.transaction = new Transaction(dataBaseAccessParameter);
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
	 * 指定のCypherを実行し、ノード、またはリレーションを作成する。<p/>
	 * ノード、またはリレーション追加に失敗した場合、例外を送出する。
	 * 
	 * @param cypher 実行対象のCypherオブジェクト
	 * @throws DataStoreManagerException ノード、またはリレーション追加に失敗した場合
	 */
	void insert(Cypher cypher) throws DataStoreManagerException {
		try {
			if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_STARTED);
			this.transaction.insert(cypher);
			this.cypherList.add(cypher);
		} catch (DataStoreManagerException e) {
			this.exceptionList.add(e);
			throw e;
		}
	}
	
	/**
	 * 指定のCypherを実行し、ノード、またはリレーションの更新を実行する。<p/>
	 * ノード、またはリレーションの更新に失敗した場合、例外を送出する。
	 * 
	 * @param cypher 実行対象のCypherオブジェクト
	 * @return 更新結果の件数
	 * @throws DataStoreManagerException ノード、またはリレーション更新に失敗した場合
	 */
	int update(Cypher cypher) throws DataStoreManagerException {
		try {
			if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_STARTED);
			int result = this.transaction.update(cypher);
			this.cypherList.add(cypher);
			return result;
		} catch (DataStoreManagerException e) {
			this.exceptionList.add(e);
			throw e;
		}
	}
	
	/**
	 * 指定のCypherを実行し、ノード、またはリレーションの削除を実行する。<p/>
	 * ノード、またはリレーションの削除に失敗した場合、例外を送出する。
	 * 
	 * @param cypher 実行対象のCypherオブジェクト
	 * @return 削除結果の件数
	 * @throws DataStoreManagerException ノード、またはリレーション削除に失敗した場合
	 */
	int delete(Cypher cypher) throws DataStoreManagerException {
		try {
			if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_STARTED);
			int result = this.transaction.delete(cypher);
			this.cypherList.add(cypher);
			return result;
		} catch (DataStoreManagerException e) {
			this.exceptionList.add(e);
			throw e;
		}
	}
	
	/**
	 * 指定のCypherを元に、ノード、またはリレーション取得を実施します。<p/>
	 * Cypherの実行に失敗した場合、例外が送出される。<br/>
	 * 
	 * @param cypher 実行対象のSELECT文 
	 * @return 実行結果
	 * @throws DataStoreManagerException Cypherの実行に失敗した場合
	 */
	ResultSet select(Cypher cypher) throws DataStoreManagerException {
		try {
			if (this.transaction == null) throw new DataStoreManagerException(TRANSACTION_IS_NOT_STARTED);
			ResultSet result = this.transaction.select(cypher);
			this.cypherList.add(cypher);
			return result;
		} catch (DataStoreManagerException e) {
			this.exceptionList.add(e);
			throw e;
		}
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
