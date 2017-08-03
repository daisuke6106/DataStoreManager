package jp.co.dk.datastoremanager.core.rdb;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.SqlParameter;
import jp.co.dk.datastoremanager.core.rdb.TimestampSqlParameter;

class TimestampSqlParameter extends SqlParameter{
	
	protected Timestamp parameter;
	
	TimestampSqlParameter(Timestamp parameter) {
		this.parameter = parameter;
	} 

	@Override
	void set(int index, PreparedStatement statement) throws DataStoreManagerException {
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
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof TimestampSqlParameter)) return false;
		TimestampSqlParameter thisClassObj = (TimestampSqlParameter) object;
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
		if (this.parameter != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(this.parameter).append("(timestamp)");
			return sb.toString();
		} else {
			return "null(timestamp)";
		}
		
	}
}