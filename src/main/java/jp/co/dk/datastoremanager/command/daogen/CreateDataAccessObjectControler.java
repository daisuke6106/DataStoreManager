package jp.co.dk.datastoremanager.command.daogen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import jp.co.dk.datastoremanager.command.AbtractCommandControler;
import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.core.rdb.DataBaseDataStore;
import jp.co.dk.datastoremanager.core.rdb.TableMetaData;
import jp.co.dk.document.exception.DocumentException;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class CreateDataAccessObjectControler extends AbtractCommandControler {
	
	@Override
	public void execute(DataBaseAccessParameter dataBaseAccessParameter) {

		try {
			DataBaseDataStore dataStore = (DataBaseDataStore)dataBaseAccessParameter.createDataStore();
			dataStore.startTransaction();
			TableMetaData tableMetaData = dataStore.getTable(this.cmd.getOptionValue("table"));
			if (tableMetaData == null) {
				System.out.println("table is not found.");
				System.exit(1);
			}
			TemplateFile  templateFile  = new TemplateFile(new FileInputStream(new java.io.File(this.cmd.getOptionValue("template"))));
			ColumnSetting columnSetting = templateFile.getColumnSetting();
			for (File file : templateFile.getFileElement()) {
				System.out.println(file.getContent(tableMetaData, columnSetting));
			}
			
		} catch (DataStoreManagerException | FileNotFoundException | DocumentException e) {
			System.out.println(e.toString());
			System.exit(1);
		}
		
	}
	
	
	@Override
	protected String getCommandName() {
		return "daogen";
	}

	@SuppressWarnings("static-access")
	@Override
	protected void getAnyOptions(Options options) {
		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withDescription("テンプレートファイルを指定する。").withLongOpt("template" ).create("tmp"));
//		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withDescription("出力先ディレクトリを指定する。").withLongOpt("output" ).create("o"));
//		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withDescription("パッケージを指定する。").withLongOpt("package" ).create("p"));
		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withDescription("対象テーブル名を指定する。").withLongOpt("table" ).create("t"));
	}
	
	public static void main(String[] args) {
		CreateDataAccessObjectControler controler = new CreateDataAccessObjectControler();
		controler.execute(args);
	}
}
