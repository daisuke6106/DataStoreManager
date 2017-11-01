package jp.co.dk.datastoremanager.core.rdb;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.AN_EXCEPTION_OCCURRED_WHEN_PERFORMING_THE_SET_PARAMETERS_TO_SQL;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.IntColumnData;
import jp.co.dk.datastoremanager.core.rdb.SqlParameter;

public class IntColumnData implements ColumnData, SqlParameter{
	
	protected int parameter;
	
	IntColumnData(int parameter) {
		this.parameter = parameter;
	}

	@Override
	public void set(int index, PreparedStatement statement) throws DataStoreManagerException {
		try {
			statement.setInt(index, this.parameter);
		} catch (SQLException e) {
			throw new DataStoreManagerException(AN_EXCEPTION_OCCURRED_WHEN_PERFORMING_THE_SET_PARAMETERS_TO_SQL, e);
		}
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof IntColumnData)) return false;
		IntColumnData thisClassObj = (IntColumnData) object;
		if (thisClassObj.hashCode() == this.hashCode()) return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.parameter * 17;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.parameter).append("(int)");
		return sb.toString();
	}
}
