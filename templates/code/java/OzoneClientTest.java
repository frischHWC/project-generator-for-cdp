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
{% if logger is sameas true %}import org.apache.log4j.Logger;{% endif %}
import org.apache.hadoop.hdds.client.ReplicationFactor;
import org.apache.hadoop.hdds.client.ReplicationType;
import org.apache.hadoop.hdds.conf.OzoneConfiguration;
import org.apache.hadoop.ozone.client.*;
import org.apache.hadoop.ozone.client.io.OzoneOutputStream;
import org.apache.hadoop.ozone.client.io.OzoneInputStream;
import org.apache.hadoop.ozone.om.exceptions.OMException;

import java.io.IOException;
import java.net.URI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class OzoneClientTest {

  {% if logger is sameas true %}private static Logger logger = Logger.getLogger(OzoneClientTest.class);{% endif %}

  private OzoneClient ozClient;
  private ObjectStore objectStore;


  public OzoneClientTest() {

  try {

    OzoneConfiguration config = new OzoneConfiguration();
    Utils.setupHadoopEnv(config);

    if (Boolean.valueOf(AppConfig.getProperty("kerberos.auth"))) {
      Utils.loginUserWithKerberos(
          AppConfig.getProperty("kerberos.user"),
          AppConfig.getProperty("kerberos.keytab"), config);
          config.set("hadoop.security.authentication", "kerberos");
    }

    ozClient = OzoneClientFactory.getRpcClient(AppConfig.getProperty("ozone.nameservice"), config);
    objectStore = ozClient.getObjectStore();

    } catch (IOException e) {
            {% if logger is sameas true %}logger.error("Could not connect to Ozone, due to error: ", e);{% endif %}
    }

  }


  public void write(String volumeName, String bucketName, String key, String value) {
  try {
            /*
            In class RPCClient of Ozone (which is the one used by default as a ClientProtocol implementation)
            Function createVolume() uses UserGroupInformation.createRemoteUser().getGroupNames() to get groups
            hence it gets all the groups of the logged user and adds them (which is not really good when you're working from a desktop or outside of the cluster machine)
             */
            objectStore.createVolume(volumeName);
        } catch (OMException e) {
            if (e.getResult() == OMException.ResultCodes.VOLUME_ALREADY_EXISTS) {
                {% if logger is sameas true %}logger.info("Volume: " + volumeName + " already exists ");{% endif %}
            } else {
                {% if logger is sameas true %}logger.error("An error occurred while creating volume " + volumeName + " : ", e);{% endif %}
            }
        } catch (IOException e) {
            {% if logger is sameas true %}logger.error("An unexpected exception occurred while creating volume " + volumeName + ": ", e);{% endif %}
        }

  try {
            OzoneVolume volume = objectStore.getVolume(volumeName);
            volume.createBucket(bucketName);
            {% if logger is sameas true %}logger.debug("Created successfully bucket : " + bucketName + " under volume : " + volumeName);{% endif %}
        } catch (OMException e) {
            if (e.getResult() == OMException.ResultCodes.BUCKET_ALREADY_EXISTS) {
                {% if logger is sameas true %}logger.info("Bucket: " + bucketName + " under volume : " + volumeName + " already exists ");{% endif %}
            } else {
                {% if logger is sameas true %}logger.error("An error occurred while creating volume " + volumeName + " : ", e);{% endif %}
            }
        } catch (IOException e) {
            {% if logger is sameas true %}logger.error("Could not create bucket to Ozone volume: "
               + volumeName + " and bucket : " + bucketName + " due to error: ", e);{% endif %}
        }

  try {
                OzoneVolume volume = objectStore.getVolume(volumeName);
                OzoneBucket bucket = volume.getBucket(bucketName);
                OzoneOutputStream os = bucket.createKey(key, value.length(), ReplicationType.RATIS, ReplicationFactor.ONE, new HashMap<>());
                os.write(value.getBytes());
                os.getOutputStream().flush();
                os.close();
            } catch (IOException e) {
                {% if logger is sameas true %}logger.error("Could not write key to Ozone volume: " + volumeName +
                        " and bucket : " + bucketName +
                        " and key: " + key + " due to error: ", e);{% endif %}
            }


  }

  public void read(String volumeName, String bucketName, String key) {

  try {
  OzoneVolume volume = objectStore.getVolume(volumeName);
  OzoneBucket bucket = volume.getBucket(bucketName);
  OzoneInputStream oi = bucket.readKey(key);
                String value = new BufferedReader(new InputStreamReader(oi, StandardCharsets.UTF_8)).lines()
                    .collect(Collectors.joining("\n"));
   {% if logger is sameas true %}logger.info("Value is: " + value);{% endif %}
   ozClient.close();
  } catch (IOException e) {
                {% if logger is sameas true %}logger.error("Could not read key from Ozone volume: " + volumeName +
                        " and bucket : " + bucketName +
                        " and key: " + key + " due to error: ", e);{% endif %}
            }

  }


}