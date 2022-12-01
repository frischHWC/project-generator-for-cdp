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
package {{ package_name }}.client;

import {{ package_name }}.config.AppConfig;
import {{ package_name }}.config.Utils;
{% if logger is sameas true %}import org.apache.log4j.Logger;{% endif %}
import org.apache.hadoop.conf.Configuration;
import org.apache.hive.jdbc.HiveConnection;
import org.apache.hive.jdbc.HivePreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;


public class HiveClient {

  {% if logger is sameas true %}private static Logger logger = Logger.getLogger(HiveClient.class);{% endif %}
  private Connection hiveConnection;


  public HiveClient() {

    if (Boolean.valueOf(AppConfig.getProperty("kerberos.auth"))) {
      Utils.loginUserWithKerberos(
          AppConfig.getProperty("kerberos.user"),
          AppConfig.getProperty("kerberos.keytab"), new Configuration());
    }

     System.setProperty("javax.net.ssl.trustStore", AppConfig.getProperty("truststore.location"));
     System.setProperty("javax.net.ssl.trustStorePassword", AppConfig.getProperty("truststore.password"));

     try {

     hiveConnection = DriverManager.getConnection("jdbc:hive2://" +
                            AppConfig.getProperty("hive.zookeeper.quorum") + ":" +
                            AppConfig.getProperty("hive.zookeeper.port") + "/" +
                            ";serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=" +
                            AppConfig.getProperty("hive.zookeeper.znode")
                    , new Properties());

     PreparedStatement preparedStatement = hiveConnection.prepareStatement("CREATE DATABASE IF NOT EXISTS test_db");
     preparedStatement.execute();
     preparedStatement.close();

     } catch (SQLException e) {
            {% if logger is sameas true %}logger.error("Could not execute request due to error: ", e);{% endif %}
     }

  }


  public void write(String toWrite) {

  }

  public void read(String toRead) {

  }


}