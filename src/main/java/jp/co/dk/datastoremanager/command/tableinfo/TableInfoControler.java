package jp.co.dk.datastoremanager.command.tableinfo;

import jp.co.dk.datastoremanager.command.AbtractCommandControler;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.rdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.core.rdb.TableMetaData;

import org.apache.commons.cli.Options;

public class TableInfoControler extends AbtractCommandControler {
	
	@Override
	public void execute(DataBaseAccessParameter dataBaseAccessParameter) {
		
		try {
			DataBaseDataStore dataStore = (DataBaseDataStore)dataBaseAccessParameter.createDataStore();
			dataStore.startTransaction();
			for (TableMetaData tableMetaData : dataStore.getTables()) System.out.println(tableMetaData.toString());
		} catch (DataStoreManagerException e) {
			System.out.println(e.toString());
			System.exit(1);
		}
		
	}
	
	@Override
	protected String getCommandName() {
		return "tableinfo";
	}

	@Override
	protected void getAnyOptions(Options options) {
		
	}
	
	public static void main(String[] args) {
		TableInfoControler controler = new TableInfoControler();
		controler.execute(args);
	}
}
