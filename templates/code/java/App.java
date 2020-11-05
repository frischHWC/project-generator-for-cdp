package {{ package_name }};

{% if logger is sameas true %}import org.apache.log4j.*;{% endif %}
{% if "spark" == program_type %}
{% if "core" is in spark_feature %}
import org.apache.spark.api.java.JavaSparkContext;
{% endif %}
{% if "core" is in spark_feature or "streaming" is in spark_feature %}
import org.apache.spark.SparkConf;
{% endif %}
{% if "streaming" is in spark_feature %}
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
{% endif %}
{% if "sql" is in spark_feature or "structured_streaming" is in spark_feature %}
import org.apache.spark.sql.SparkSession;
{% endif %}
{% endif %}

public class App {

   {% if logger is sameas true %}private static Logger logger = Logger.getLogger(App.class);{% endif %}

  /**
    * Main function that creates a SparkContext and launches treatment
    */
  public static void main(String[] args) {

    {% if logger is sameas true %}logger.info("Start Application");{% endif %}

    {% if "spark" is in components %}
    {% if "core" is in spark_feature %}// Creating Spark context
    SparkConf conf = new SparkConf().setMaster(appConfig.master)
      .setAppName(appConfig.name);
    JavaSparkContext sc = new JavaSparkContext(conf);

    // Launch treatment
    Treatment.treatment(sc); {% endif %}

    {% if "sql" is in spark_feature or "structured_streaming" is in spark_feature %}// Create Spark SQL Context
    SparkSession spark = SparkSession
      .builder()
      .appName(appConfig.name)
       {% if "structured_streaming" is in spark_feature %}.config("spark.sql.streaming.schemaInference", "true"){% endif %}
      .getOrCreate();

     // Launch treatment
    {% if "structured_streaming" is in spark_feature %}Treatment.structuredStreamingTreatment(spark);{% else %}Treatment.sqlTreatment(spark);{% endif %}{% endif %}

    {% if "streaming" is in spark_feature %}
    // Create Streaming context

     SparkConf conf = new SparkConf().setMaster(appConfig.master)
      .setAppName(appConfig.name);
    JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(appConfig.streamingTime));

    // Launch Treatment
    Treatment.streamingTreatment(ssc);

    // Launch computation
    ssc.start(); // Start the computation
    try {
      ssc.awaitTermination();  // Wait for the computation to terminate
    } catch(InterruptedException e) {
      logger.error("An error occurred while making a streaming treatment");
    } {% endif %}
    {% else %}

    Treatment.treatment();

    {% endif %}

    {% if logger is sameas true %}logger.info("Finish Application");{% endif %}

  }

}
