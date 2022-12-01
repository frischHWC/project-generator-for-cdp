#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#  http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#
#!/usr/bin/env bash

# Goal of this is to setup a complete test
#export HOST=
#export NAMESERVICE=
#export USER=
#export HDFS_WORK_DIR=
#export SSH_KEY=


# Generate project
/usr/local/Cellar/python@3.9/3.9.1_6/bin/python3 main.py \
    --version 7.1.6.0 \
    --language scala \
    --projectName scala_hdfs_client \
    --packageName com.cloudera.frisch \
    --compilation True \
    --logger True \
    --compiler maven \
    --fatjar true \
    \
    --tls True \
    --kerberos True \
    --principal dev@FRISCH.COM \
    --keytab /home/dev/dev.keytab \
    \
    --host ${HOST} \
    --user ${USER} \
    \
    --hdfsNameservice ${NAMESERVICE} \
    --hadoopUser dev \
    --hadoopHome /user/dev \
    --truststoreLocation /var/lib/cloudera-scm-agent/agent-cert/cm-auto-global_truststore.jks \
    --truststorePassword ${TRUSTSTORE_PASSWORD} \
    --keystoreLocation /var/lib/cloudera-scm-agent/agent-cert/cm-auto-host_keystore.jks \
    --keystorePassword ${KEYSTORE_PASSWORD} \
    --keystoreKeyPassword  ${KEYSTORE_PASSWORD} \
    --zookeeperQuorum ccycloud-2.${NAMESERVICE}.root.hwx.site,ccycloud-3.${NAMESERVICE}.root.hwx.site,ccycloud-10.${NAMESERVICE}.root.hwx.site \
    --ozoneNameservice ${NAMESERVICE} \
    --solrServer ccycloud-8.${NAMESERVICE}.root.hwx.site \
    --kafkaBroker ccycloud-7.${NAMESERVICE}.root.hwx.site:9093,ccycloud-8.${NAMESERVICE}.root.hwx.site:9093,ccycloud-9.${NAMESERVICE}.root.hwx.site:9093 \
    --kafkaSecurityProtocol SASL_SSL \
    --schemaRegistry ccycloud-2.${NAMESERVICE}.root.hwx.site:7790 \
    --kuduMaster ccycloud-2.${NAMESERVICE}.root.hwx.site,ccycloud-3.${NAMESERVICE}.root.hwx.site,ccycloud-10.${NAMESERVICE}.root.hwx.site \
    --components hdfs


# Start program with script
cd ../scala_hdfs_client
chmod +x src/main/resources/scripts/*
src/main/resources/scripts/launchFromIDE.sh

