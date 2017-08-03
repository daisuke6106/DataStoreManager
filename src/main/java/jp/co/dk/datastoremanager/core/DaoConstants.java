package jp.co.dk.datastoremanager.core;

/**
 * DaoConstantsは、すべてのデータアクセスオブジェクトを定義するEnumクラスが実装するインターフェースです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public interface DaoConstants {
	
	/**
	 * このDAOのファクトリクラスを取得します。
	 * @return DAOファクトリクラスオブジェクト
	 */
	public DataAccessObjectFactory getDataAccessObjectFactory();
	
	/**
	 * このデータアクセスオブジェクトの名称を取得します。
	 * 
	 * @return データアクセスオブジェクトの名称
	 */
	public String getName();
	
}
