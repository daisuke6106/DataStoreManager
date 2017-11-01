package jp.co.dk.datastoremanager.core.rdb;

import static jp.co.dk.datastoremanager.core.message.DataStoreManagerMessage.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.BytesColumnData;
import jp.co.dk.datastoremanager.core.rdb.SqlParameter;

public class BytesColumnData implements ColumnData, SqlParameter {
	
	protected byte[] parameter;
	
	BytesColumnData(byte[] parameter) {
		this.parameter = parameter;
	} 
	
	BytesColumnData(Object parameter) throws DataStoreManagerException {
		if (parameter != null) {
			try {
				ByteArrayOutputStream baos= new ByteArrayOutputStream();  
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(parameter);
				this.parameter = baos.toByteArray();
				baos.close();
				oos.close();
			} catch (IOException e) {
				throw new DataStoreManagerException(FAILE_TO_AN_ATTEMPT_WAS_MADE_TO_CONVERT_TO_BYTE_ARRAY, e);
			}
		}
	}

	@Override
	public void set(int index, PreparedStatement statement) throws DataStoreManagerException {
		try {
			if (this.parameter != null) {
				statement.setBytes(index, this.parameter);
			} else {
				statement.setBytes(index, null);
			}
		} catch (SQLException e) {
			throw new DataStoreManagerException(AN_EXCEPTION_OCCURRED_WHEN_PERFORMING_THE_SET_PARAMETERS_TO_SQL, e);
		}
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof BytesColumnData)) return false;
		BytesColumnData thisClassObj = (BytesColumnData) object;
		if (thisClassObj.hashCode() == this.hashCode()) return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		int result = -1;
		if (this.parameter != null) {
			for (int i=0; i<this.parameter.length; i++) {
				result*=this.parameter[i];
			}
			return result * 17;
		} else {
			return result;
		}
		
	}
	
	@Override
	public String toString() {
		if (this.parameter != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i< this.parameter.length; i++) {
				sb.append(this.parameter[i]).append(',');
			}
			StringBuilder result = new StringBuilder(sb.substring(0, sb.length()-1));
			result.append("(byte[])");
			return result.toString();
		} else {
			return "null(byte[])";
		}
	}
}