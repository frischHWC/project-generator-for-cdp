package {{ package_name }}.client;

import {{ package_name }}.config.AppConfig;
import {{ package_name }}.config.Utils;
{% if logger is sameas true %}import org.apache.log4j.Logger;{% endif %}
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import org.apache.kudu.client.*;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;


import java.security.PrivilegedExceptionAction;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;


public class KuduClientTest {

  {% if logger is sameas true %}private static Logger logger = Logger.getLogger(KuduClientTest.class);{% endif %}

  private KuduSession session;
  private KuduClient client;


  public KuduClientTest() {

  try {

         if(Boolean.parseBoolean(AppConfig.getProperty("tls.auth"))) {
            System.setProperty("javax.net.ssl.trustStore", AppConfig.getProperty("truststore.location"));
            System.setProperty("javax.net.ssl.trustStorePassword", AppConfig.getProperty("truststore.password"));
         }

                if (Boolean.valueOf(AppConfig.getProperty("kerberos.auth"))) {
                    Utils.loginUserWithKerberos(
                        AppConfig.getProperty("kerberos.user"),
                        AppConfig.getProperty("kerberos.keytab"), new Configuration());

                UserGroupInformation.getLoginUser().doAs(
                        new PrivilegedExceptionAction<KuduClient>() {
                            @Override
                            public KuduClient run() throws Exception {
                                client = new KuduClient.KuduClientBuilder(AppConfig.getProperty("kudu.master")).build();
                                return client;
                            }
                        });
            } else {
                client = new KuduClient.KuduClientBuilder(AppConfig.getProperty("kudu.master")).build();
            }

            session = client.newSession();

        } catch (Exception e) {
            {% if logger is sameas true %}logger.error("Could not connect to Kudu due to error: ", e);{% endif %}
        }

  }


  public void write(String hashKey, String tableName, String value) {

    // Create Table if not exist
    CreateTableOptions cto = new CreateTableOptions();
        cto.setNumReplicas(1);
        cto.addHashPartitions(Arrays.asList(hashKey), 8);

  List<ColumnSchema> columns = new ArrayList<>(1);
  columns.add(new ColumnSchema.ColumnSchemaBuilder(hashKey, Type.STRING)
                            .key(true)
                            .build());
  Schema kuduSchema = new Schema(columns);

        try {
            client.createTable(tableName, kuduSchema, cto);
        } catch (KuduException e) {
            if(e.getMessage().contains("already exists")){
                {% if logger is sameas true %}logger.info("Table Kudu : "  + tableName + " already exists, hence it will not be created");{% endif %}
            } else {
                {% if logger is sameas true %}logger.error("Could not create table due to error", e);{% endif %}
            }
        }

    try {

    KuduTable table = client.openTable(tableName);

    // Insert row
    Insert insert = table.newInsert();
    PartialRow partialRow = insert.getRow();
    partialRow.addObject(hashKey, value);

    session.apply(insert);
    session.flush();

    } catch (Exception e) {
            {% if logger is sameas true %}logger.error("Could not insert to Kudu due to error: ", e);{% endif %}
        }

  }

  public void read(String tableName) {

  try {

    KuduTable table = client.openTable(tableName);

    KuduScanner kuduScanner = client.newScannerBuilder(table).build();

    while (kuduScanner.hasMoreRows()) {
       RowResultIterator iterator = kuduScanner.nextRows();
       while (iterator.hasNext()) {
           RowResult result = iterator.next();
           {% if logger is sameas true %}logger.info("Value is: " + result.rowToString());{% endif %}
       }
    }

    session.flush();
    session.close();

  } catch (Exception e) {
            {% if logger is sameas true %}logger.error("Could not read from Kudu due to error: ", e);{% endif %}
  }

  }


}