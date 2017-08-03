package jp.co.dk.datastoremanager.command.exporter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.dk.datastoremanager.core.exception.DataStoreExporterException;
import jp.co.dk.datastoremanager.core.message.DataStoreExporterMessage;

/**
 * <p>パラメータの一覧を表すクラスです。</p>
 * 引数に指定された文字列の一覧を元にパラメータの一覧を保持する
 * @version 1.2
 * @author D.Kanno
 */
public class Parameters {
	
	/** バインド変数フォーマット */
	protected static Pattern variableFormat = Pattern.compile("^(.+?)=(.+?)$");
	
	/** パラメータ一覧（「キー=値」形式の文字列を複数持つ配列） */
	protected String[] parameterstr;
	
	/** パラメータ一覧をキーと値に分け、格納したマップオブジェクト */
	protected Map<String, String> parameter = new HashMap<>();
	
	/**
	 * <p>コンストラクタ</p>
	 * パラメータ一覧（「キー=値」形式の文字列を複数持つ配列）を元に、パラメータ一覧を表すクラスのインスタンスを生成します。
	 * @param parameters パラメータ一覧
	 * @throws DataStoreExporterException　引数に指定された文字列に「キー=値」形式以外の文字列が含まれていた場合
	 */
	public Parameters(String[] parameters) throws DataStoreExporterException {
		if (parameters != null) {
			this.parameterstr = parameters;
			for(String paramStr : parameters) {
				Matcher variableMatcher = variableFormat.matcher(paramStr);
				if (variableMatcher.find()) {
					String name  = variableMatcher.group(1);
					String value = variableMatcher.group(2);
					parameter.put(name, value);
				} else {
					throw new DataStoreExporterException(DataStoreExporterMessage.PARAMETER_FORMAT_IS_INVALID, paramStr);
				}
			}
		}
	}
	
	/**
	 * パラメータ一覧を表すマップオプジェクトを返却します。
	 * @return パラメータ一覧を表すマップオプジェクト
	 */
	public Map<String, String> getParameter() {
		return new HashMap<>(this.parameter);
	}
	
	@Override
	public String toString() {
		return this.parameter.toString();
	}
}
