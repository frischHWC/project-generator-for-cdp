/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package {{ package_name }}.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory


object AppConfig {

  val conf: Config = ConfigFactory.load()

  val name = conf.getString("appName")

  {% if program_type == "spark" %}
  val master = conf.getString("master")
  {% if feature is not none and "streaming" is in feature %}val streamingTime = conf.getInt("streamingTime"){% endif %}
  {% endif %}

  {% if "none" != components %}
  {% if "hdfs" is in components %}val coreSitePath = conf.getString("hadoop.core.site.path")
  val hdfsSitePath = conf.getString("hadoop.hdfs.site.path"){% endif %}
  {% if "ozone" is in components %}val ozoneSitePath = conf.getString("hadoop.ozone.site.path"){% endif %}
  {% if "hbase" is in components %}val hbaseSitePath = conf.getString("hadoop.hbase.site.path"){% endif %}

  val hadoopUser = conf.getString("hadoop.user")
  val hadoopHome = conf.getString("hadoop.home")
  val kerberosAuth = conf.getString("kerberos.auth")
  val kerberosUser = conf.getString("kerberos.user")
  val kerberosKeytab = conf.getString("kerberos.keytab")
  val keystoreLocation = conf.getString("keystore.location")
  val keystorePassword = conf.getString("keystore.password")
  val keystoreKeyPassword = conf.getString("keystore.keypassword")
  val truststoreLocation = conf.getString("truststore.location")
  val truststorePassword = conf.getString("truststore.password")
  {% endif %}

  {% if "hdfs" is in components %}
  val hdfsNameservice = conf.getString("hdfs.nameservice")
  val port = conf.getString("hdfs.port")
  val hdfsHomeDir = conf.getString("hdfs.home_dir")
  {% endif %}

  {% if "hbase" is in components %}
  val hbaseZookeeperQuorum = conf.getString("hbase.zookeeper.quorum")
  val hbaseZookeeperPort = conf.getString("hbase.zookeeper.port")
  val habseZookeeperZnode = conf.getString("hbase.zookeeper.znode")
  {% endif %}

  {% if "ozone" is in components %}
  val ozoneNameService = conf.getString("ozone.nameservice")
  {% endif %}

  {% if "hive" is in components %}
  val hiveZookeeperQuorum = conf.getString("hive.zookeeper.quorum")
  val hiveZookeeperPort = conf.getString("hive.zookeeper.port")
  val hiveZookeeperZnode = conf.getString("hve.zookeeper.znode")
  {% endif %}

  {% if "solr" is in components %}
  val solrServer = conf.getString("solr.server")
  val solrPort = conf.getString("solr.port")
  {% endif %}

  {% if "kafka" is in components %}
  val kafkaBrokers = conf.getString("kafka.brokers")
  val kafkaSecurityProtocol = conf.getString("kafka.security_protocol")
  val schemaRegistryUrl = conf.getString("kafka.schema_registry.url")
  {% endif %}

  {% if "kudu" is in components %}
  val kuduMaster = conf.getString("kudu.master")
  {% endif %}

}
