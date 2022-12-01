/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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