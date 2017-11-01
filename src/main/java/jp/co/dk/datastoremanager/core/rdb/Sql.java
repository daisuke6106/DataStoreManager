package jp.co.dk.datastoremanager.core.rdb;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.BytesColumnData;
import jp.co.dk.datastoremanager.core.rdb.DateColumnData;
import jp.co.dk.datastoremanager.core.rdb.IntColumnData;
import jp.co.dk.datastoremanager.core.rdb.LongColumnData;
import jp.co.dk.datastoremanager.core.rdb.ObjectColumnData;
import jp.co.dk.datastoremanager.core.rdb.Sql;
import jp.co.dk.datastoremanager.core.rdb.SqlParameter;
import jp.co.dk.datastoremanager.core.rdb.StringColumnData;
import jp.co.dk.datastoremanager.core.rdb.TimestampColumnData;
import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

/**
 * Sqlは、SQL本文と、そのSQLに対するパラメータを保持し、単一のSQL本文を生成するクラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class Sql {
	
	/** SQL本文 */
	protected StringBuilder sql = new StringBuilder();
	
	/** パラメータ */
	protected List<SqlParameter> sqlParameter = new ArrayList<SqlParameter>();
	
	/**
	 * コンストラクタ<p/>
	 * 指定のSQLを表す文字列を元に、SQLオブジェクトのインスタンスを生成します。<br/>
	 * SQL本文がnull、または空文字の場合例外を送出します。
	 * 
	 * @param sql SQL本文の文字列
	 * @throws DataStoreManagerException SQLオブジェクトのインスタンス生成に失敗した場合
	 */
	public Sql(String sql) throws DataStoreManagerException {
		if (sql == null || sql.equals("")) throw new DataStoreManagerException(FAILE_TO_CREATE_SQL_OBJECT); 
		this.sql.append(sql);
	}
	
	/**
	 * 本SQLにSQLを追加する。
	 * @param sql 追加する対象のSQL
	 */
	public Sql add(String sql) {
		if (sql == null) return this;
		this.sql.append(sql);
		return this;
	}
	
	/**
	 * 本SQLにSQLを追加する。
	 * @param sql 追加する対象のSQL
	 */
	@SuppressWarnings("rawtypes")
	public Sql add(Enum parameter) {
		if (parameter == null) return this;
		this.sql.append(parameter.toString());
		return this;
	}
	
	
	/**
	 * 指定の文字列を元に、SQLの？部分にあたる文字列を設定します。<p/>
	 * データベースに送るときに、ドライバはこれを SQL VARCHAR または LONGVARCHAR 値 (ドライバの VARCHAR 値に関する制限に関する引数のサイズに依存) に変換します。
	 * @param parameter SQLの？部分にあたる文字列
	 */
	public Sql setParameter(String parameter) {
		this.sqlParameter.add(new StringColumnData(parameter));
		return this;
	}
	
	/**
	 * 指定の文字列を元に、SQLの？部分にあたる文字列を設定します。<p/>
	 * データベースに送るときに、ドライバはこれを SQL VARCHAR または LONGVARCHAR 値 (ドライバの VARCHAR 値に関する制限に関する引数のサイズに依存) に変換します。
	 * @param SQLの？部分にあたる文字列（定数クラス）
	 * @param parameter SQLの？部分にあたる文字列
	 */
	@SuppressWarnings("rawtypes")
	public Sql setParameter(Enum parameter) {
		this.sqlParameter.add(new StringColumnData(parameter.toString()));
		return this;
	}
	
	/**
	 * 指定の数値を元に、SQLの？部分にあたる数値を設定します。<p/>
	 * データベースに送るときに、ドライバはこれを SQL INTEGER 値に変換します。
	 * @param parameter SQLの？部分にあたる数値(int)
	 */
	public Sql setParameter(int parameter) {
		this.sqlParameter.add(new IntColumnData(parameter));
		return this;
	}
	
	/**
	 * 指定の数値を元に、SQLの？部分にあたる数値を設定します。<p/>
	 * データベースに送るときに、ドライバはこれを SQL BIGINT 値に変換します。
	 * @param parameter SQLの？部分にあたる数値(long)
	 */
	public Sql setParameter(long parameter) {
		this.sqlParameter.add(new LongColumnData(parameter));
		return this;
	}
	
	/**
	 * 指定の日付を元に、SQLの？部分にあたる日付を設定します。<p/>
	 * データベースに送るときに、ドライバはこれを SQL DATE 値に変換します。
	 * @param parameter SQLの？部分にあたる日付
	 */
	public Sql setParameter(Date parameter) {
		this.sqlParameter.add(new DateColumnData(parameter));
		return this;
	}
	
	/**
	 * 指定の日時を元に、SQLの？部分にあたる日付を設定します。<p/>
	 * データベースに送るときに、ドライバはこれを SQL DATE 値に変換します。
	 * @param parameter SQLの？部分にあたる日付
	 * @throws DataStoreManagerException 設定されたパラメータがnullの場合
	 */
	public Sql setParameter(Timestamp parameter) {
		this.sqlParameter.add(new TimestampColumnData(parameter));
		return this;
	}
	
	/**
	 * 指定のバイト配列を元に、SQLの？部分にあたるバイトは配列を設定します。<p/>
	 * データベースに送るときに、ドライバはこれを SQL VARBINARY または LONGVARBINARY (ドライバの VARBINARY 値に関する制限に関する引数のサイズに依存) に変換します。
	 * @param parameter SQLの？部分にあたるバイト配列
	 */
	public Sql setParameter(byte[] parameter) {
		this.sqlParameter.add(new BytesColumnData(parameter));
		return this;
	}
	
	/**
	 * 指定のオブジェクトを元に、SQLの？部分にあたるバイトは配列を設定します。<p/>
	 * 指定された引数は、データベースに送られる前に、対応する SQL 型に変換されます。<br/>
	 * このメソッドは、ドライバ固有の Java 型を使用して、データベース固有の抽象データ型を渡すために使用することに注意してください。<br/>
	 * オブジェクトがインタフェース SQLData を実装するクラスのインスタンスである場合、JDBC ドライバは SQLData.writeSQL メソッドを呼び出して、そのオブジェクトを SQL データストリームへ書き込む必要があります。<br/>
	 * また、オブジェクトが Ref、Blob、Clob、NClob、Struct、java.net.URL、RowId、SQLXML、または Array を実装するクラスのオブジェクトである場合、ドライバはこのオブジェクトを対応する SQL 型の値としてデータベースに渡す必要があります。  
	 * @param parameter SQLの？部分にあたるバイト配列
	 */
	public Sql setParameter(Serializable parameter) {
		this.sqlParameter.add(new ObjectColumnData(parameter));
		return this;
	}
	
	/**
	 * 指定のオブジェクトを元に、SQLの？部分にあたるバイトは配列を設定します。<p/>
	 * 指定のオブジェクトはバイト配列に変換されて保持されます。<p/>
	 * データベースに送るときに、ドライバはこれを SQL VARBINARY または LONGVARBINARY (ドライバの VARBINARY 値に関する制限に関する引数のサイズに依存) に変換します。
	 * @param parameter SQLの？部分にあたるバイト配列
	 * @throws DataStoreManagerException 設定されたパラメータがnullの場合
	 */
	public Sql setParameterConvertToBytes(Object parameter) throws DataStoreManagerException{
		this.sqlParameter.add(new BytesColumnData(parameter));
		return this;
	}
	
	public String getSql() {
		return this.sql.toString();
	}
	
	/**
	 * このSQLに設定されたSQLに対するパラメータの一覧を取得します。
	 * @return パラメータの一覧
	 */
	List<SqlParameter> getParameterList() {
		return new ArrayList<SqlParameter>(this.sqlParameter);
	}
	
	@Override
	public int hashCode() { 
		int hashcode = this.sql.toString().hashCode();
		for (SqlParameter param : this.sqlParameter) hashcode *= param.hashCode();
		return hashcode *17;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof Sql)) return false;
		Sql thisClassObj = (Sql) object;
		if (thisClassObj.hashCode() == this.hashCode()) return true;
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sqlstr = new StringBuilder("SQL=[").append(this.sql).append(']');
		if (sqlParameter.size() == 0) {
			sqlstr.append(" PARAMETER=[NOTHING]");
			return sqlstr.toString();
		} else {
			sqlstr.append(" PARAMETER=[");
			for (SqlParameter param : this.sqlParameter) {
				sqlstr.append(param.toString()).append(", ");
			}
			int index = sqlstr.length();
			sqlstr.delete(index-2, index);
			sqlstr.append(']');
			return sqlstr.toString();
		}
	}
}
