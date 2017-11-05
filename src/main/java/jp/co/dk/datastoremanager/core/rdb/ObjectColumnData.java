package jp.co.dk.datastoremanager.core.rdb;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.ObjectColumnData;
import jp.co.dk.datastoremanager.core.rdb.SqlParameter;

public class ObjectColumnData implements ColumnData, SqlParameter{
	
	protected Serializable parameter;
	
	ObjectColumnData(Serializable parameter) {
		this.parameter = parameter;
	} 

	public Serializable get() {
		return this.parameter;
	}
	
	@Override
	public void set(int index, PreparedStatement statement) throws DataStoreManagerException {
		try {
			if (this.parameter != null) {
				statement.setObject(index, this.parameter);
			} else {
				statement.setObject(index, null);
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
		if (!(object instanceof ObjectColumnData)) return false;
		ObjectColumnData thisClassObj = (ObjectColumnData) object;
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
			sb.append(this.parameter);
			return sb.toString();
		} else {
			return "NULL";
		}
	}
}