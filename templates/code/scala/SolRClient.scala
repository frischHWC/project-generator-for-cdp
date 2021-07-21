package {{ package_name }}.client

import {{ package_name }}.config.{AppConfig, Utils}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

{% if logger is sameas true %}import org.apache.logging.log4j.scala.Logging{% endif %}

import java.net.URI


class SolRClient {% if logger is sameas true %}extends Logging{% endif %} {


    def write(value: String): Unit = {
     
    }

    def read(value: String): Unit = {

    }
}

object SolRClient {% if logger is sameas true %}extends Logging{% endif %} {

   def apply(): SolRClient = {
    var solRClient = new SolRClient


    solRClient
  }


}