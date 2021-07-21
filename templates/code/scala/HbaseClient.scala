package {{ package_name }}.client

import {{ package_name }}.config.{AppConfig, Utils}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

{% if logger is sameas true %}import org.apache.logging.log4j.scala.Logging{% endif %}

import java.net.URI


class HbaseClient {% if logger is sameas true %}extends Logging{% endif %} {


    def write(namespace: : String, tableName: : String, cf: String, col1: String, col2: String, val1: String, val2: : String, key: String): Unit = {

    }

    def read(namespace: : String, tableName: : String, cf: String, col1: String, key: String): Unit = {

    }
}

object HbaseClient {% if logger is sameas true %}extends Logging{% endif %} {

   def apply(): HbaseClient = {
    var hbaseClient = new HbaseClient


    hbaseClient
  }


}