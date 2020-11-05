#!/usr/bin/env bash
# Export your host here to launch the program on the platform
# export HOST=

export USER={{ user }}
export DIRECTORY_TO_WORK=/home/{{ user }}/{{ project_name }}/

# Create directory folder on cluster
ssh ${USER}@${HOST} mkdir -p ${DIRECTORY_TO_WORK}

# Copy files to cluster
{% if "spark" == type %}
scp {% if language == "scala" or language == "java" %}src/main/{% endif %}resources/scripts/spark-submit.sh ${USER}@${HOST}:${DIRECTORY_TO_WORK}
ssh ${USER}@${HOST} chmod 775 ${DIRECTORY_TO_WORK}spark-submit.sh
{% endif %}
{% if language == "scala" or language == "java" %}
scp src/main/resources/application.* ${USER}@${HOST}:${DIRECTORY_TO_WORK} {% if logger %}
scp src/main/resources/log4j.properties ${USER}@${HOST}:${DIRECTORY_TO_WORK} {% endif %}
scp target/{% if compiler=="sbt" %}scala-2.12/{% endif %}{{ project_name }}-0.1-SNAPSHOT{% if fat_jar is sameas true %}-jar-with-dependencies{% endif %}.jar ${USER}@${HOST}:${DIRECTORY_TO_WORK}{{ project_name }}.jar
{% elif language == "python" %}
tar -czf python_files.tar src/
scp python_files.tar ${USER}@${HOST}:${DIRECTORY_TO_WORK}
{% endif %}

echo "Launch script on platform to launch program properly"
scp {% if language == "scala" or language == "java" %}src/main/{% endif %}resources/scripts/launch.sh ${USER}@${HOST}:${DIRECTORY_TO_WORK}
ssh ${USER}@${HOST} chmod 775 ${DIRECTORY_TO_WORK}launch.sh

{% if language == "scala" or language == "java" %}
ssh ${USER}@${HOST} 'bash -s' < src/main/resources/scripts/launch.sh $@
{% elif language == "python" %}
ssh ${USER}@${HOST} 'bash -s' < resources/scripts/launch.sh $@
{% endif %}