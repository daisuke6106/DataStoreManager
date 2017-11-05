package jp.co.dk.datastoremanager.core.rdb.history;

public enum OperationType {
	
	IN("INSERT"), 
	U1("UPDATE(BEFORE)"), 
	U2("UPDATE(AFTER)"), 
	DL("DELETE");
	
	private String name;
	
	private OperationType(String name) {
		this.name = name;
	}
	
	String getName() {
		return this.name;
	}
}
