#!/usr/bin/env bash

# Goal of this is to setup a complete test
#export HOST=
#export NAMESERVICE=
#export USER=
#export HDFS_WORK_DIR=


# Generate project
python3 main.py \
    --version 7.1.4.0-203 \
    --language python \
    --projectName first_test \
    --packageName com.cloudera.frisch \
    --compiler none \
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
chmod +x resources/scripts/*
resources/scripts/launchFromIDE.sh

