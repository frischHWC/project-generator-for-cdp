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
package {{ package_name }}.config;

{% if logger is sameas true %}import org.apache.log4j.Logger;{% endif %}

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class AppConfig {

    {% if logger is sameas true %}private static final Logger logger = Logger.getLogger(AppConfig.class);{% endif %}

    public static final Properties properties = loadProperties();

    private static Properties loadProperties() {
        // Load config file
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("application.properties");
            properties.load(fileInputStream);
        } catch (IOException e) {
            {% if logger is sameas true %}logger.error("Property file not found !", e);
             {% else %}System.out.println("Property file not found !");{% endif %}
        }
        return properties;
    }

    public static String getProperty(String key) {
        String property = "null";
        try {
             property = properties.getProperty(key);
            if(property.substring(0,2).equalsIgnoreCase("${")) {
                property = AppConfig.getProperty(property.substring(2,property.length()-1));
            }
        } catch (Exception e) {
            {% if logger is sameas true %}logger.warn("Could not get property : " + key + " due to following error: ", e);
            {% else %}System.out.println("Could not get property : " + key + " due to following error: " + e);{% endif %}
        }
        return property;
    }
}
