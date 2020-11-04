name := "{{ project_name }}"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.12"

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
  "org.apache.logging.log4j" % "log4j-api" % "2.11.2",
  "org.apache.logging.log4j" % "log4j-core" % "2.11.2",
  {% endif %}

  {% if "spark" == type %}
  "org.apache.spark" %% "spark-core" % s"${sparkVersion}",{% if "sql" is in feature %}
  "org.apache.spark" %% "spark-sql" % s"${sparkVersion}",{% endif %}{% if "streaming" is in feature %}
  "org.apache.spark" %% "spark-streaming" % s"${sparkVersion}",{% if "kafka" is in techs %}
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % s"${sparkVersion}",{% endif %}{% endif %}
  {% endif %}

  "com.typesafe" % "config" % "1.3.2"
)

// Fat jar creation
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

assemblyJarName in assembly := s"${name.value}-${version.value}-jar-with-dependencies.jar"
