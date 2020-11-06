{% if logger is sameas true %}import logging{% endif %}
{% if "spark" == program_type %}
{% if "core" or "streaming" is in feature %}from pyspark import SparkContext, SparkConf{% endif %}
{% if "sql" or "structured_streaming" is in feature %}from pyspark.sql import SparkSession{% endif %}
{% if "streaming" is in feature %}from pyspark.streaming import StreamingContext{% endif %}
{% endif %}
from Treatment import *
from AppConfig import *



def main():

    {% if "spark" == program_type %}
    {% if "core" or "streaming" is in feature %}
    conf = SparkConf().setAppName(app_name).setMaster(master)
    sc = SparkContext(conf=conf){% endif %}
    {% if "core" is in feature %}treatment(sc){% endif %}

    {% if "sql" or "structured_streaming" is in feature %}
    spark = SparkSession \
        .builder \
        .appName(app_name) \
        .config("spark.sql.streaming.schemaInference", "true") \
        .getOrCreate(){% endif %}

    {% if "sql" is in feature %}treatment_sql(spark){% endif %}
    {% if "structured_streaming" is in feature %}treatment_structured_streaming(spark){% endif %}

    {% if "streaming" is in feature %}ssc = StreamingContext(sc, streaming_time)
    treatment_streaming(ssc)
    ssc.start()
    ssc.awaitTermination(){% endif %}
    {% else %}
    print("Starting app: "  + app_name)

    print("Finished app: " + app_name)
    {% endif %}

if __name__ == "__main__":
    {% if logger is sameas true %}# Prepare logger
    logger = logging.getLogger("{{ project_name }}")
    logger.setLevel(logging.DEBUG)

    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

    file_handler = logging.FileHandler("{{ project_name }}.log")
    file_handler.setLevel(logging.INFO)
    file_handler.setFormatter(formatter)

    console_handler = logging.StreamHandler()
    console_handler.setLevel(logging.DEBUG)
    console_handler.setFormatter(formatter)

    logger.addHandler(file_handler)
    logger.addHandler(console_handler){% endif %}

    main()
