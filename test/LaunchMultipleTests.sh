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

# Goal of this is to setup different multiple test
export HOST=
export NAMESERVICE=
export VERSION=7.1.6.0

export USER=root
export HDFS_WORK_DIR=/home/dev/first_test/
export USER=dev
export KEYTAB=/home/dev/dev.keytab
# Maybe to change
export COMPONENTS=hdfs

# TODO: Add more tests below when more features are available to test

echo "*******************************"
echo "***** Scala Test with SBT *****"
# Generate project
python3 main.py \
    --version ${VERSION} \
    --packageName com.cloudera.frisch \
    --kerberos true \
    --principal ${USER} \
    --keytab ${KEYTAB}  \
    --components ${COMPONENTS} \
    --host ${HOST} \
    --user ${USER} \
    --hdfs-nameservice ${NAMESERVICE} \
    --hdfs-workdir ${HDFS_WORK_DIR} \
    \
    --language scala \
    --projectName scala_test_sbt \
    --compiler sbt \
    --compilation true

# Start program with script
cd ../scala_test_sbt
chmod +x src/main/resources/scripts/*
src/main/resources/scripts/launchFromIDE.sh
cd ../project-generator-for-cdp
echo "*******************************"
sleep 5

echo "*******************************"
echo "***** Scala Test with Maven *****"
# Generate project
python3 main.py \
    --version ${VERSION} \
    --packageName com.cloudera.frisch \
    --kerberos true \
    --principal ${USER} \
    --keytab ${KEYTAB}  \
    --components ${COMPONENTS} \
    --host ${HOST} \
    --user ${USER} \
    --hdfs-nameservice ${NAMESERVICE} \
    --hdfs-workdir ${HDFS_WORK_DIR} \
    \
    --language scala \
    --projectName scala_test_maven \
    --compiler maven \
    --compilation true

# Start program with script
cd ../scala_test_maven
chmod +x src/main/resources/scripts/*
src/main/resources/scripts/launchFromIDE.sh
cd ../project-generator-for-cdp
echo "*******************************"
sleep 5


echo "*******************************"
echo "***** Java Test with Maven *****"
# Generate project
python3 main.py \
    --version ${VERSION} \
    --packageName com.cloudera.frisch \
    --kerberos true \
    --principal ${USER} \
    --keytab ${KEYTAB}  \
    --components ${COMPONENTS} \
    --host ${HOST} \
    --user ${USER} \
    --hdfs-nameservice ${NAMESERVICE} \
    --hdfs-workdir ${HDFS_WORK_DIR} \
    \
    --language java \
    --projectName java_test_maven \
    --compiler maven \
    --compilation true

# Start program with script
cd ../java_test_maven
chmod +x src/main/resources/scripts/*
src/main/resources/scripts/launchFromIDE.sh
cd ../project-generator-for-cdp
echo "*******************************"
sleep 5


echo "*******************************"
echo "***** Python Test *****"
# Generate project
python3 main.py \
    --version ${VERSION} \
    --packageName com.cloudera.frisch \
    --kerberos true \
    --principal ${USER} \
    --keytab ${KEYTAB}  \
    --components ${COMPONENTS} \
    --host ${HOST} \
    --user ${USER} \
    --hdfs-nameservice ${NAMESERVICE} \
    --hdfs-workdir ${HDFS_WORK_DIR} \
    \
    --language python \
    --projectName python_test_maven

# Start program with script
cd ../python_test_maven
chmod +x resources/scripts/*
resources/scripts/launchFromIDE.sh
cd ../project-generator-for-cdp
echo "*******************************"
sleep 5


# TODO: Add tests with components configured to test integration with components