# -*- coding: utf-8 -*-
import sys
import os
import shutil
sys.path.append("datastoremanager_1.2.4_all.jar")

# sysモジュールをリロードする
reload(sys)
# デフォルトの文字コードを変更する．
sys.setdefaultencoding('utf-8')

import jp.co.dk.datastoremanager.core.rdb.oracle.OracleDataBaseDataStore as OracleDataBaseDataStore
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter as DataBaseAccessParameter
import jp.co.dk.datastoremanager.core.DataStoreKind as DataStoreKind
import jp.co.dk.datastoremanager.core.DataBaseDriverConstants as DataBaseDriverConstants
import jp.co.dk.datastoremanager.core.rdb.TableMetaData as TableMetaData


class ClassFile:
	
	def contents(self):
		pass
	
	def write(self, dirpath):
		filepath = dirpath + "/" + self.classname() + "." + self.extension()
		if os.path.exists(filepath) :
			os.remove(filepath)
		f = open(dirpath + "/" + self.classname() + "." + self.extension(), "w")
		f.write(self.contents())
		f.close()
	
	def classname(self):
		return ""
	
	def extension(self):
		return ""
	
class JavaClassFile(ClassFile):
	def __init__(self, package, table):
		self.package = package
		self.table   = table
		
	def write(self, dirpath):
		
		package_path = package.replace(".","/")
		
		if (package_path[-1:]=="/"):
			package_path = package_path[:-1]
		
		package_path = dirpath + "/" + package_path
		
		if os.path.exists(package_path) and os.path.isfile(package_path) :
			print("[WARN]package_path is file, exists already. package_path=[" + package_path + "]" )
			sys.exit(1) 
		elif os.path.exists(package_path) and os.path.isdir(package_path) :
			print("[WARN]package_path is dir, exists already. package_path=[" + package_path + "]" )
		else :
			os.makedirs(package_path)
		
		filepath = package_path + "/" + self.classname() + "." + self.extension()
		if os.path.exists(filepath) :
			os.remove(filepath)
		f = open(package_path + "/" + self.classname() + "." + self.extension(), "w")
		f.write(self.contents())
		f.close()
		
		print("[INFO]Complete create file, filepath=[" + filepath + "]" )
	
	def convertOracleColumnToJavaObject(self, column):
		column_type = column.getColumnType()
		if column_type in {"CHAR", "NCHAR", "VARCHAR2", "NVARCHAR2", "CLOB"}:
			return "java.lang.String"
		elif column_type in {"NUMBER"}:
			return "java.math.BigDecimal"
		elif column_type in {"DATE"}:
			return "java.sql.Date"
		elif column_type in {"TIMESTAMP(6)"}:
			return "java.sql.Timestamp"
		elif column_type in {"BLOB"}:
			return "byte[]"
		else:
			raise Exception("unknown column_type=[" + column_type + "]")

	def convertOracleColumnToGetter(self, column):
		column_type = column.getColumnType()
		if column_type in {"CHAR", "NCHAR", "VARCHAR2", "NVARCHAR2", "CLOB"}:
			return "getString"
		elif column_type in {"NUMBER"}:
			return "getBigDecimal"
		elif column_type in {"DATE"}:
			return "getDate"
		elif column_type in {"TIMESTAMP(6)"}:
			return "getTimestamp"
		elif column_type in {"BLOB"}:
			return "getBytes"
		else:
			raise Exception("unknown column_type=[" + column_type + "]")

	def extension(self):
		return "java"
	

class JavaRecordClass(JavaClassFile):
	
	def classname(self):
		return self.table.toString() + "Record"
	
	def contents(self):
		
		tableName       = self.table.toString()
		recordClassName = self.classname()
		
		contentsstr = ""
		contentsstr = contentsstr + "package " + self.package + ";\n"
		contentsstr = contentsstr + "\n"
		contentsstr = contentsstr + "import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;\n"
		contentsstr = contentsstr + "import jp.co.dk.datastoremanager.core.rdb.DataBaseRecord;\n"
		contentsstr = contentsstr + "import jp.co.dk.datastoremanager.core.rdb.DataConvertable;\n"
		contentsstr = contentsstr + "\n"
		contentsstr = contentsstr + "/**" + "\n"
		contentsstr = contentsstr + " * This class is DataBaseRecord Object to Oracle of [" + tableName + "] Table." + "\n"
		contentsstr = contentsstr + " * This class is auto generate class." + "\n"
		contentsstr = contentsstr + " * " + "\n"
		contentsstr = contentsstr + " * このクラスは [" + tableName + "] テーブルのレコードを表すクラスです。" + "\n"
		contentsstr = contentsstr + " * このクラスは自動生成クラスです。" + "\n"
		contentsstr = contentsstr + " */" + "\n"
		contentsstr = contentsstr + "public class " + recordClassName + " implements DataConvertable {" + "\n"
		contentsstr = contentsstr + "\n"
		
		# フィールド定義
		for column in self.table.getColumns():
			contentsstr = contentsstr + "\t/** [" + column.getColumnname() + "] set flg. */" + "\n"
			contentsstr = contentsstr + "\tprivate " + "boolean isset_" + column.getColumnname() + " = false;" + "\n"
			contentsstr = contentsstr + "\n"
			contentsstr = contentsstr + "\t/** [" + column.getColumnname() + "] Data. */" + "\n"
			contentsstr = contentsstr + "\tprivate " + self.convertOracleColumnToJavaObject(column) + " " + column.getColumnname() + ";" + "\n"
			contentsstr = contentsstr + "\n"
		
		# GETTER, SETTER定義
		for column in self.table.getColumns():
			contentsstr = contentsstr + "\t/**" + "\n"
			contentsstr = contentsstr + "\t * It is judged whether [" + column.getColumnname() + "] column has been set. " + "\n"
			contentsstr = contentsstr + "\t * Use the setter to set true if a value has been set for this instance, false if it is not set." + "\n"
			contentsstr = contentsstr + "\t * " + "\n"
			contentsstr = contentsstr + "\t * [" + column.getColumnname() + "] カラムが設定済みか判定します。" + "\n"
			contentsstr = contentsstr + "\t * セッターを使用し本インスタンスに値が設定されていた場合true、設定されていない場合falseを設定する。" + "\n"
			contentsstr = contentsstr + "\t */" + "\n"
			contentsstr = contentsstr + "\tpublic" + " boolean isSet" + column.getColumnname() + "(){" + "\n"
			contentsstr = contentsstr + "\t\t" + "return this.isset_" + column.getColumnname() + ";\n"
			contentsstr = contentsstr + "\t}\n"
			contentsstr = contentsstr + "\n"

			contentsstr = contentsstr + "\t/**" + "\n"
			contentsstr = contentsstr + "\t * Set a value in the [" + column.getColumnname() + "] column." + "\n"
			contentsstr = contentsstr + "\t * " + "\n"
			contentsstr = contentsstr + "\t * [" + column.getColumnname() + "] カラムに値を設定する。" + "\n"
			contentsstr = contentsstr + "\t */" + "\n"
			contentsstr = contentsstr + "\tpublic" + " void" + " set" + column.getColumnname() + " ("+ self.convertOracleColumnToJavaObject(column) + " value)" + "{" + "\n"
			contentsstr = contentsstr + "\t\t" + "this.isset_" + column.getColumnname() + " = true;" + "\n"
			contentsstr = contentsstr + "\t\t" + "this." + column.getColumnname() + " = value;\n"
			contentsstr = contentsstr + "\t}\n"
			contentsstr = contentsstr + "\n"

			contentsstr = contentsstr + "\t/**" + "\n"
			contentsstr = contentsstr + "\t * Gets the value of the [" + column.getColumnname() + "] column." + "\n"
			contentsstr = contentsstr + "\t * " + "\n"
			contentsstr = contentsstr + "\t * [" + column.getColumnname() + "] カラムの値を取得します。" + "\n"
			contentsstr = contentsstr + "\t */" + "\n"
			contentsstr = contentsstr + "\tpublic" + " " + self.convertOracleColumnToJavaObject(column) + " get" + column.getColumnname() + "(){" + "\n"
			contentsstr = contentsstr + "\t\t" + "return this." + column.getColumnname() + ";\n"
			contentsstr = contentsstr + "\t}\n"
			contentsstr = contentsstr + "\n"
		
		# convert
		contentsstr = contentsstr + "\t@Override" + "\n"
		contentsstr = contentsstr + "\tpublic" + " DataConvertable convert(DataBaseRecord dataBaseRecord) throws DataStoreManagerException {" + "\n"
		contentsstr = contentsstr + "\t\t" + recordClassName + " record = new " + recordClassName + "();" + "\n"
		for column in self.table.getColumns():
			contentsstr = contentsstr + "\t\t" + "record." + column.getColumnname() + " = dataBaseRecord." + self.convertOracleColumnToGetter(column) + "(\"" + column.getColumnname() + "\").get()" + ";\n"
		contentsstr = contentsstr + "\t\treturn record;\n"
		contentsstr = contentsstr + "\t}\n"
		contentsstr = contentsstr + "\n"
		
		# toString
		contentsstr = contentsstr + "\t@Override" + "\n"
		contentsstr = contentsstr + "\tpublic" + " String toString() {" + "\n"
		contentsstr = contentsstr + "\t\tStringBuilder str = new StringBuilder();" + "\n"
		contentsstr = contentsstr + "\t\tstr.append(\"" + recordClassName + " [\");" + "\n"
		for column in self.table.getColumns():
			contentsstr = contentsstr + "\t\t" + "if ( this.isset_" + column.getColumnname() + " ) " + "str.append(\"" + column.getColumnname() + "=\").append(this." + column.getColumnname() + ");\n" 
		contentsstr = contentsstr + "\t\tstr.append(\"]\");" + "\n"
		contentsstr = contentsstr + "\t\treturn str.toString();" + "\n"
		contentsstr = contentsstr + "\t}\n"
		contentsstr = contentsstr + "\n"
		
		
		contentsstr = contentsstr + "\n"
		contentsstr = contentsstr + "}" + "\n"
		return contentsstr

class JavaDaoClass(JavaClassFile):

	def classname(self):
		return self.table.toString() + "Dao"
	
	def contents(self):
		
		tableName       = self.table.toString()
		daoClassName    = self.classname()
		recordClassName = tableName + "Record"
		
		# all columns
		all_columns = ""
		for column in self.table.getColumns():
				all_columns = all_columns + column.getColumnname() + ","
		all_columns = all_columns[:-1]

		# all all_columns_question
		all_columns_question = ""
		for column in self.table.getColumns():
				all_columns_question = all_columns_question + "?,"
		all_columns_question = all_columns_question[:-1]

		# all_columns_columns_java_args
		all_columns_columns_java_args = ""
		for column in self.table.getColumns():
				all_columns_columns_java_args = all_columns_columns_java_args + self.convertOracleColumnToJavaObject(column) + " " + column.getColumnname() + ","
		all_columns_columns_java_args = all_columns_columns_java_args[:-1]

		# primarykey columns
		primarykey_columns = ""
		for column in self.table.getColumns():
			if column.isPrimaryKey() :
				primarykey_columns = primarykey_columns + column.getColumnname() + ","
		primarykey_columns = primarykey_columns[:-1]
		
		# primarykey_columns_where_args
		primarykey_columns_where_args = ""
		for column in self.table.getColumns():
			if column.isPrimaryKey() :
				primarykey_columns_where_args = primarykey_columns_where_args + column.getColumnname() + " = ? AND"
		primarykey_columns_where_args = primarykey_columns_where_args[:-4]
		
		# primarykey_columns_java_args
		primarykey_columns_java_args = ""
		for column in self.table.getColumns():
			if column.isPrimaryKey() :
				primarykey_columns_java_args = primarykey_columns_java_args + self.convertOracleColumnToJavaObject(column) + " " + column.getColumnname() + ","
		primarykey_columns_java_args = primarykey_columns_java_args[:-1]
		
		
		contentsstr = ""
		contentsstr = contentsstr + "package " + self.package + ";\n"
		contentsstr = contentsstr + "\n"
		
		contentsstr = contentsstr + "import jp.co.dk.datastoremanager.core.DataStore;\n"
		contentsstr = contentsstr + "import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;\n"
		contentsstr = contentsstr + "import jp.co.dk.datastoremanager.core.rdb.AbstractDataBaseAccessObject;\n"
		contentsstr = contentsstr + "import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter;\n"
		contentsstr = contentsstr + "import jp.co.dk.datastoremanager.core.rdb.Sql;\n"
		contentsstr = contentsstr + "\n"
		contentsstr = contentsstr + "/**" + "\n"
		contentsstr = contentsstr + " * This class is DataAccessObject to Oracle of [" + tableName + "] Table." + "\n"
		contentsstr = contentsstr + " * This class is auto generate class." + "\n"
		contentsstr = contentsstr + " * This class has SELECT method By Primary Key, INSERT method, UPDATE method by Primary Key, DELETE method by Primary Key" + "\n"
		contentsstr = contentsstr + " * When you want to you own SQL to This Table, extend this class and create new Method." + "\n"
		contentsstr = contentsstr + " * This class dependents DataStoreManage Library. Please add DataStoreManage JarFile and JDBC jar" + "\n"
		contentsstr = contentsstr + " */" + "\n"
		contentsstr = contentsstr + "public class " + daoClassName + " extends AbstractDataBaseAccessObject {" + "\n"
		contentsstr = contentsstr + "\n"

		# constractor
		contentsstr = contentsstr + "\t/**" + "\n"
		contentsstr = contentsstr + "\t * This is Constractor by DataBaseAccessParameter." + "\n"
		contentsstr = contentsstr + "\t * @param dataBaseAccessParameter instance of DataBase Access Parameter" + "\n"
		contentsstr = contentsstr + "\t */" + "\n"
		contentsstr = contentsstr + "\tpublic " + daoClassName + "(DataBaseAccessParameter dataBaseAccessParameter) throws DataStoreManagerException {\n"
		contentsstr = contentsstr + "\t\tsuper(dataBaseAccessParameter);\n"
		contentsstr = contentsstr + "\t}\n"
		contentsstr = contentsstr + "\n"
		
		contentsstr = contentsstr + "\t/**" + "\n"
		contentsstr = contentsstr + "\t * This is Constractor by DataStore." + "\n"
		contentsstr = contentsstr + "\t * @param dataBaseAccessParameter instance of DataStore" + "\n"
		contentsstr = contentsstr + "\t */" + "\n"
		contentsstr = contentsstr + "\tpublic " + daoClassName + "(DataStore dataStore) throws DataStoreManagerException {\n"
		contentsstr = contentsstr + "\t\tsuper(dataStore);\n"
		contentsstr = contentsstr + "\t}\n"
		contentsstr = contentsstr + "\n"
		
		# select
		contentsstr = contentsstr + "\tpublic" + " " + recordClassName + " select(" + primarykey_columns_java_args + ") throws DataStoreManagerException {" + "\n"
		contentsstr = contentsstr + "\t\tSql sql = new Sql(\"SELECT " + all_columns + " FROM " + tableName + " WHERE " + primarykey_columns_where_args + "\");\n"
		for column in self.table.getColumns():
			if column.isPrimaryKey() :
				contentsstr = contentsstr + "\t\tsql.setParameter(" + column.getColumnname() + ");\n"
		contentsstr = contentsstr + "\t\treturn selectSingle(sql, new " + recordClassName + "());\n"
		contentsstr = contentsstr + "\t}" + "\n"
		contentsstr = contentsstr + "\n"
		
		# insert
		contentsstr = contentsstr + "\tpublic void insert(" + all_columns_columns_java_args + ") throws DataStoreManagerException {" + "\n"
		contentsstr = contentsstr + "\t\tSql sql = new Sql(\"INSERT INTO " + tableName + "(" + all_columns + ") VALUES (" + all_columns_question + ")\");\n"
		for column in self.table.getColumns():
			contentsstr = contentsstr + "\t\tsql.setParameter(" + column.getColumnname() + ");\n"
		contentsstr = contentsstr + "\t\tinsert(sql);\n"
		contentsstr = contentsstr + "\t}" + "\n"
		contentsstr = contentsstr + "\n"
		
		# update
		contentsstr = contentsstr + "\tpublic int update(" + recordClassName + " updateRecord, " + primarykey_columns_java_args + ") throws DataStoreManagerException {" + "\n"
		contentsstr = contentsstr + "\t\tSql sql = new Sql(\"UPDATE " + tableName + "\");\n" 
		contentsstr = contentsstr + "\t\tjava.util.StringJoiner setToken = new java.util.StringJoiner(\",\");\n"
		for column in self.table.getColumns():
			contentsstr = contentsstr + "\t\tif (updateRecord.isSet" + column.getColumnname() + "()) {\n"
			contentsstr = contentsstr + "\t\t\tsetToken.add(\"" + column.getColumnname() + " = ?\");\n"
			contentsstr = contentsstr + "\t\t\tsql.setParameter(updateRecord.get" + column.getColumnname() + "());\n"
			contentsstr = contentsstr + "\t\t}\n"
		contentsstr = contentsstr + "\t\tsql.add(\" SET \");\n"
		contentsstr = contentsstr + "\t\tsql.add(setToken.toString());\n"
		contentsstr = contentsstr + "\t\tsql.add(\" WHERE " + primarykey_columns_where_args + "\");\n"
		for column in self.table.getColumns():
			if column.isPrimaryKey() :
				contentsstr = contentsstr + "\t\tsql.setParameter(" + column.getColumnname() + ");\n"
		contentsstr = contentsstr + "\t\treturn update(sql);\n"
		contentsstr = contentsstr + "\t}" + "\n"
		contentsstr = contentsstr + "\n"
		
		# delete
		contentsstr = contentsstr + "\tpublic int delete(" + primarykey_columns_java_args + ") throws DataStoreManagerException {" + "\n"
		contentsstr = contentsstr + "\t\tSql sql = new Sql(\"DELETE FROM " + tableName + " WHERE " + primarykey_columns_where_args + "\");\n"
		for column in self.table.getColumns():
			if column.isPrimaryKey() :
				contentsstr = contentsstr + "\t\tsql.setParameter(" + column.getColumnname() + ");\n"
		contentsstr = contentsstr + "\t\treturn delete(sql);\n"
		contentsstr = contentsstr + "\t}" + "\n"
		contentsstr = contentsstr + "\n"

		contentsstr = contentsstr + "}" + "\n"
		return contentsstr

if __name__ == "__main__":
	
	config      = sys.argv[1] # jdbc:oracle:thin:@192.168.1.151:1521:XE
	user        = sys.argv[2] # usr01
	password    = sys.argv[3] # 12345
	output_path = sys.argv[4] # /tmp/dao_generate/
	package     = sys.argv[5] # jp.co.dao_example
	
	# 最後の文字に/が含まれていた場合、除外する
	if (output_path[-1:]=="/"):
		output_path = output_path[:-1]
	
	if os.path.exists(output_path) and os.path.isfile(output_path) :
		print("[WARN]output_path is file, exists already. output_path=[" + output_path + "]" )
		sys.exit(1) 
	elif os.path.exists(output_path) and os.path.isdir(output_path) :
		print("[WARN]output_path is dir, exists already. output_path=[" + output_path + "]" )
	else :
		os.makedirs(output_path)
	
	param = DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, config, user, password)
	dataStore = OracleDataBaseDataStore(param)
	dataStore.startTransaction()
	
	# DAOクラスを生成
	classFile = JavaDaoClass(package, dataStore.getTables()[0])
	classFile.write(output_path)
	
	# DAOレコードクラスを生成
	classFile = JavaRecordClass(package, dataStore.getTables()[0])
	classFile.write(output_path)
	
	dataStore.finishTransaction()
	
	sys.exit()
