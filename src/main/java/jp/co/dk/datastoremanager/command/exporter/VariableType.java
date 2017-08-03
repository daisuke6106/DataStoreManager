package jp.co.dk.datastoremanager.command.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.dk.datastoremanager.core.exception.DataStoreExporterException;
import static jp.co.dk.datastoremanager.core.message.DataStoreExporterMessage.*;

public enum VariableType {
	
	/** 文字列 */
	CHAR("CHAR", new ParameterSetter(){}),
	
	/** 数値 */
	NUM ("NUM" , new ParameterSetter(){
		private Pattern regex = Pattern.compile("^[0-9]+$");
		@Override
		public void setParameter(Sql sql, String value) throws DataStoreExporterException {
			Matcher matcher = this.regex.matcher(value);
			if (!matcher.find()) throw new DataStoreExporterException(VALIABLE_IS_CHECK_ERROR);
			sql.setParameter(Integer.parseInt(value));
		}
	}),
	
	/** 日付 */
	DATE("DATE", new ParameterSetter(){
		private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		private Pattern          regex      = Pattern.compile("^\\d{4}\\d{1,2}\\d{1,2}$");
		@Override
		public void setParameter(Sql sql, String value) throws DataStoreExporterException {
			Matcher matcher = this.regex.matcher(value);
			if (!matcher.find()) throw new DataStoreExporterException(VALIABLE_IS_CHECK_ERROR);
			try {
				sql.setParameter(dateFormat.parse(value));
			} catch (ParseException e) {
				throw new DataStoreExporterException(VALIABLE_IS_CHECK_ERROR);
			}
		}
	}),
	
	;
	
	private String typeStr;
	
	private ParameterSetter paramSetter;
	
	private VariableType(String typeStr, ParameterSetter paramSetter) {
		this.typeStr     = typeStr;
		this.paramSetter = paramSetter;
	}
	
	public static VariableType getType(String typeStr) throws DataStoreExporterException {
		for (VariableType type : VariableType.values()) if (type.typeStr.equals(typeStr)) return type;
		throw new DataStoreExporterException(VALIABLE_TYPE_IS_UNKNOWN);
	}
	
	void setParameter(Sql sql, String value) throws DataStoreExporterException {
		this.paramSetter.setParameter(sql, value);
	}
}

interface ParameterSetter {
	default void setParameter(Sql sql, String value) throws DataStoreExporterException {
		sql.setParameter(value);
	}
}