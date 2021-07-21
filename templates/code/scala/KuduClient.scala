package {{ package_name }}.client

import {{ package_name }}.config.{AppConfig, Utils}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

{% if logger is sameas true %}import org.apache.logging.log4j.scala.Logging{% endif %}

import java.net.URI


class KuduClient {% if logger is sameas true %}extends Logging{% endif %} {


    def write(hashKey: String, table: String, value: String): Unit = {
     
    }

    def read(table: String): Unit = {

    }
}

object KuduClient {% if logger is sameas true %}extends Logging{% endif %} {

   def apply(): KuduClient = {
    var kuduClient = new KuduClient


    kuduClient
  }


}