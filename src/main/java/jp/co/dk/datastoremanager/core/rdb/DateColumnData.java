package jp.co.dk.datastoremanager.core.rdb;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.AN_EXCEPTION_OCCURRED_WHEN_PERFORMING_THE_SET_PARAMETERS_TO_SQL;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Date;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.DateColumnData;
import jp.co.dk.datastoremanager.core.rdb.SqlParameter;

public class DateColumnData implements ColumnData, SqlParameter{
	
	protected Date parameter;
	
	DateColumnData(Date parameter) {
		this.parameter = parameter;
	}

	public Date get() {
		return this.parameter;
	}
	
	@Override
	public void set(int index, PreparedStatement statement) throws DataStoreManagerException {
		try {
			if (this.parameter != null) {
				statement.setDate(index, new java.sql.Date(this.parameter.getTime()));
			} else {
				statement.setDate(index, null);
			}
		} catch (SQLException e) {
			throw new DataStoreManagerException(AN_EXCEPTION_OCCURRED_WHEN_PERFORMING_THE_SET_PARAMETERS_TO_SQL, e);
		}
	}

	@Override
	public String getDataByString() {
		return this.toString();
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof DateColumnData)) return false;
		DateColumnData thisClassObj = (DateColumnData) object;
		if (thisClassObj.hashCode() == this.hashCode()) return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		if (this.parameter != null) {
			return this.parameter.hashCode() * 17;
		} else {
			return -1;
		}
	}
	
	@Override
	public String toString() {
		return this.toString(DateFormat.YYYYMMDD_HH24MISS);
	}
	
	public String toString(DateFormat format) {
		if (this.parameter != null) {
			return format.parse(this.parameter);
		} else {
			return "NULL";
		}
	}
	
	public enum DateFormat {
		YYYYMMDD(new SimpleDateFormat("yyyy/MM/dd")),
		YYYYMMDD_HH24MISS(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")),
		;
		protected SimpleDateFormat format;
		private DateFormat(SimpleDateFormat format) {
			this.format = format;
		}
		String parse(Date date) {
			return this.format.format(date);
		}
	}
}