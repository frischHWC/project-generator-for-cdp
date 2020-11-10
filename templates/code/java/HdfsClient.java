package {{ package_name }}.client;

import {{ package_name }}.config.AppConfig;
import {{ package_name }}.config.Utils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
{% if logger is sameas true %}import org.apache.log4j.Logger;{% endif %}

import java.io.IOException;
import java.net.URI;


public class HdfsClient {

  {% if logger is sameas true %}private static Logger logger = Logger.getLogger(HdfsClient.class);{% endif %}

  private FileSystem fileSystem;
  private URI hdfsUri;

  public HdfsClient() {
    Configuration config = new Configuration();
    Utils.setupHadoopEnv(config);

    if (Boolean.valueOf(AppConfig.getProperty("kerberos.auth"))) {
      Utils.loginUserWithKerberos(
          AppConfig.getProperty("kerberos.user"),
          AppConfig.getProperty("kerberos.keytab"), config);
    }

    hdfsUri = URI.create("hdfs://" + AppConfig.getProperty("hdfs.nameservice") + ":" + AppConfig.getProperty("hdfs.port"));

    logger.debug("Setting up access to HDFS");
    try {
      fileSystem = FileSystem.get(hdfsUri, config);
    } catch (IOException e) {
      logger.error("Could not access to HDFS !", e);
    }
  }


  public void write(String toWrite, String path) {
    try(FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path(path), true)) {
      fsDataOutputStream.writeChars(toWrite);
    } catch (IOException e) {
      logger.error("Could not write to hdfs: " + toWrite + " due to error", e);
    }
  }

  public void read(String path) {
    try(FSDataInputStream fsDataInputStream = fileSystem.open(new Path(path))) {
      logger.info("File content is: " + fsDataInputStream.readUTF());
    } catch (IOException e) {
      logger.error("Could not read hdfs file: " + path + " due to error", e);
    }
  }


}