package {{ package_name }}.client

import {{ package_name }}.config.{AppConfig, Utils}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

{% if logger is sameas true %}import org.apache.logging.log4j.scala.Logging{% endif %}

import java.net.URI


class OzoneClientTest {% if logger is sameas true %}extends Logging{% endif %} {


    def write(volume: String, bucket: String, key: String, value: String): Unit = {
     
    }

    def read(volume: String, bucket: String, key: String): Unit = {

    }
}

object OzoneClientTest {% if logger is sameas true %}extends Logging{% endif %} {

   def apply(): OzoneClientTest = {
    var ozoneClientTest = new OzoneClientTest


    ozoneClientTest
  }


}