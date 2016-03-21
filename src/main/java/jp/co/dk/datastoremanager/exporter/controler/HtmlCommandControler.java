package jp.co.dk.datastoremanager.exporter.controler;

import java.io.IOException;

import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreExporterException;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.exporter.Parameters;
import jp.co.dk.datastoremanager.exporter.SqlFile;
import jp.co.dk.datastoremanager.exporter.html.HtmlDBData;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;
import jp.co.dk.datastoremanager.rdb.AbstractDataBaseAccessObject;
import jp.co.dk.property.exception.PropertyException;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class HtmlCommandControler extends AbtractCommandControler {
	
	@Override
	public void execute() {

		try (DataStoreManager dataStoreManager = new DataStoreManager(new DataStoreManagerProperty())) {
			
			java.io.File sqlFile    = new java.io.File(this.cmd.getOptionValue("f"));
			java.io.File outputFile = new java.io.File(this.cmd.getOptionValue("o"));
			Parameters   parameter  = new Parameters(this.cmd.getOptionValues("p"));
			
			dataStoreManager.startTrunsaction();
			AbstractDataBaseAccessObject dao = (AbstractDataBaseAccessObject)dataStoreManager.getDataAccessObject("default");
			
			SqlFile    sqlfile  = new SqlFile(sqlFile);
			sqlfile.setParameter(parameter);
			
			HtmlDBData htmlDBData = HtmlDBData.auto(outputFile, dao);
			htmlDBData.write(sqlfile);
			htmlDBData.write();
		} catch (DataStoreExporterException | DataStoreManagerException | PropertyException | IOException e) {
			System.out.println(e.toString());
			System.exit(1);
		}
	}
	
	
	@Override
	protected String getCommandName() {
		return "db_to_excel";
	}

	@Override
	protected void getOptions(Options options) {
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true).withArgName("file"     ).withDescription("実行対象のSQLが記載されたファイル")	.withLongOpt("sql_file" ).create("f"));
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true).withArgName("output"   ).withDescription("出力対象先"                      )	.withLongOpt("output"   ).create("o"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(true).withArgName("parameter").withDescription("SQLのパラメータ"                 )	.withLongOpt("parameter").create("p"));
	}
	
	public static void main(String[] args) {
		HtmlCommandControler controler = new HtmlCommandControler();
		controler.execute(args);
	}
}
