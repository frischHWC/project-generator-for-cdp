package {{ package_name }}

{% if logger is sameas true %}import org.apache.logging.log4j.scala.Logging{% endif %}
{% if program_type == "spark" %}
{% if "core" is in feature %}import org.apache.spark.SparkContext{% endif %}
{% if "sql" is in feature %}import org.apache.spark.sql.SparkSession{% endif %}
{% if "streaming" is in feature %}import org.apache.spark.streaming.StreamingContext{% endif %}
{% endif %}

import {{ package_name }}.client.HdfsClient

object Treatment {% if logger is sameas true %}extends Logging{% endif %}{

{% if program_type == "spark" %}
  {% if "core" is in feature %}/**
    * Spark Simple Treatment
    */
  def treatment(sc: SparkContext): Unit = {

    // TODO : Replace lines belows by your code using the SparkContext passed in argument

    val rdd = sc.parallelize(Array(0,1,2,3,4,5))
    {% if logger is sameas true %}rdd.foreach(x => {logger.info("x * 2 = " + x*2)}){% endif %}
    {% if logger is sameas true %}logger.info("Mean is : " + rdd.mean().toString){% endif %}

  } {% endif %}

  {% if "sql" is in feature %}/**
    * Spark SQL treatment
    */
  def sqlTreatment(spark: SparkSession): Unit = {

  // TODO : Replace lines belows by your code using the SparkSession passed in argument
  val dfInit = spark.read
    .option("sep", ",")
    .option("inferSchema", "true")
    .option("header", "true")
    .csv(AppConfig.hdfs + AppConfig.hdfsHomeDir + "random-data.csv")

    dfInit.show(false)

  } {% endif %}

  {% if "structured_streaming" is in feature %}/**
    * Spark Structured Streaming Treatment
    */
  def structuredStreamingTreatment(spark: SparkSession): Unit = {

    // TODO : Replace lines belows by your code using the SparkSession passed in argument

    val dfStreamed = spark.readStream
      .parquet(AppConfig.hdfs + AppConfig.hdfsHomeDir)

    dfStreamed.writeStream.format("parquet")
      .option("checkpointLocation", AppConfig.hdfs + AppConfig.hdfsHomeDir + "checkpoints/")
      .option("path", AppConfig.hdfs + AppConfig.hdfsHomeDir + "random-data-2.parquet")
      .start()

    spark.streams.awaitAnyTermination()

  }{% endif %}

    {% if "streaming" is in feature %}/**
    * Spark streaming treatment to enrich data
    */
  def streamingTreatment(ssc: StreamingContext): Unit = {

   // TODO : Replace lines belows by your code using the SparkSession passed in argument
   val streamfile = ssc.textFileStream(AppConfig.hdfs + AppConfig.hdfsHomeDir + "streaming/")

    streamfile.foreachRDD(rdd => {
      rdd.foreach(record => {
        logger.info("Record is : " + record)
      })
    })

  }{% endif %}
  {% else %}

  def treatment(): Unit = {

    // TODO: Write code here
    {% if "hdfs" is in components %}
    val hdfsClient = HdfsClient.apply()
    hdfsClient.write("test", "/tmp/test")
    hdfsClient.read("/tmp/test")
    {% endif %}

    {% if "hbase" is in components %}
    val hbaseClient = HbaseClient.apply()
    hbaseClient.write("namespace", "tableName", "columnFamily", "col1", "col2", "value1", "value2", "key")
    hbaseClient.read("namespace", "tableName", "columnFamily", "col1", "key")
    {% endif %}

    {% if "ozone" is in components %}
    val ozoneClient = OzoneClientTest.apply()
    ozoneClient.write("volumename", "bucketname", "key", "value")
    ozoneClient.read("volumename", "bucketname", "key")
    {% endif %}

    {% if "hive" is in components %}
    val hiveClient = HiveClient.apply()
    hiveClient.write("test")
    hiveClient.read("test")
    {% endif %}

    {% if "kafka" is in components %}
    val kafkaClient = KafkaClient.apply()
    kafkaClient.write("key", "value", "topic_test")
    kafkaClient.read("topic_test")
    {% endif %}

    {% if "kudu" is in components %}
    val kuduClient = KuduClientTest.apply()
    kuduClient.write("hashkey", "tablename", "value1")
    kuduClient.read("tablename")
    {% endif %}

    {% if "solr" is in components %}
    val solrClient = SolRClient.apply()
    solrClient.write("test")
    solrClient.read("test")
    {% endif %}

  }
  {% endif %}

}
