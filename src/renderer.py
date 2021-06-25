import logging
from jinja2 import Environment
from src.utils import write_template_to_file

logger = logging.getLogger("project_generator")
generated_files_path = "/tmp/generated_files"


def render_code_files(env: Environment,
                      language: str,
                      spark_feature,
                      logger_needed: bool,
                      package_name: str,
                      user: str,
                      components,
                      program_type: str,
                      project_name: str,
                      hadoop_user: str,
                      hadoop_home: str,
                      kerberos_auth: str,
                      kerberos_user: str,
                      kerberos_keytab: str,
                      keystore_location: str,
                      keystore_password: str,
                      keystore_key_password: str,
                      truststore_location: str,
                      truststore_password: str,
                      hdfs_nameservice: str,
                      hdfs_work_dir: str,
                      zookeeper_quorum: str,
                      ozone_nameservice: str,
                      solr_server: str,
                      kafka_broker: str,
                      kafka_security_protocol: str,
                      schema_registry: str,
                      kudu_master: str
                      ):
    """
        Generate code files according to language and features needed
    :param env:
    :param language:
    :param spark_feature:
    :param logger_needed:
    :param package_name:
    :param user:
    :param components:
    :param program_type:
    :param project_name:
    :param haddop_user:
    :param hadoop_home:
    :param kerberos_auth:
    :param kerberos_user:
    :param kerberos_keytab:
    :param keystore_location:
    :param keystore_password:
    :param keystore_key_password:
    :param truststore_location:
    :param truststore_password:
    :param hdfs_nameservice:
    :param hdfs_work_dir:
    :param zookeeper_quorum:
    :param ozone_nameservice:
    :param solr_server:
    :param kafka_broker:
    :param kafka_security_protocol:
    :param schema_registry:
    :param kudu_master:
    :return:
    """
    if language == "python":
        language_extension = "py"
    else:
        language_extension = language

    write_template_to_file(
        env.get_template("code/" + language + "/App." + language_extension)
        .render(language=language,
                spark_feature=spark_feature,
                logger=logger_needed,
                package_name=package_name,
                components=components,
                program_type=program_type,
                project_name=project_name),
        generated_files_path + "/App." + language_extension)

    write_template_to_file(
            env.get_template("code/" + language + "/Treatment." + language_extension)
            .render(language=language,
                    spark_feature=spark_feature,
                    logger=logger_needed,
                    package_name=package_name,
                    components=components,
                    program_type=program_type),
            generated_files_path + "/Treatment." + language_extension)

    write_template_to_file(
        env.get_template("code/" + language + "/Utils." + language_extension)
        .render(language=language,
                logger=logger_needed,
                package_name=package_name,
                components=components),
        generated_files_path + "/Utils." + language_extension)

    write_template_to_file(
            env.get_template("code/" + language + "/AppConfig." + language_extension)
            .render(spark_feature=spark_feature,
                    package_name=package_name,
                    user=user,
                    logger=logger_needed,
                    components=components,
                    program_type=program_type,
                    project_name=project_name,
                    hadoop_home=hadoop_home,
                    hadoop_user=hadoop_user,
                    kerberos_auth=kerberos_auth,
                    kerberos_keytab=kerberos_keytab,
                    kerberos_user=kerberos_user,
                    keystore_location=keystore_location,
                    keystore_password=keystore_password,
                    keystore_key_password=keystore_key_password,
                    truststore_location=truststore_location,
                    truststore_password=truststore_password,
                    hdfs_nameservice=hdfs_nameservice,
                    zookeeper_quorum=zookeeper_quorum,
                    ozone_nameservice=ozone_nameservice,
                    solr_server=solr_server,
                    kafka_broker=kafka_broker,
                    kafka_security_protocol=kafka_security_protocol,
                    schema_registry=schema_registry,
                    kudu_master=kudu_master
                    ),
            generated_files_path + "/AppConfig." + language_extension)

    if "hdfs" in components:
        write_template_to_file(
            env.get_template("code/" + language + "/HdfsClient." + language_extension)
            .render(language=language,
                    logger=logger_needed,
                    package_name=package_name),
            generated_files_path + "/HdfsClient." + language_extension)

    if "hbase" in components:
        write_template_to_file(
            env.get_template("code/" + language + "/HbaseClient." + language_extension)
            .render(language=language,
                    logger=logger_needed,
                    package_name=package_name),
            generated_files_path + "/HbaseClient." + language_extension)

    if "hive" in components:
        write_template_to_file(
            env.get_template("code/" + language + "/HiveClient." + language_extension)
            .render(language=language,
                    logger=logger_needed,
                    package_name=package_name),
            generated_files_path + "/HiveClient." + language_extension)

    if "kafka" in components:
        write_template_to_file(
            env.get_template("code/" + language + "/KafkaClient." + language_extension)
            .render(language=language,
                    logger=logger_needed,
                    package_name=package_name),
            generated_files_path + "/KafkaClient." + language_extension)

    if "kudu" in components:
        write_template_to_file(
            env.get_template("code/" + language + "/KuduClientTest." + language_extension)
            .render(language=language,
                    logger=logger_needed,
                    package_name=package_name),
            generated_files_path + "/KuduClientTest." + language_extension)

    if "ozone" in components:
        write_template_to_file(
            env.get_template("code/" + language + "/OzoneClientTest." + language_extension)
            .render(language=language,
                    logger=logger_needed,
                    package_name=package_name),
            generated_files_path + "/OzoneClientTest." + language_extension)

    if "solr" in components:
        write_template_to_file(
            env.get_template("code/" + language + "/SolRClient." + language_extension)
            .render(language=language,
                    logger=logger_needed,
                    package_name=package_name),
            generated_files_path + "/SolRClient." + language_extension)

    logger.debug("Generated code files for language : %s with components : %s", language, str(components))


def render_compiler_files(env: Environment,
                          compiler: str,
                          version: str,
                          spark_feature,
                          project_name: str,
                          language: str,
                          package_name: str,
                          components,
                          logger_enabled: bool,
                          libs,
                          fat_jar: bool,
                          program_type: str,
                          hdfs_version: str,
                          hbase_version: str,
                          hive_version: str,
                          flink_version: str,
                          kafka_version: str,
                          kudu_version: str,
                          ozone_version: str,
                          solr_version: str,
                          spark_version: str):
    """
        Generate files needed by compiler such as pom.xml or build.sbt
    :param env:
    :param compiler:
    :param version:
    :param spark_feature:
    :param project_name:
    :param language:
    :param package_name:
    :param components:
    :param logger_enabled:
    :param libs:
    :param fat_jar:
    :param program_type:
    :return:
    """
    if compiler == "maven":

        write_template_to_file(
            env.get_template("compiler/maven/pom.xml")
            .render(version=version,
                    spark_feature=spark_feature,
                    project_name=project_name,
                    language=language,
                    package_name=package_name,
                    components=components,
                    logger=logger_enabled,
                    libs=libs,
                    fat_jar=fat_jar,
                    type=program_type,
                    hdfs_version=hdfs_version,
                    hbase_version=hbase_version,
                    hive_version=hive_version,
                    flink_version=flink_version,
                    kafka_version=kafka_version,
                    kudu_version=kudu_version,
                    ozone_version=ozone_version,
                    solr_version=solr_version,
                    spark_version=spark_version
                    ),
            generated_files_path + "/pom.xml")

    elif compiler == "sbt":

        write_template_to_file(
            env.get_template("compiler/sbt/build.sbt")
            .render(version=version,
                    spark_feature=spark_feature,
                    project_name=project_name,
                    components=components,
                    logger=logger_enabled,
                    libs=libs,
                    fat_jar=fat_jar,
                    program_type=program_type,
                    package_name=package_name,
                    hdfs_version=hdfs_version,
                    hbase_version=hbase_version,
                    hive_version=hive_version,
                    flink_version=flink_version,
                    kafka_version=kafka_version,
                    kudu_version=kudu_version,
                    ozone_version=ozone_version,
                    solr_version=solr_version,
                    spark_version=spark_version
                    ),
            generated_files_path + "/build.sbt"
        )

        write_template_to_file(
            env.get_template("compiler/sbt/assembly.sbt")
            .render(),
            generated_files_path + "/assembly.sbt"
        )

        write_template_to_file(
            env.get_template("compiler/sbt/build.properties")
            .render(),
            generated_files_path + "/build.properties"
        )

        write_template_to_file(
            env.get_template("compiler/sbt/plugins.sbt")
            .render(),
            generated_files_path + "/plugins.sbt"
        )

    logger.debug("Generated compiler files for compiler : %s with version : %s " +
                 "with fat jar generation : %s", compiler, version, fat_jar)


def render_script_files(env: Environment,
                        language: str,
                        spark_feature,
                        kerberos: bool,
                        project_name: str,
                        logger_needed: bool,
                        package_name: str,
                        compiler: str,
                        principal: str,
                        keytab: str,
                        host: str,
                        user: str,
                        fat_jar: bool,
                        program_type: str):
    """
        Generate script files to deploy spark project (hence spark-submit.sh and other if needed)
    :param env:
    :param language:
    :param spark_feature:
    :param kerberos:
    :param project_name:
    :param logger_needed:
    :param package_name:
    :param compiler:
    :param principal:
    :param keytab:
    :param host:
    :param user:
    :param fat_jar:
    :param program_type:
    :return:
    """
    write_template_to_file(
            env.get_template("scripts/launchToCDP.sh")
            .render(language=language,
                    project_name=project_name,
                    logger=logger_needed,
                    compiler=compiler,
                    host=host,
                    user=user,
                    fat_jar=fat_jar),
            generated_files_path + "/launchToCDP.sh")

    write_template_to_file(
        env.get_template("scripts/launch.sh")
        .render(language=language,
                project_name=project_name,
                user=user),
        generated_files_path + "/launch.sh")

    write_template_to_file(
        env.get_template("scripts/launchFromIDE.sh")
        .render(host=host,
                language=language),
        generated_files_path + "/launchFromIDE.sh")

    if program_type == "spark":
        write_template_to_file(
            env.get_template("scripts/spark-submit.sh")
            .render(language=language,
                    project_name=project_name,
                    kerberos=kerberos,
                    logger=logger_needed,
                    package_name=package_name,
                    principal=principal,
                    keytab=keytab),
            generated_files_path + "/spark-submit.sh")

    logger.debug("Generated script files for language : %s " +
                 ", compiler : %s, kerberos : %s, logger : %s", language, compiler, kerberos, logger_needed)


def render_configuration_files(env: Environment,
                               language: str,
                               spark_feature,
                               project_name: str,
                               user: str,
                               logger_needed: bool,
                               components,
                               libs,
                               program_type: str,
                               hadoop_user: str,
                               hadoop_home: str,
                               kerberos_auth: str,
                               kerberos_user: str,
                               kerberos_keytab: str,
                               keystore_location: str,
                               keystore_password: str,
                               keystore_key_password: str,
                               truststore_location: str,
                               truststore_password: str,
                               hdfs_nameservice: str,
                               hdfs_work_dir: str,
                               zookeeper_quorum: str,
                               ozone_nameservice: str,
                               solr_server: str,
                               kafka_broker: str,
                               kafka_security_protocol: str,
                               schema_registry: str,
                               kudu_master: str,
                               tls: bool
                               ):
    """
        Generate configuration file (for logging and external variables)
    :param env:
    :param language:
    :param spark_feature:
    :param project_name:
    :param user:
    :param logger_needed:
    :param components:
    :param libs:
    :param program_type:
    :param haddop_user:
    :param hadoop_home:
    :param kerberos_auth:
    :param kerberos_user:
    :param kerberos_keytab:
    :param keystore_location:
    :param keystore_password:
    :param keystore_key_password:
    :param truststore_location:
    :param truststore_password:
    :param hdfs_nameservice:
    :param hdfs_work_dir:
    :param zookeeper_quorum:
    :param ozone_nameservice:
    :param solr_server:
    :param kafka_broker:
    :param kafka_security_protocol:
    :param schema_registry:
    :param kudu_master:
    :return:
    """
    if logger_needed:
        if language == "scala" or language == "java":
            write_template_to_file(
                env.get_template("configuration/log4j.properties")
                .render(project_name=project_name),
                generated_files_path + "/log4j.properties")

            write_template_to_file(
                env.get_template("configuration/log4j2.properties")
                .render(project_name=project_name),
                generated_files_path + "/log4j2.properties")

    if language == "scala" or "typesafe" in libs:

        write_template_to_file(
            env.get_template("configuration/application.conf")
            .render(spark_feature=spark_feature,
                    project_name=project_name,
                    user=user,
                    hdfsWorkDir=hdfs_work_dir,
                    components=components,
                    hadoop_home=hadoop_home,
                    hadoop_user=hadoop_user,
                    kerberos_auth=kerberos_auth,
                    kerberos_keytab=kerberos_keytab,
                    kerberos_user=kerberos_user,
                    keystore_location=keystore_location,
                    keystore_password=keystore_password,
                    keystore_key_password=keystore_key_password,
                    truststore_location=truststore_location,
                    truststore_password=truststore_password,
                    hdfs_nameservice=hdfs_nameservice,
                    zookeeper_quorum=zookeeper_quorum,
                    ozone_nameservice=ozone_nameservice,
                    solr_server=solr_server,
                    kafka_broker=kafka_broker,
                    kafka_security_protocol=kafka_security_protocol,
                    schema_registry=schema_registry,
                    kudu_master=kudu_master,
                    tls=tls
                    ),
            generated_files_path + "/application.conf")

    elif language == "java":

        write_template_to_file(
            env.get_template("configuration/application.properties")
            .render(spark_feature=spark_feature,
                    project_name=project_name,
                    user=user,
                    hdfsWorkDir=hdfs_work_dir,
                    components=components,
                    hadoop_home=hadoop_home,
                    hadoop_user=hadoop_user,
                    kerberos_auth=kerberos_auth,
                    kerberos_keytab=kerberos_keytab,
                    kerberos_user=kerberos_user,
                    keystore_location=keystore_location,
                    keystore_password=keystore_password,
                    keystore_key_password=keystore_key_password,
                    truststore_location=truststore_location,
                    truststore_password=truststore_password,
                    hdfs_nameservice=hdfs_nameservice,
                    zookeeper_quorum=zookeeper_quorum,
                    ozone_nameservice=ozone_nameservice,
                    solr_server=solr_server,
                    kafka_broker=kafka_broker,
                    kafka_security_protocol=kafka_security_protocol,
                    schema_registry=schema_registry,
                    kudu_master=kudu_master,
                    tls=tls
                    ),
            generated_files_path + "/application.properties")

    logger.debug("Generated configuration files for language : %s", language)


def render_doc_files(env: Environment,
                     language: str,
                     compiler: str,
                     version: str,
                     kerberos: bool,
                     project_name: str,
                     doc_type: str,
                     components,
                     program_type: str):
    """
        Generate a doc file as a README.MD or README.ADOC file for the project
    :param env:
    :param language:
    :param compiler:
    :param version:
    :param kerberos:
    :param project_name:
    :param doc_type:
    :param components:
    :param program_type:
    :return:
    """
    write_template_to_file(
        env.get_template("docs/README." + doc_type)
        .render(language=language,
                compiler=compiler,
                kerberos=kerberos,
                version=version,
                project_name=project_name,
                components=components,
                program_type=program_type),
        generated_files_path + "/README." + doc_type)

    logger.debug("Generated doc files for language : %s with components: %s" +
                 ", compiler : %s ; version : %s ; kerberos : %s ; project_name : %s",
                 language, str(components), compiler, version, kerberos, project_name)
