package jp.co.dk.datastoremanager.core.rdb;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.SqlParameter;
import jp.co.dk.datastoremanager.core.rdb.TimestampColumnData;

public class TimestampColumnData implements ColumnData, SqlParameter{
	
	protected Timestamp parameter;
	
	TimestampColumnData(Timestamp parameter) {
		this.parameter = parameter;
	} 

	@Override
	public void set(int index, PreparedStatement statement) throws DataStoreManagerException {
		try {
			if (this.parameter != null) {
				statement.setTimestamp(index, this.parameter);
			} else {
				statement.setTimestamp(index, null);
			}
		} catch (SQLException e) {
			throw new DataStoreManagerException(AN_EXCEPTION_OCCURRED_WHEN_PERFORMING_THE_SET_PARAMETERS_TO_SQL, e);
		}
	}
	
	public Timestamp get() {
		return this.parameter;
	}
	
	@Override
	public String getDataByString() {
		return this.toString();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof TimestampColumnData)) return false;
		TimestampColumnData thisClassObj = (TimestampColumnData) object;
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