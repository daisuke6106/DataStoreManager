package jp.co.dk.datastoremanager.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import static jp.co.dk.datastoremanager.message.DataStoreManagerMessage.*;

/**
 * Sqlは、SQL本文と、そのSQLに対するパラメータを保持し、単一のSQL本文を生成するクラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class Sql {
	
	/** SQL本文 */
	private StringBuilder sql = new StringBuilder();
	
	/** パラメータ */
	private List<SqlParameter> sqlParameter = new ArrayList<SqlParameter>();
	
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
	 * 指定の文字列を元に、SQLの？部分にあたる文字列を設定します。<p/>
	 * データベースに送るときに、ドライバはこれを SQL VARCHAR または LONGVARCHAR 値 (ドライバの VARCHAR 値に関する制限に関する引数のサイズに依存) に変換します。
	 * @param parameter SQLの？部分にあたる文字列
	 */
	public void setParameter(String parameter) {
		sqlParameter.add(new StringSqlParameter(parameter));
	}
	
	/**
	 * 指定の数値を元に、SQLの？部分にあたる数値を設定します。<p/>
	 * データベースに送るときに、ドライバはこれを SQL INTEGER 値に変換します。
	 * @param parameter SQLの？部分にあたる数値
	 */
	public void setParameter(int parameter) {
		sqlParameter.add(new IntSqlParameter(parameter));
	}
	
	/**
	 * 指定の日付を元に、SQLの？部分にあたる日付を設定します。<p/>
	 * データベースに送るときに、ドライバはこれを SQL DATE 値に変換します。
	 * @param parameter SQLの？部分にあたる日付
	 */
	public void setParameter(Date parameter) {
		sqlParameter.add(new DateSqlParameter(parameter));
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
		int hashcode = this.sql.hashCode();
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
