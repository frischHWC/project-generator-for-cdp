name := "{{ project_name }}"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.8"

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  DefaultMavenRepository,
  Resolver.typesafeRepo("releases"),
  Resolver.sbtPluginRepo("releases"),
  Resolver.sonatypeRepo("public")
)

val sparkVersion = "{{ version }}"

libraryDependencies ++= Seq(
{% if logger is sameas true %}
  "org.apache.logging.log4j" %% "log4j-api-scala" % "11.0",
  "org.apache.logging.log4j" % "log4j-api" % "2.11.0",
  "org.apache.logging.log4j" % "log4j-core" % "2.11.0" % Runtime,
  {% endif %}

  {% if "hdfs" is in components %}"org.apache.hadoop" % "hadoop-client" % ("{{ hdfs_version }}." + cdpVersion),{% endif %}
  {% if "hbase" is in components %}"org.apache.hbase" % "hbase-client" % ("{{ hbase_version }}." + cdpVersion),{% endif %}
  {% if "hive" is in components %}"org.apache.hive" % "hive-jdbc" % ("{{ hive_version }}." + cdpVersion),{% endif %}
  {% if "kafka" is in components %}"org.apache.kafka" % "kafka-clients" % ("{{ kafka_version }}." + cdpVersion),{% endif %}
  {% if "kudu" is in components %}"org.apache.kudu" % "kudu-client" % ("{{ kudu_version }}." + cdpVersion),{% endif %}
  {% if "ozone" is in components %}"org.apache.hadoop" % "hadoop-ozone-client" % ("{{ ozone_version }}." + cdpVersion),
  "com.google.guava" % "guava" % "28.2-jre",{% endif %}
  {% if "solr" is in components %}"org.apache.solr" % "solr-solrj" % ("{{ solr_version }}." + cdpVersion),{% endif %}

  {% if "spark" == program_type %}
  "org.apache.spark" %% "spark-core" % s"${sparkVersion}",{% if "sql" is in feature %}
  "org.apache.spark" %% "spark-sql" % s"${sparkVersion}",{% endif %}{% if "streaming" is in feature %}
  "org.apache.spark" %% "spark-streaming" % s"${sparkVersion}",{% if "kafka" is in techs %}
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % s"${sparkVersion}",{% endif %}{% endif %}
  {% endif %}

  "com.typesafe" % "config" % "1.3.2"
)

mainClass in Compile := Some("{{ package_name }}.App")
mainClass in assembly := Some("{{ package_name }}.App")

// Fat jar creation
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case x => MergeStrategy.first
}

assemblyJarName in assembly := s"${name.value}-${version.value}-jar-with-dependencies.jar"
