package jp.co.dk.datastoremanager.core.rdb;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.AN_EXCEPTION_OCCURRED_WHEN_PERFORMING_THE_SET_PARAMETERS_TO_SQL;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.LongColumnData;
import jp.co.dk.datastoremanager.core.rdb.SqlParameter;

public class LongColumnData implements ColumnData, SqlParameter{
	
	protected long parameter;
	
	LongColumnData(long parameter) {
		this.parameter = parameter;
	}

	@Override
	public void set(int index, PreparedStatement statement) throws DataStoreManagerException {
		try {
			statement.setLong(index, this.parameter);
		} catch (SQLException e) {
			throw new DataStoreManagerException(AN_EXCEPTION_OCCURRED_WHEN_PERFORMING_THE_SET_PARAMETERS_TO_SQL, e);
		}
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof LongColumnData)) return false;
		LongColumnData thisClassObj = (LongColumnData) object;
		if (thisClassObj.hashCode() == this.hashCode()) return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int) ((int)this.parameter * 17L);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.parameter).append("(long)");
		return sb.toString();
	}
}
