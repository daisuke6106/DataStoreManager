package jp.co.dk.datastoremanager.command.exporthtab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import jp.co.dk.datastoremanager.command.AbtractCommandControler;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.rdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.core.rdb.TableMetaData;
import jp.co.dk.datastoremanager.core.rdb.history.HistoryTableRecordList;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class ExportHistoryTableControler extends AbtractCommandControler {
	
	@Override
	public void execute(DataBaseAccessParameter dataBaseAccessParameter) {
		
		long time = 0 ;
		try {
			time = Long.parseLong(this.cmd.getOptionValue("tm")) * 1000L;
		} catch (NumberFormatException e) {
			System.out.println("tm is not number.");
			System.exit(1);
		}
		
		File outputFile = new File(this.cmd.getOptionValue("o"));
		if (outputFile.isDirectory()) {
			System.out.println(outputFile.toString() + " is directory.");
			System.exit(1);
		}
		if (outputFile.isFile()) {
			System.out.println(outputFile.toString() + " is exists file.");
			System.exit(1);
		}
		
		OutputStream ops = null;
		try {
			ops = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e1) {
			System.out.println(outputFile.toString() + " is not found.");
			System.exit(1);
		}
		
		try {
			DataBaseDataStore dataStore = (DataBaseDataStore)dataBaseAccessParameter.createDataStore();
			dataStore.startTransaction();
			
			Date dbDate = dataStore.getDataBaseTime();
			
			List<TableMetaData> tableMetaDataList = dataStore.getTables();
			for (TableMetaData tableMetaData : tableMetaDataList) {
				if (tableMetaData.isExistsHistoryTable()) {
					HistoryTableRecordList historyTableRecordList = tableMetaData.getHistoryTable().getRecordAfterSpecifiedDate(new Date( dbDate.getTime() - time ));
					historyTableRecordList.writeHtml(ops);
				}
			}
		} catch (DataStoreManagerException e) {
			System.out.println(e.toString());
			System.exit(1);
		}
		
	}
	
	
	@Override
	protected String getCommandName() {
		return "exp_htab";
	}

	@Override
	protected void getAnyOptions(Options options) {
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true).withDescription("出力先（ファイル名含む）").withLongOpt("output_file").create("o"));
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true).withDescription("取得時間(秒)")	.withLongOpt("time" ).create("tm"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(true).withDescription("取得対象テーブル").withLongOpt("target_table").create("tbl"));
		
	}
	
	public static void main(String[] args) {
		ExportHistoryTableControler controler = new ExportHistoryTableControler();
		controler.execute(args);
	}
}
