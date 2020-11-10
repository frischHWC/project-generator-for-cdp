#!/usr/bin/env bash

# Goal of this is to setup a complete test
#export HOST=
#export NAMESERVICE=
#export USER=
#export HDFS_WORK_DIR=


# Generate project
python3 main.py \
    --version 7.1.4.0 \
    --language java \
    --projectName java_client \
    --packageName com.cloudera.frisch \
    --compilation True \
    --logger True \
    --compiler maven \
    --kerberos True \
    --principal  dev \
    --keytab /home/dev/dev.keytab \
    --components hdfs \
    --host ${HOST} \
    --user ${USER} \
    --hdfsNameservice ${NAMESERVICE} \
    --hadoopUser dev \
    --hadoopHome /user/dev \
    --kerberosAuth True \
    --kerberosUser dev@FRISCH.COM \
    --kerberosKeytab /home/dev/dev.keytab \
    --fatjar False


# Start program with script
cd ../java_client
chmod +x src/main/resources/scripts/*
src/main/resources/scripts/launchFromIDE.sh

