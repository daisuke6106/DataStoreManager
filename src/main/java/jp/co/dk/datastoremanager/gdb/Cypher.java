package jp.co.dk.datastoremanager.gdb;

import java.util.ArrayList;
import java.util.List;

import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import static jp.co.dk.datastoremanager.message.DataStoreManagerMessage.*;

/**
 * Cypherは、Cypher本文と、そのCypherに対するパラメータを保持し、単一のCypher本文を生成するクラスです。
 * 
 * @version 1.1
 * @author D.Kanno
 */
public class Cypher {
	
	/** Cypher本文 */
	private StringBuilder cypher = new StringBuilder();
	
	/** パラメータ */
	private List<CypherParameter> cypherParameter = new ArrayList<CypherParameter>();
	
	/**
	 * コンストラクタ<p/>
	 * 指定のCypherを表す文字列を元に、Cypherオブジェクトのインスタンスを生成します。<br/>
	 * Cypher本文がnull、または空文字の場合例外を送出します。
	 * 
	 * @param cypher Cypher本文の文字列
	 * @throws DataStoreManagerException Cypherオブジェクトのインスタンス生成に失敗した場合
	 */
	public Cypher(String cypher) throws DataStoreManagerException {
		if (cypher == null || cypher.equals("")) throw new DataStoreManagerException(FAILE_TO_CREATE_SQL_OBJECT); 
		this.cypher.append(cypher);
	}
	/**
	 * 指定のCypher言語の文字列を本クラスのCypherの末尾に追加します。
	 * @param cypher Cypher文字列
	 * @throws DataStoreManagerException 引数に指定されたCypherがnullまたは空文字の場合
	 */
	public Cypher append(String cypher) throws DataStoreManagerException {
		if (cypher == null || cypher.equals("")) throw new DataStoreManagerException(FAILE_TO_CREATE_SQL_OBJECT); 
		this.cypher.append(cypher);
		return this;
	}
	
	/**
	 * 指定の文字列を元に、Cypherの{x}部分にあたる文字列を設定します。
	 * @param parameter Cypherの{x}部分にあたる文字列
	 * @return 本オブジェクトインスタンス
	 */
	public Cypher setParameter(String parameter){
		this.cypherParameter.add(new StringCypherParameter(parameter));
		return this;
	}
	
	/**
	 * 指定の数値を元に、Cypherの{x}部分にあたる数値を設定します。
	 * @param parameter Cypherの{x}部分にあたる数値(int)
	 * @return 本オブジェクトインスタンス
	 */
	public Cypher setParameter(int parameter) {
		this.cypherParameter.add(new NumericCypherParameter(parameter));
		return this;
	}
	
	/**
	 * 指定の数値を元に、Cypherの{x}部分にあたる数値を設定します。
	 * @param parameter Cypherの{x}部分にあたる数値(boolean)
	 * @return 本オブジェクトインスタンス
	 */
	public Cypher setParameter(boolean parameter) {
		this.cypherParameter.add(new BooleanCypherParameter(parameter));
		return this;
	}
	
	public String getCypher() {
		return this.cypher.toString();
	}
	
	/**
	 * このCypherに設定されたCypherに対するパラメータの一覧を取得します。
	 * @return パラメータの一覧
	 */
	List<CypherParameter> getParameterList() {
		return new ArrayList<CypherParameter>(this.cypherParameter);
	}
	
	@Override
	public int hashCode() { 
		int hashcode = this.cypher.toString().hashCode();
		for (CypherParameter param : this.cypherParameter) hashcode *= param.hashCode();
		return hashcode *17;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof Cypher)) return false;
		Cypher thisClassObj = (Cypher) object;
		if (thisClassObj.hashCode() == this.hashCode()) return true;
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sqlstr = new StringBuilder("CYPHER=[").append(this.cypher).append(']');
		if (cypherParameter.size() == 0) {
			sqlstr.append(" PARAMETER=[NOTHING]");
			return sqlstr.toString();
		} else {
			sqlstr.append(" PARAMETER=[");
			for (CypherParameter param : this.cypherParameter) {
				sqlstr.append(param.toString()).append(", ");
			}
			int index = sqlstr.length();
			sqlstr.delete(index-2, index);
			sqlstr.append(']');
			return sqlstr.toString();
		}
	}
}
