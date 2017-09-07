package jp.co.dk.datastoremanager.command.createhtab;

import java.util.List;

import jp.co.dk.datastoremanager.command.AbtractCommandControler;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.rdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.core.rdb.TableMetaData;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class CreateHistoryTableControler extends AbtractCommandControler {
	
	@Override
	public void execute(DataBaseAccessParameter dataBaseAccessParameter) {

		try {
			DataBaseDataStore dataStore = (DataBaseDataStore)dataBaseAccessParameter.createDataStore();
			dataStore.startTransaction();
			List<TableMetaData> tableMetaDataList = dataStore.getTables();
			
			if (this.cmd.hasOption("i")) {
				for (TableMetaData tableMetaData : tableMetaDataList) {
					if (tableMetaData.isExistsHistoryTable()) {
						System.out.println(tableMetaData.toString() + " : history table is exists.");
					} else {
						System.out.println(tableMetaData.toString() + " : history table is not exists.");
					}
				}
			} else if (this.cmd.hasOption("c")) {
				for (TableMetaData tableMetaData : tableMetaDataList) {
					if (tableMetaData.createHistoryTable()) {
						System.out.println(tableMetaData.toString() + " : history table create complete!");
					} else {
						System.out.println(tableMetaData.toString() + " : history table is exists.");
					}
					if (tableMetaData.createTriggerHistoryTable()) {
						System.out.println(tableMetaData.toString() + " : trigger for history table create complete!");
					} else {
						System.out.println(tableMetaData.toString() + " : trigger for history table is exists.");
					}
				}
			} else if (this.cmd.hasOption("d")) {
				for (TableMetaData tableMetaData : tableMetaDataList) {
					if (tableMetaData.dropHistoryTrigger()) {
						System.out.println(tableMetaData.toString() + " : trigger for history table delete complete!");
					} else {
						System.out.println(tableMetaData.toString() + " : trigger for history table is not exists.");
					}
					if (tableMetaData.dropHistoryTable()) {
						System.out.println(tableMetaData.toString() + " : history table delete complete!");
					} else {
						System.out.println(tableMetaData.toString() + " : history table is not exists.");
					}
				}
			}
			
		} catch (DataStoreManagerException e) {
			System.out.println(e.toString());
			System.exit(1);
		}
		
	}
	
	
	@Override
	protected String getCommandName() {
		return "histtab";
	}

	@Override
	protected void getAnyOptions(Options options) {
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withDescription("ヒストリーテーブルの状態を確認する")	.withLongOpt("info" ).create("i"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withDescription("ヒストリーテーブルを作成する").withLongOpt("create").create("c"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withDescription("ヒストリーテーブルを削除する").withLongOpt("drop").create("d"));
		
	}
	
	public static void main(String[] args) {
		CreateHistoryTableControler controler = new CreateHistoryTableControler();
		controler.execute(args);
	}
}
