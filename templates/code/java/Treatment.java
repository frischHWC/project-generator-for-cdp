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
package {{ package_name }};

{% if logger is sameas true %}import org.apache.log4j.Logger;{% endif %}
{% if "spark" == program_type %}
{% if "core" is in spark_feature %}
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
{% endif %}
{% if "streaming" is in spark_feature %}
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
{% endif %}
{% if "structured_streaming" is in spark_feature or "sql" is in spark_feature %}
import org.apache.spark.sql.Row;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
{% endif %}
{% if "structured_streaming" is in spark_feature %}
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
{% endif %}
import java.util.Arrays;
import java.util.List;
{% endif %}
{% if "hdfs" is in components %}
import {{ package_name }}.client.HdfsClient;{% endif %}
{% if "hbase" is in components %}
import {{ package_name }}.client.HbaseClient;{% endif %}
{% if "hive" is in components %}
import {{ package_name }}.client.HiveClient;{% endif %}
{% if "kafka" is in components %}
import {{ package_name }}.client.KafkaClient;{% endif %}
{% if "kudu" is in components %}
import {{ package_name }}.client.KuduClientTest;{% endif %}
{% if "solr" is in components %}
import {{ package_name }}.client.SolRClient;{% endif %}
{% if "ozone" is in components %}
import {{ package_name }}.client.OzoneClientTest;{% endif %}


class Treatment {

  {% if logger is sameas true %}private static Logger logger = Logger.getLogger(Treatment.class);{% endif %}

  {% if "spark" == program_type %}
  {% if "core" is in spark_feature %}/**
    * Spark Simple Treatment
    */
  public static void treatment(JavaSparkContext sc) {

  // TODO : Replace lines belows by your code using the SparkContext passed in argument

    List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
    JavaRDD<Integer> rdd = sc.parallelize(data);

    rdd.foreach(x -> logger.info("x * 2 = " + Integer.toString(x * 2)));

    logger.info("Count is : " + Long.toString(rdd.count()));

  } {% endif %}

    {% if "sql" is in spark_feature %}/**
    * Spark SQL treatment
    */
  public static void sqlTreatment(SparkSession spark) {

  // TODO : Replace lines belows by your code using the SparkSession passed in argument

    // First goal is to load, format and write data :)
    Dataset<Row> dfInit = spark.read()
      .option("sep", ",")
      .option("inferSchema", "true")
      .option("header", "true")
      .csv(appConfig.hdfs + appConfig.hdfsHomeDir + "random-data.csv");

    dfInit.show(false);
  }{% endif %}

    {% if "structured_streaming" is in spark_feature %}/**
    * Spark Structured Streaming Treatment
    */
  public static void structuredStreamingTreatment(SparkSession spark) {

   // TODO : Replace lines belows by your code using the SparkSession passed in argument

    // Goal is to send data to Kafka
    Dataset<Row> dfStreamed = spark.readStream()
      .parquet(appConfig.hdfs + appConfig.hdfsHomeDir + "streaming/");

    dfStreamed.writeStream()
        .option("checkpointLocation", appConfig.hdfs + appConfig.hdfsHomeDir + "checkpoints/")
        .format("csv").start(appConfig.hdfs + appConfig.hdfsHomeDir + "random-data-2.parquet");

    try {
      spark.streams().awaitAnyTermination();
    } catch (StreamingQueryException e) {
      logger.error("An error occurred while making a structured streaming treatment");
    }

  }{% endif %}

    {% if "streaming" is in spark_feature %}/**
    * Spark streaming treatment to enrich data
    */
  public static void streamingTreatment(JavaStreamingContext ssc ){

  // TODO : Replace lines belows by your code using the SparkSession passed in argument

    JavaDStream<String> streamfile = ssc.textFileStream(appConfig.hdfs + appConfig.hdfsHomeDir + "streaming/");

    streamfile.foreachRDD(rdd -> {
      rdd.foreach(record -> {
        logger.info("Record is : " + record);
      });
    });

  } {% endif %}
  {% else %}

  public static void treatment() {

    // TODO: Write code here
    {% if "hdfs" is in components %}
    HdfsClient hdfsClient = new HdfsClient();
    hdfsClient.write("test", "/tmp/test");
    hdfsClient.read("/tmp/test");
    {% endif %}

    {% if "hbase" is in components %}
    HbaseClient hbaseClient = new HbaseClient();
    hbaseClient.write("namespace", "tableName", "columnFamily", "col1", "col2", "value1", "value2", "key");
    hbaseClient.read("namespace", "tableName", "columnFamily", "col1", "key");
    {% endif %}

    {% if "ozone" is in components %}
    OzoneClientTest ozoneClient = new OzoneClientTest();
    ozoneClient.write("volumename", "bucketname", "key", "value");
    ozoneClient.read("volumename", "bucketname", "key");
    {% endif %}

    {% if "hive" is in components %}
    HiveClient hiveClient = new HiveClient();
    hiveClient.write("test");
    hiveClient.read("test");
    {% endif %}

    {% if "kafka" is in components %}
    KafkaClient kafkaClient = new KafkaClient();
    kafkaClient.write("key", "value", "topic_test");
    kafkaClient.read("topic_test");
    {% endif %}

    {% if "kudu" is in components %}
    KuduClientTest kuduClient = new KuduClientTest();
    kuduClient.write("hashkey", "tablename", "value1");
    kuduClient.read("tablename");
    {% endif %}

    {% if "solr" is in components %}
    SolRClient solrClient = new SolRClient();
    solrClient.write("test");
    solrClient.read("test");
    {% endif %}

  }
  {% endif %}

}
