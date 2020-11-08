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

  {% if "hdfs" is in components %}"org.apache.hadoop" % "hadoop-client" % ("3.1.1." + cdpVersion),{% endif %}
  {% if "hbase" is in components %}"org.apache.hbase" % "hbase-client" % ("2.2.3." + cdpVersion),{% endif %}
  {% if "hive" is in components %}"org.apache.hive" % "hive-jdbc" % ("3.1.3000." + cdpVersion),{% endif %}
  {% if "kafka" is in components %}"org.apache.kafka" % "kafka-clients" % ("2.4.1." + cdpVersion),{% endif %}
  {% if "kudu" is in components %}"org.apache.kudu" % "kudu-client" % ("1.12.0." + cdpVersion),{% endif %}
  {% if "ozone" is in components %}"org.apache.hadoop" % "hadoop-ozone-client" % ("0.5.0." + cdpVersion),
  "com.google.guava" % "guava" % "28.2-jre",{% endif %}
  {% if "solr" is in components %}"org.apache.solr" % "solr-solrj" % ("8.4.1." + cdpVersion),{% endif %}

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
