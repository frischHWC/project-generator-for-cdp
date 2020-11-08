
# TODO : Add other configurations properties here
app_name = "{{ project_name }}"

{% if program_type == "spark" %}
master = "yarn"
{% if "streaming" is in feature %}streaming_time = 5{% endif %}
{% endif %}

{% if "hdfs" is in components %}
{% endif %}

{% if "none" != components %}  # HADOOP general conf{% endif %}
{% if "hdfs" is in components %}
hadooop_core_site = "/etc/hadoop/conf_cloudera_hdfs/core-site_xml"
hdfs_site_path ="/etc/hadoop/conf_cloudera_hdfs/hdfs-site_xml"{% endif %}
{% if "ozone" is in components %}ozone_site_path = "/etc/hadoop/conf_cloudera_hdfs/ozone-site_xml"{% endif %}
{% if "hbase" is in components %}hbase_site_path = "/etc/hbase/conf_cloudera_hbase/hbase-site_xml"{% endif %}

hadoop_user = "{{ hadoop_user }}"
hadoop_home = "{{ haddop_home }}"
kerberos_auth = "{{ kerberos_auth }}"
kerberos_user = "{{ kerberos_user }}"
kerberos_keytab = "{{ kerberos_keytab }}"
keystore_location = "{{ keystore_location }}"
keystore_password = "{{ keystore_password }}"
keystore_keypassword = "{{ keystore_key_password }}"
truststore_location = "{{ truststore_location }}"
truststore_password = "{{ truststore_password }}"
{% endif %}

{% if "hdfs" is in components %}
hdfs_nameservice = "{{ hdfs_nameservice }}"
hdfs_port =  "8020"
hdfs_home_dir = "{{ hdfs_workdir }}"
{% endif %}

{% if "hbase" is in components %}  # HBASE
hbase_zookeeper_quorum = "{{ zookeeper_quorum }}"
hbase_zookeeper_port = "2181"
hbase_zookeeper_znode = "/hbase"
{% endif %}

{% if "ozone" is in components %}  # OZONE
ozone_nameservice = "{{ ozone_nameservice }}"
{% endif %}

{% if "hive" is in components %}  # HIVE
hive_zookeeper_server = "{{ zookeeper_quorum }}"
hive_zookeeper_port = "2181"
hive_zookeeper_znode = "hiveserver2"
{% endif %}

{% if "solr" is in components %}  # SOLR
solr_server = "{{ solr_server }}"
solr_port = "8983"
{% endif %}

{% if "kafka" is in components %}  # KAFKA
kafka_brokers = "{{ kafka_broker }}"
security_protocol = "{{ kafka_security_protocol }}"
schema_registry_url = "{{ schema_registry }}"
{% endif %}

{% if "kudu" is in components %}  # KUDU
kudu_master = "{{ kudu_master }}"
{% endif %}

