package {{ package_name }}.client

import {{ package_name }}.config.{AppConfig, Utils}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

{% if logger is sameas true %}import org.apache.logging.log4j.scala.Logging{% endif %}

import java.net.URI


class KafkaClient {% if logger is sameas true %}extends Logging{% endif %} {


    def write(key: String, value: String, topic: String): Unit = {
     
    }

    def read(topic: String): Unit = {

    }
}

object KafkaClient {% if logger is sameas true %}extends Logging{% endif %} {

   def apply(): KafkaClient = {
    var kafkaClient = new KafkaClient


    kafkaClient
  }


}