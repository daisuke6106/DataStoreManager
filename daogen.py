# -*- coding: utf-8 -*-
import sys
sys.path.append("datastoremanager_1.2.4_all.jar")

import jp.co.dk.datastoremanager.core.rdb.oracle.OracleDataBaseDataStore as OracleDataBaseDataStore
import jp.co.dk.datastoremanager.core.rdb.DataBaseAccessParameter as DataBaseAccessParameter
import jp.co.dk.datastoremanager.core.DataStoreKind as DataStoreKind
import jp.co.dk.datastoremanager.core.DataBaseDriverConstants as DataBaseDriverConstants
import jp.co.dk.datastoremanager.core.rdb.TableMetaData as TableMetaData


class ClassFile:
	
	def contents(self):
		pass
	
	def write(self, filepath):
		f = open(filepath, "w")
		f.write(self.contents())
		f.close()

class JavaClassFile(ClassFile):
	def __init__(self, package, table):
		self.package = package
		self.table   = table
		
	def convertOracleColumnToJavaObject(self, column):
		column_type = column.getColumnType()
		if column_type in {"CHAR", "NCHAR", "VARCHAR2", "NVARCHAR2", "CLOB"}:
			return "java.util.String"
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

class JavaRecordClass(JavaClassFile):

	def contents(self):
		
		recordClassName = self.table.toString() + "Record"
		
		contentsstr = ""
		contentsstr = contentsstr + "package " + self.package + ";\n"
		contentsstr = contentsstr + "\n"
		contentsstr = contentsstr + "import jp.co.dk.datastoremanager.core.exception.DataStoreManagerException;\n"
		contentsstr = contentsstr + "import jp.co.dk.datastoremanager.core.rdb.DataBaseRecord;\n"
		contentsstr = contentsstr + "import jp.co.dk.datastoremanager.core.rdb.DataConvertable;\n"
		contentsstr = contentsstr + "\n"
		contentsstr = contentsstr + "public class " + recordClassName + " implements DataConvertable {" + "\n"
		contentsstr = contentsstr + "\n"
		
		# フィールド定義
		for column in self.table.getColumns():
			contentsstr = contentsstr + "\tprivate " + "boolean isset_" + column.getColumnname() + " = false;" + "\n"
			contentsstr = contentsstr + "\n"
			contentsstr = contentsstr + "\tprivate " + self.convertOracleColumnToJavaObject(column) + " " + column.getColumnname() + ";" + "\n"
			contentsstr = contentsstr + "\n"
		
		# GETTER, SETTER定義
		for column in self.table.getColumns():
			contentsstr = contentsstr + "\tpublic" + " void" + " set" + column.getColumnname() + " ("+ self.convertOracleColumnToJavaObject(column) + " value)" + "{" + "\n"
			contentsstr = contentsstr + "\t\t" + "this.isset_" + column.getColumnname() + " = true;" + "\n"
			contentsstr = contentsstr + "\t\t" + "this." + column.getColumnname() + " = value;\n"
			contentsstr = contentsstr + "\t}\n"
			contentsstr = contentsstr + "\n"
		
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
		contentsstr = contentsstr + "\t}\n"
		contentsstr = contentsstr + "\n"
		
		
		contentsstr = contentsstr + "\n"
		contentsstr = contentsstr + "}" + "\n"
		return contentsstr

class JavaDaoClass(JavaClassFile):

	def contents(self):
		
		tableName       = self.table.toString()
		daoClassName    = tableName + "Dao"
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
		contentsstr = contentsstr + "public class " + daoClassName + " extends AbstractDataBaseAccessObject {" + "\n"
		contentsstr = contentsstr + "\n"

		# constractor
		contentsstr = contentsstr + "\tpublic " + daoClassName + "(DataBaseAccessParameter dataBaseAccessParameter) throws DataStoreManagerException {\n"
		contentsstr = contentsstr + "\t\tsuper(dataBaseAccessParameter);\n"
		contentsstr = contentsstr + "\t}\n"
		contentsstr = contentsstr + "\n"
		
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
		contentsstr = contentsstr + "\t\tjava.uril.StringJoiner setToken = new java.uril.StringJoiner(\",\");\n"
		for column in self.table.getColumns():
			contentsstr = contentsstr + "\t\tif (updateRecord.isset_" + column.getColumnname() + ") {\n"
			contentsstr = contentsstr + "\t\t\tsetToken.append(\"" + column.getColumnname() + " = ?\");\n"
			contentsstr = contentsstr + "\t\t\tsql.setParameter(" + column.getColumnname() + ");\n"
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
	param = DataBaseAccessParameter(DataStoreKind.ORACLE, DataBaseDriverConstants.ORACLE, "jdbc:oracle:thin:@192.168.42.194:1521:XE", "usr01", "12345")
	dataStore = OracleDataBaseDataStore(param)
	dataStore.startTransaction()
	
	classFile = JavaDaoClass("jp.co.dk.dbms", dataStore.getTables()[0])
	print( classFile.contents() )
	sys.exit()