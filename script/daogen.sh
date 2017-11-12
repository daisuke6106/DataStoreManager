#!/bin/sh
CURRENT=$(cd $(dirname $0) && pwd)
MAIN_JAR="${CURRENT}/../datastoremanager_1.2.4.jar"
LIB_JAR="${CURRENT}/../lib/*"
MESSAGE_DIR="${CURRENT}/../messages"
PROPERTY_DIR="${CURRENT}/../properties"
java -classpath "${MAIN_JAR}:${LIB_JAR}:${MESSAGE_DIR}:${PROPERTY_DIR}" jp.co.dk.datastoremanager.command.daogen.CreateDataAccessObjectControler $*

