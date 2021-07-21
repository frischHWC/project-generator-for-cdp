package {{ package_name }}.client

import {{ package_name }}.config.{AppConfig, Utils}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

{% if logger is sameas true %}import org.apache.logging.log4j.scala.Logging{% endif %}

import java.net.URI


class HdfsClient {% if logger is sameas true %}extends Logging{% endif %} {

  var fileSystem: FileSystem = _
  var hdfsUri: URI = _

    def write(toWrite: String, path: String): Unit = {
      try {
        val fsDataOutputStream = fileSystem.create(new Path(path), true)
        fsDataOutputStream.writeChars(toWrite)
        fsDataOutputStream.close()
      } catch {
        case e: Exception => {% if logger is sameas true %}logger.error("Could not write to hdfs: " + toWrite + " due to error", e){% endif %}
      }
    }

    def read(path: String): Unit = {
      try {
        val fsDataInputStream = fileSystem.open(new Path(path))
        logger.info("File content is: " + fsDataInputStream.read())
        fsDataInputStream.close()
      } catch {
        case e: Exception => {% if logger is sameas true %}logger.error("Could not read hdfs file: " + path + " due to error", e){% endif %}
      }
    }
}

object HdfsClient {% if logger is sameas true %}extends Logging{% endif %} {

   def apply(): HdfsClient = {
    var hdfsClient = new HdfsClient

    val config = new Configuration()
    Utils.setupHadoopEnv(config)

    if (AppConfig.kerberosAuth.equalsIgnoreCase("true")) {
      Utils.loginUserWithKerberos(
        AppConfig.kerberosUser,
        AppConfig.kerberosKeytab, config)
      config.set("hadoop.security.authentication", "kerberos")
    }

    hdfsClient.hdfsUri = URI.create("hdfs://" + AppConfig.hdfsNameservice + ":" + 8020 + "/")

    logger.debug("Setting up access to HDFS")
    try {
      hdfsClient.fileSystem = FileSystem.get(hdfsClient.hdfsUri, config)
    } catch {
      case e: Exception => {% if logger is sameas true %}logger.error("Could not access to HDFS !", e){% endif %}
    }
    hdfsClient
  }


}