package jp.co.dk.datastoremanager.core;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

/**
 * DataStoreParameterは、データストアにアクセスする際に必要なパラメータを保持するパラメータクラスが実装する基底クラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class DataStoreParameter {
	
	/** データストア種別 */
	protected DataStoreKind dataStoreKind;
	
	/**
	 * コンストラクタ<p/>
	 * 指定されたデータストア種別を元にデータストアパラメータのインスタンスを生成します。<br/>
	 * データストア種別が設定されなかった場合、例外を送出する。
	 * 
	 * @param dataStoreKind データストア種別
	 * @throws DataStoreManagerException データストア種別が設定されなかった場合
	 */
	protected DataStoreParameter(DataStoreKind dataStoreKind) throws DataStoreManagerException {
		if (dataStoreKind == null) throw new DataStoreManagerException(DATA_STORE_KIND_IS_NOT_SET);
		this.dataStoreKind = dataStoreKind;
	}
	
	/**
	 * データストア種別を取得する。
	 * 
	 * @return データストア種別
	 */
	public DataStoreKind getDataStoreKind() {
		return this.dataStoreKind;
	}
	
	/**
	 * このデータストアパラメータを元にデータストアインスタンスを生成する。
	 * 
	 * @return データストアインスタンス
	 */
	public abstract DataStore createDataStore();
	
	@Override
	public int hashCode() {
		return this.dataStoreKind.hashCode() * 17;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof DataStoreParameter)) return false;
		DataStoreParameter thisClassObj = (DataStoreParameter) object;
		if (this.hashCode() == thisClassObj.hashCode()) return true;
		return false;
	}
	
	@Override
	public abstract String toString();
}
