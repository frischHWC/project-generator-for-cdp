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
# Export your host here to launch the program on the platform
# export HOST=

export USER={{ user }}
export DIRECTORY_TO_WORK=/home/{{ user }}/{{ project_name }}/

# Create directory folder on cluster
ssh ${SSH_KEY} ${USER}@${HOST} mkdir -p ${DIRECTORY_TO_WORK}

# Copy files to cluster
{% if "spark" == type %}
scp ${SSH_KEY} {% if language == "scala" or language == "java" %}src/main/{% endif %}resources/scripts/spark-submit.sh ${USER}@${HOST}:${DIRECTORY_TO_WORK}
ssh ${SSH_KEY} ${USER}@${HOST} chmod 775 ${DIRECTORY_TO_WORK}spark-submit.sh
{% endif %}
{% if language == "scala" or language == "java" %}
scp ${SSH_KEY} src/main/resources/application.* ${USER}@${HOST}:${DIRECTORY_TO_WORK} {% if logger %}
scp ${SSH_KEY} src/main/resources/log4j.properties ${USER}@${HOST}:${DIRECTORY_TO_WORK}
scp ${SSH_KEY} src/main/resources/log4j2.properties ${USER}@${HOST}:${DIRECTORY_TO_WORK}{% endif %}
scp ${SSH_KEY} target/{% if compiler=="sbt" %}scala-2.12/{% endif %}{{ project_name }}-0.1-SNAPSHOT{% if fat_jar is sameas true %}-jar-with-dependencies{% endif %}.jar ${USER}@${HOST}:${DIRECTORY_TO_WORK}{{ project_name }}.jar
{% elif language == "python" %}
tar -czf python_files.tar src/
scp ${SSH_KEY} python_files.tar ${USER}@${HOST}:${DIRECTORY_TO_WORK}
{% endif %}

echo "Launch script on platform to launch program properly"
scp ${SSH_KEY} {% if language == "scala" or language == "java" %}src/main/{% endif %}resources/scripts/launch.sh ${USER}@${HOST}:${DIRECTORY_TO_WORK}
ssh ${SSH_KEY} ${USER}@${HOST} chmod 775 ${DIRECTORY_TO_WORK}launch.sh

{% if language == "scala" or language == "java" %}
ssh ${SSH_KEY} ${USER}@${HOST} 'bash -s' < src/main/resources/scripts/launch.sh $@
{% elif language == "python" %}
ssh ${SSH_KEY} ${USER}@${HOST} 'bash -s' < resources/scripts/launch.sh $@
{% endif %}