#!/bin/sh
# ====================================================================================================
# データベース環境情報読み込み
# ====================================================================================================
. env/datastore_env.sh

# ====================================================================================================
# プロジェクト情報読み込み
# ====================================================================================================
. env/project_env.sh

# ====================================================================================================
# スクリプト実行
# ====================================================================================================
CURRENT=$(cd $(dirname $0) && pwd)
MAIN_JAR="${CURRENT}/../datastoremanager_1.2.4_all.jar"
MESSAGE_DIR="${CURRENT}/../messages"
PROPERTY_DIR="${CURRENT}/../properties"
LOG_PROPERTY="${CURRENT}/../properties/Logger.properties"
java \
-Dpython.path="${MAIN_JAR}:${LIB_JAR}:${MESSAGE_DIR}:${PROPERTY_DIR}" \
-Dlogger_property_file=${LOG_PROPERTY} \
-jar ${JYTHON_HOME}/jython.jar \
./daogen/daogen.py \
--url ${DATABASE_URL} \
--user ${USER} \
--password ${PASSWORD} \
--output_path ${DAOGEN_OUTPUT_PATH} --package ${DAOGEN_PACKAGE}
