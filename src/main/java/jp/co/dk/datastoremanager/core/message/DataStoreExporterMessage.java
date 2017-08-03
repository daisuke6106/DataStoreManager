package jp.co.dk.datastoremanager.core.message;

import jp.co.dk.message.AbstractMessage;

/**
 * DataStoreManagerMessageは、データストアの操作にて使用するメッセージを定義する定数クラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class DataStoreExporterMessage extends AbstractMessage{
	
	/** ファイル読み込みに失敗しました。PATH=[{0}] */
	public static final DataStoreExporterMessage FAILED_TO_READ_FILE = new DataStoreExporterMessage("E001");
	
	/** 変数値が設定されていません。 */
	public static final DataStoreExporterMessage VALIABLE_IS_NOT_SET = new DataStoreExporterMessage("E002");
	
	/** 変数型に不明な値が設定されています。 */
	public static final DataStoreExporterMessage VALIABLE_TYPE_IS_UNKNOWN = new DataStoreExporterMessage("E003");
	
	/** 変数に不正な値が設定されています。 */
	public static final DataStoreExporterMessage VALIABLE_IS_CHECK_ERROR = new DataStoreExporterMessage("E004");
	
	/** 変数の値が指定されていません。変数名=[{0}] */
	public static final DataStoreExporterMessage VALUE_IS_NOT_SET = new DataStoreExporterMessage("E005");
	
	/** パラメータのフォーマットが不正です。パラメータ=[{0}] */
	public static final DataStoreExporterMessage PARAMETER_FORMAT_IS_INVALID = new DataStoreExporterMessage("E006");
	
	/** カラム情報の取得に失敗しました。 */
	public static final DataStoreExporterMessage FAILED_TO_ACQUIRE_COLUMN_INFO = new DataStoreExporterMessage("E007");


	
	// === HTML関連 ===
	/** HTMLの生成に失敗しました。 */
	public static final DataStoreExporterMessage FAILED_TO_GENERATE_HTML = new DataStoreExporterMessage("E101");
	
	/** HTMLの出力に失敗しました。PATH=[{0}] */
	public static final DataStoreExporterMessage FAILED_TO_CREATE_HTML = new DataStoreExporterMessage("E102");
	
			
	protected DataStoreExporterMessage(String messageId) {
		super(messageId);
	}

}
