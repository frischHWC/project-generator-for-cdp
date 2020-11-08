#!/usr/bin/env bash

# Goal of this is to setup a complete test
#export HOST=
#export NAMESERVICE=
#export USER=
#export HDFS_WORK_DIR=


# Generate project
python3 main.py \
    --version 7.1.4.0-203 \
    --language java \
    --projectName first_test \
    --packageName com.cloudera.frisch \
    --compilation true \
    --logger true \
    --compiler sbt \
    --kerberos true \
    --principal  dev \
    --keytab /home/dev/dev.keytab \
    --components hdfs \
    --host ${HOST} \
    --user ${USER} \
    --hdfsNameservice ${NAMESERVICE} \
    --hdfsWorkDir ${HDFS_WORK_DIR}

# Start program with script
cd ../first_test
chmod +x src/main/resources/scripts/*
src/main/resources/scripts/launchFromIDE.sh

