#!/usr/bin/env bash

export HOST={{ host }}

{% if language == "scala" or language == "java" %}
src/main/resources/scripts/launchToCDP.sh
{% elif language == "python" %}
resources/scripts/launchToCDP.sh
{% endif %}
