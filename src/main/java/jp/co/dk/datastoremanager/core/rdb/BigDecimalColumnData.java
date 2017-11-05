package jp.co.dk.datastoremanager.core.rdb;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.AN_EXCEPTION_OCCURRED_WHEN_PERFORMING_THE_SET_PARAMETERS_TO_SQL;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.BigDecimalColumnData;
import jp.co.dk.datastoremanager.core.rdb.SqlParameter;

public class BigDecimalColumnData implements ColumnData, SqlParameter{
	
	protected BigDecimal parameter;
	
	BigDecimalColumnData(BigDecimal parameter) {
		this.parameter = parameter;
	}

	public BigDecimal get() {
		return this.parameter;
	}
	
	@Override
	public void set(int index, PreparedStatement statement) throws DataStoreManagerException {
		try {
			statement.setBigDecimal(index, this.parameter);
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
		if (!(object instanceof BigDecimalColumnData)) return false;
		BigDecimalColumnData thisClassObj = (BigDecimalColumnData) object;
		if (thisClassObj.hashCode() == this.hashCode()) return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int) (this.parameter.hashCode() * 17L);
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
