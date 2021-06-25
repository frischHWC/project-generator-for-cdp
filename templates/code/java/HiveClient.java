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