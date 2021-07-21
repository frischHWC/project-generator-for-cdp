package {{ package_name }}.client

import {{ package_name }}.config.{AppConfig, Utils}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

{% if logger is sameas true %}import org.apache.logging.log4j.scala.Logging{% endif %}

import java.net.URI


class HiveClient {% if logger is sameas true %}extends Logging{% endif %} {


    def write(toWrite: String): Unit = {
     
    }

    def read(path: String): Unit = {

    }
}

object HiveClient {% if logger is sameas true %}extends Logging{% endif %} {

   def apply(): HiveClient = {
    var hiveClient = new HiveClient


    hiveClient
  }


}