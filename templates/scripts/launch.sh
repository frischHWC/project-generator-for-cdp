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

export DIRECTORY_TO_WORK=/home/{{ user }}/{{ project_name }}/

echo "*** Starting to launch program ***"

    cd $DIRECTORY_TO_WORK

{% if "spark" is in components %}
    spark-submit.sh
{% else %}
{% if language == "scala" or language == "java" %}
    java -jar {{ project_name }}.jar $@
{% elif language == "python" %}
    tar -xvzf python_files.tar
    cd src/
    python App.py
{% endif %}
{% endif %}

echo "*** Finished program ***"