package jp.co.dk.datastoremanager.command.exporter;

import jp.co.dk.datastoremanager.core.exception.DataStoreExporterException;
import static jp.co.dk.datastoremanager.core.message.DataStoreExporterMessage.*;

public class Variable {
	
	/** 変数名 */
	protected String name;
	
	/** 変数型 */
	protected VariableType type;
	
	Variable(String name, String type) throws DataStoreExporterException {
		if (name == null || name.equals("")) new DataStoreExporterException(VALIABLE_IS_NOT_SET);
		this.name = name;
		this.type = VariableType.getType(type);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Variable other = (Variable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}


