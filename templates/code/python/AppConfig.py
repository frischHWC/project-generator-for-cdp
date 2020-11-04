
# TODO : Add other configurations properties here
app_name = "{{ project_name }}"

{% if type == "spark" %}
master = "yarn"
{% if "streaming" or "sql" or "structured_streaming" is in feature %}
hdfs = "hdfs://{{ hdfsNameservice }}:8020"
hdfs_home_dir = "{{ hdfsWorkDir }}"{% endif %}
{% if "streaming" is in feature %}streaming_time = 5{% endif %}
{% endif %}