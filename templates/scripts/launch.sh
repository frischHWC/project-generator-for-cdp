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
    unzip python_files.zip
    python App.py
{% endif %}
{% endif %}

echo "*** Finished program ***"