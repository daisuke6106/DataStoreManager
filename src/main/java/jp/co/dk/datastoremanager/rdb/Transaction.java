package jp.co.dk.datastoremanager.rdb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.logger.Logger;
import jp.co.dk.logger.LoggerFactory;
import static jp.co.dk.datastoremanager.message.DataStoreManagerMessage.*;

/**
 * Transactionは、指定のデータベースアクセスパラメータを元にコネクションを作成し、トランザクションを管理する。<p/>
 * <br/>
 * トランザション分離は以下のとおり<br/>
 * ダーティー読み取りは抑制、繰り返し不可の読み取りおよびファントム読み取りは起こります。<br/>
 * <br/>
 * ・ダーティ読み取り<br/>
 * 　トランザクションの外からトランザクションの内部で変更されたデータが読み取れてしまうこと。他のトランザクションのコミット前のデータを読み取ると、ロールバックしたときに値が変わってしまう。<br/>
 * ・反復不能読み取り<br/>
 * 　同じローを複数回読むと、読み出すたびに結果が異なる場合。読み取った後に他のトランザクションにデータを変更されたときなど。<br/>
 * ・ファントム<br/>
 * 　検索基準に合致するがクエリーからは見えないローのこと。他のトランザクションからクエリーの範囲に行を挿入した場合など。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class Transaction {
	
	/** コネクション */
	protected Connection connection;
	
	/** パラメータ */
	protected DataBaseAccessParameter dataBaseAccessParameter;
	
	/** ロガーインスタンス */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * コンストラクタ<p/>
	 * 指定のデータベースアクセスパラメータを元にトランザクションを開始する。<br/>
	 * <br/>
	 * このトランザクションは、ダーティー読み取りは抑制されます。<br/>
	 * 繰り返し不可の読み取りおよびファントム読み取りは起こります。<br/>
	 * このレベルは、コミットされていない変更がある行をトランザクションが読み取ることを禁止するだけです。
	 * 
	 * @param dataBaseAccessParameter データベースアクセスパラメータ
	 * @throws DataStoreManagerException トランザクションの開始に失敗した場合
	 */
	protected Transaction(DataBaseAccessParameter dataBaseAccessParameter) throws DataStoreManagerException{
		this.logger.constractor(this.getClass(), dataBaseAccessParameter);
		if (dataBaseAccessParameter == null) throw new DataStoreManagerException(PARAMETER_IS_NOT_SET);
		this.dataBaseAccessParameter = dataBaseAccessParameter;
		DataBaseDriverConstants driverConstants = dataBaseAccessParameter.getDriver();
		String driver   = driverConstants.getDriverClass();
		String url      = dataBaseAccessParameter.getUrl();
		String user     = dataBaseAccessParameter.getUser();
		String password = dataBaseAccessParameter.getPassword();
		try {
			Class.forName(driver);
			this.connection = DriverManager.getConnection(url, user, password);
			this.connection.setAutoCommit(false);
			this.connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		} catch (ClassNotFoundException e) {
			throw new DataStoreManagerException(FAILE_TO_READ_DRIVER, driverConstants.getDriverClass(), e);
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_CREATE_CONNECTION, this.dataBaseAccessParameter.toString(), e);
		}
		this.logger.info("transaction start param=[" + this.dataBaseAccessParameter + "]");
	}
	
	/**
	 * 指定のSQLを実行し、テーブルを作成する。<p/>
	 * テーブル作成に失敗した場合、例外を送出する。
	 * 
	 * @param sql 実行対象のSQLオブジェクト
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void createTable(Sql sql) throws DataStoreManagerException {
		this.logger.info("create table " + sql.toString());
		PreparedStatement statement = null;
		try {
			statement = this.connection.prepareStatement(sql.getSql());
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_EXECUTE_SQL, sql.toString(), e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DataStoreManagerException(FAILE_TO_CLOSE, sql.toString(), e);
				}
			}
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
		this.logger.info("insert " + sql.toString());
		PreparedStatement statement = null;
		try {
			statement = this.connection.prepareStatement(sql.getSql());
			List<SqlParameter> sqlPrameterList = sql.getParameterList();
			for (int index = 0; index < sqlPrameterList.size(); index++) {
				int tmpIndex = index + 1;
				sqlPrameterList.get(index).set(tmpIndex, statement);
			}
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_EXECUTE_SQL, sql.toString(), e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DataStoreManagerException(FAILE_TO_CLOSE, sql.toString(), e);
				}
			}
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
		this.logger.info("update " + sql.toString());
		PreparedStatement statement = null;
		try {
			statement = this.connection.prepareStatement(sql.getSql());
			List<SqlParameter> sqlPrameterList = sql.getParameterList();
			for (int index = 0; index < sqlPrameterList.size(); index++) {
				int tmpIndex = index + 1;
				sqlPrameterList.get(index).set(tmpIndex, statement);
			}
			int result = statement.executeUpdate();
			statement.executeUpdate();
			statement.close();
			return result;
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_EXECUTE_SQL, sql.toString(), e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DataStoreManagerException(FAILE_TO_CLOSE, sql.toString(), e);
				}
			}
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
		this.logger.info("delete " + sql.toString());
		PreparedStatement statement = null;
		try {
			statement = this.connection.prepareStatement(sql.getSql());
			List<SqlParameter> sqlPrameterList = sql.getParameterList();
			for (int index = 0; index < sqlPrameterList.size(); index++) {
				int tmpIndex = index + 1;
				sqlPrameterList.get(index).set(tmpIndex, statement);
			}
			int result = statement.executeUpdate();
			statement.executeUpdate();
			statement.close();
			return result;
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_EXECUTE_SQL, sql.toString(), e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DataStoreManagerException(FAILE_TO_CLOSE, sql.toString(), e);
				}
			}
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
		this.logger.info("select " + sql.toString());
		PreparedStatement statement = null;
		try {
			statement = this.connection.prepareStatement(sql.getSql());
			List<SqlParameter> sqlPrameterList = sql.getParameterList();
			for (int index = 0; index < sqlPrameterList.size(); index++) {
				int tmpIndex = index + 1;
				sqlPrameterList.get(index).set(tmpIndex, statement);
			}
			return statement.executeQuery();
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_EXECUTE_SQL, sql.toString(), e);
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
		this.logger.info("drop table " + sql.toString());
		PreparedStatement statement = null;
		try {
			statement = this.connection.prepareStatement(sql.getSql());
			statement.execute();
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_EXECUTE_SQL, sql.toString(), e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DataStoreManagerException(FAILE_TO_CLOSE, sql.toString(), e);
				}
			}
		}
	}
	
	/**
	 * このトランザクションに対してコミットを実行します。
	 * 
	 * @throws DataStoreManagerException コミットに失敗した場合
	 */
	void commit() throws DataStoreManagerException {
		this.logger.info("commit start");
		try {
			this.connection.commit();
			this.logger.info("commit fin");
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_COMMIT, e);
		}
	}
	
	/**
	 * このトランザクションに対してロールバックを実行します。
	 * 
	 * @throws DataStoreManagerException ロールバックに失敗した場合
	 */
	void rollback() throws DataStoreManagerException {
		this.logger.info("rollback start");
		try {
			this.connection.rollback();
			this.logger.info("rollback fin");
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_ROLLBACK, e);
		}
	}

	/**
	 * このトランザクションが接続しているデータベースに指定のテーブルが存在するか確認する。
	 * 
	 * @param tableName テーブル名
	 * @return テーブル有無（true=テーブル有り、false=テーブル無し）
	 * @throws DataStoreManagerException テーブル情報の取得に失敗した場合
	 */
	public boolean isExistsTable(String tableName) throws DataStoreManagerException {
		return (this.getTableName(tableName).size() >= 1);
	}
	
	/**
	 * このトランザクションが接続できるテーブルの一覧を取得する。<p/>
	 * 取得できなかった場合、空のリストを返却する。
	 * 
	 * @return テーブル一覧 
	 * @throws DataStoreManagerException テーブル情報の取得に失敗した場合
	 */
	public List<String> getAllTableName() throws DataStoreManagerException {
		return this.getTableName("%");
	}
	
	protected List<String> getTableName(String tableName) throws DataStoreManagerException {
		try {
			List<String> tables = new ArrayList<>();
			DatabaseMetaData dbmd = this.connection.getMetaData();
			String schema = this.dataBaseAccessParameter.getUser().toUpperCase();
			ResultSet rs = dbmd.getTables(null, schema, tableName, new String[]{"TABLE"});
			while (rs.next()) tables.add(rs.getString("TABLE_NAME"));
			return tables;
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_CLOSE, e);
		}
	}
	
	
	/**
	 * このトランザクションに対してクローズを実施します。
	 * 
	 * @throws DataStoreManagerException クローズに失敗した場合
	 */
	void close() throws DataStoreManagerException {
		this.logger.info("transaction close");
		try {
			this.connection.close();
		} catch (SQLException e) {
			throw new DataStoreManagerException(FAILE_TO_CLOSE, e);
		}
	}
	
	@Override
	public int hashCode() {
		return this.connection.hashCode() * this.dataBaseAccessParameter.hashCode() * 17;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof Transaction)) return false;
		Transaction thisClassIbj = (Transaction)object;
		if (thisClassIbj.hashCode() == this.hashCode()) return true;
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CONNECTION_HASH=[").append(this.connection.hashCode()).append(']').append(',');
		sb.append(this.dataBaseAccessParameter.toString());
		return sb.toString();
	}
	
	@Override
	protected void finalize() {
		try {
			if (!(this.connection.isClosed())) {
				this.connection.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
