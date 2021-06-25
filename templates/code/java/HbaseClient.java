package {{ package_name }}.client;

import {{ package_name }}.config.AppConfig;
import {{ package_name }}.config.Utils;
{% if logger is sameas true %}import org.apache.log4j.Logger;{% endif %}
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


public class HbaseClient {

  {% if logger is sameas true %}private static Logger logger = Logger.getLogger(HbaseClient.class);{% endif %}
   private Connection connection;


  public HbaseClient() {

        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", AppConfig.getProperty("hbase.zookeeper.quorum"));
        config.set("hbase.zookeeper.property.clientPort", AppConfig.getProperty("hbase.zookeeper.port"));
        config.set("zookeeper.znode.parent", AppConfig.getProperty("hbase.zookeeper.znode"));
        Utils.setupHadoopEnv(config);

        if (Boolean.valueOf(AppConfig.getProperty("kerberos.auth"))) {
          Utils.loginUserWithKerberos(
          AppConfig.getProperty("kerberos.user"),
          AppConfig.getProperty("kerberos.keytab"), config);
          config.set("hbase.security.authentication", "kerberos");
        }

        try {
            connection = ConnectionFactory.createConnection(config);
        } catch (IOException e) {
            {% if logger is sameas true %}logger.error("Could not initiate HBase connection due to error: ", e);{% endif %}
            System.exit(1);
        }

  }


  public void write(String namespace, String tableName, String columnFamily, String col1, String col2, String value1, String value2, String key) {

    try {
        // Create Namespace
        Admin admin = connection.getAdmin();
        admin.createNamespace(NamespaceDescriptor.create(namespace).build());

        // Create table
        TableDescriptorBuilder tbdesc = TableDescriptorBuilder.newBuilder(TableName.valueOf(namespace + ":" + tableName));
        tbdesc.setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(columnFamily)).build());
        admin.createTable(tbdesc.build());

        // Load data into the table
        Table table = connection.getTable(TableName.valueOf(namespace + ":" + tableName));
        Put put = new Put(Bytes.toBytes("row1"));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(col1), Bytes.toBytes(value1));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(col2), Bytes.toBytes(value2));
        table.put(put);

    } catch (IOException e) {
            {% if logger is sameas true %}logger.error("Could not write to HBase due to error: ", e);{% endif %}
            System.exit(1);
    }

  }

  public void read(String namespace, String tableName, String columnFamily, String col1, String key) {

  try {

       Table table = connection.getTable(TableName.valueOf(namespace + ":" + tableName));
       Get get = new Get(Bytes.toBytes(key));
       Result result = table.get(get);
       byte [] value = result.getValue(Bytes.toBytes(columnFamily),Bytes.toBytes(col1));
       {% if logger is sameas true %}logger.info("Value is " + Bytes.toString(value));{% endif %}

       table.close();

  } catch (IOException e) {
     {% if logger is sameas true %}logger.error("Could not read from HBase due to error: ", e);{% endif %}
     System.exit(1);
  }


  }


}