package jp.co.dk.datastoremanager.core;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

/**
 * 接続先のデータストアの種別を定義します。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public enum DataStoreKind {
	
	/** ORACLE */
	ORACLE("oracle"),
	
	/** MYSQL */
	MYSQL("mysql"),
	
	/** POSTGRESSQL */
	POSTGRESQL("postgresql"),

	/** NEO4J */
	NEO4J("neo4j"),
	
	/** CSV */
	CSV("csv"),
	;
	
	/** データベース種別 */
	private String kind;
	
	/**
	 * コンストラクタ<p/>
	 * データベース種別名称を元にインスタンスを生成します。
	 * 
	 * @param kind データベース種別名称
	 */
	private DataStoreKind(String kind) {
		this.kind = kind;
	}
	
	/**
	 * 指定の文字列をデータストアの種別へ変換します。
	 * 
	 * @param kind データストア種別を表す文字列
	 * @return 指定されたデータストア種別の文字列を変換したデータストア種別オブジェクト
	 * @throws DataStoreManagerException 変換できないデータストア種別を指定された場合
	 */
	public static DataStoreKind convert(String kind) throws DataStoreManagerException {
		for (DataStoreKind kindObj : DataStoreKind.values()) {
			if (kindObj.kind.equals(kind)) return kindObj;
		}
		if (kind == null) {
			throw new DataStoreManagerException(DATASTORE_KIND_VALUE_IS_FAILE, "null");
		} else {
			throw new DataStoreManagerException(DATASTORE_KIND_VALUE_IS_FAILE, kind);
		}
	}
	
	@Override
	public String toString() {
		return this.kind;
	}
}

