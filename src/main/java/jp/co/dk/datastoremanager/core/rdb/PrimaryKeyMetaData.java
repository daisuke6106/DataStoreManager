package jp.co.dk.datastoremanager.core.rdb;

public class PrimaryKeyMetaData {
	
	protected String tableName;

	protected String pkName;
	
	protected short keySeq;
	
	protected String columnName;
	
	PrimaryKeyMetaData(String tableName, String pkName, short keySeq, String columnName) {
		this.tableName  = tableName;
		this.pkName     = pkName;
		this.keySeq     = keySeq;
		this.columnName = columnName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result + keySeq;
		result = prime * result + ((pkName == null) ? 0 : pkName.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrimaryKeyMetaData other = (PrimaryKeyMetaData) obj;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (keySeq != other.keySeq)
			return false;
		if (pkName == null) {
			if (other.pkName != null)
				return false;
		} else if (!pkName.equals(other.pkName))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PrimaryKeyMetaData [tableName=");
		builder.append(tableName);
		builder.append(", pkName=");
		builder.append(pkName);
		builder.append(", keySeq=");
		builder.append(keySeq);
		builder.append(", columnName=");
		builder.append(columnName);
		builder.append("]");
		return builder.toString();
	}
	
	
}
