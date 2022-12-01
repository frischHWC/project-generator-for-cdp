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
          config.set("hadoop.security.authentication", "kerberos");
    }

    hdfsUri = URI.create("hdfs://" + AppConfig.getProperty("hdfs.nameservice") + ":" + AppConfig.getProperty("hdfs.port") + "/");

    {% if logger is sameas true %}logger.debug("Setting up access to HDFS");{% endif %}
    try {
      fileSystem = FileSystem.get(hdfsUri, config);
    } catch (IOException e) {
      {% if logger is sameas true %}logger.error("Could not access to HDFS !", e);{% endif %}
    }
  }


  public void write(String toWrite, String path) {
    try(FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path(path), true)) {
      fsDataOutputStream.writeUTF(toWrite);
    } catch (IOException e) {
      {% if logger is sameas true %}logger.error("Could not write to hdfs: " + toWrite + " due to error", e);{% endif %}
    }
  }

  public void read(String path) {
    try(FSDataInputStream fsDataInputStream = fileSystem.open(new Path(path))) {
      {% if logger is sameas true %}logger.info("File content is: " + fsDataInputStream.readUTF());{% endif %}
    } catch (IOException e) {
      {% if logger is sameas true %}logger.error("Could not read hdfs file: " + path + " due to error", e);{% endif %}
    }
  }


}