#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#  http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#
import logging
import datetime
from jinja2 import Environment, select_autoescape, FileSystemLoader
from src.renderer import render_code_files, render_compiler_files, render_configuration_files, render_doc_files, \
    render_script_files
from src.orderer import order_files
from src.utils import clean_directory, check_files_and_compilation, create_folder
from src.commandline import command_line_arguments_to_dict, check_command_lines
from src.version import get_versions

generated_files_path = "/tmp/generated_files"
final_path = "../"


def main():

    start_time = datetime.datetime.now().timestamp()

    logger.info("Start to generate project files")

    clean_directory(generated_files_path)
    create_folder(generated_files_path)

    versions = get_versions(dict_of_options.get("version"))
    cdp_version=dict_of_options.get("version")+"-"+versions.get("build_version")

    render_code_files(env=env,
                      language=dict_of_options.get("language"),
                      spark_feature=dict_of_options.get("sparkFeature"),
                      logger_needed=dict_of_options.get("logger"),
                      package_name=dict_of_options.get("packageName"),
                      user=dict_of_options.get("user"),
                      hdfs_work_dir=dict_of_options.get("hdfsWorkdir"),
                      components=dict_of_options.get("components"),
                      program_type=dict_of_options.get("type"),
                      project_name=dict_of_options.get("projectName"),
                      hadoop_home=dict_of_options.get("hadoopHome"),
                      hadoop_user=dict_of_options.get("hadoopUser"),
                      kerberos_auth=dict_of_options.get("kerberos"),
                      kerberos_keytab=dict_of_options.get("keytab"),
                      kerberos_user=dict_of_options.get("principal"),
                      keystore_location=dict_of_options.get("keystoreLocation"),
                      keystore_password=dict_of_options.get("keystorePassword"),
                      keystore_key_password=dict_of_options.get("keystoreKeyPassword"),
                      truststore_location=dict_of_options.get("truststoreLocation"),
                      truststore_password=dict_of_options.get("truststorePassword"),
                      hdfs_nameservice=dict_of_options.get("hdfsNameservice"),
                      zookeeper_quorum=dict_of_options.get("zookeeperQuorum"),
                      ozone_nameservice=dict_of_options.get("ozoneNameservice"),
                      solr_server=dict_of_options.get("solrServer"),
                      kafka_broker=dict_of_options.get("kafkaBroker"),
                      kafka_security_protocol=dict_of_options.get("kafkaSecurityProtocol"),
                      schema_registry=dict_of_options.get("schemaRegistry"),
                      kudu_master=dict_of_options.get("kuduMaster")
                      )

    render_compiler_files(env=env,
                          compiler=dict_of_options.get("compiler"),
                          version=cdp_version,
                          spark_feature=dict_of_options.get("sparkFeature"),
                          project_name=dict_of_options.get("projectName"),
                          language=dict_of_options.get("language"),
                          package_name=dict_of_options.get("packageName"),
                          components=dict_of_options.get("components"),
                          logger_enabled=dict_of_options.get("logger"),
                          libs=dict_of_options.get("libs"),
                          fat_jar=dict_of_options.get("fatjar"),
                          program_type=dict_of_options.get("type"),
                          hdfs_version=versions.get("hdfs"),
                          hbase_version=versions.get("hbase"),
                          hive_version=versions.get("hive"),
                          flink_version=versions.get("flink"),
                          kafka_version=versions.get("kafka"),
                          kudu_version=versions.get("kudu"),
                          ozone_version=versions.get("ozone"),
                          solr_version=versions.get("solr"),
                          spark_version=versions.get("spark")
                          )

    render_script_files(env=env,
                        language=dict_of_options.get("language"),
                        spark_feature=dict_of_options.get("sparkFeature"),
                        kerberos=dict_of_options.get("kerberos"),
                        project_name=dict_of_options.get("projectName"),
                        logger_needed=dict_of_options.get("logger"),
                        package_name=dict_of_options.get("packageName"),
                        compiler=dict_of_options.get("compiler"),
                        principal=dict_of_options.get("principal"),
                        keytab=dict_of_options.get("keytab"),
                        host=dict_of_options.get("host"),
                        user=dict_of_options.get("user"),
                        fat_jar=dict_of_options.get("fatjar"),
                        program_type=dict_of_options.get("type"))

    render_configuration_files(env=env,
                               language=dict_of_options.get("language"),
                               spark_feature=dict_of_options.get("sparkFeature"),
                               project_name=dict_of_options.get("projectName"),
                               user=dict_of_options.get("user"),
                               logger_needed=dict_of_options.get("logger"),
                               hdfs_work_dir=dict_of_options.get("hdfsWorkdir"),
                               components=dict_of_options.get("components"),
                               libs=dict_of_options.get("libs"),
                               program_type=dict_of_options.get("type"),
                               hadoop_home=dict_of_options.get("hadoopHome"),
                               hadoop_user=dict_of_options.get("hadoopUser"),
                               kerberos_auth=dict_of_options.get("kerberos"),
                               kerberos_keytab=dict_of_options.get("keytab"),
                               kerberos_user=dict_of_options.get("principal"),
                               keystore_location=dict_of_options.get("keystoreLocation"),
                               keystore_password=dict_of_options.get("keystorePassword"),
                               keystore_key_password=dict_of_options.get("keystoreKeyPassword"),
                               truststore_location=dict_of_options.get("truststoreLocation"),
                               truststore_password=dict_of_options.get("truststorePassword"),
                               hdfs_nameservice=dict_of_options.get("hdfsNameservice"),
                               zookeeper_quorum=dict_of_options.get("zookeeperQuorum"),
                               ozone_nameservice=dict_of_options.get("ozoneNameservice"),
                               solr_server=dict_of_options.get("solrServer"),
                               kafka_broker=dict_of_options.get("kafkaBroker"),
                               kafka_security_protocol=dict_of_options.get("kafkaSecurityProtocol"),
                               schema_registry=dict_of_options.get("schemaRegistry"),
                               kudu_master=dict_of_options.get("kuduMaster"),
                               tls=dict_of_options.get("tls")
                               )

    render_doc_files(env=env,
                     language=dict_of_options.get("language"),
                     compiler=dict_of_options.get("compiler"),
                     version=cdp_version,
                     kerberos=dict_of_options.get("kerberos"),
                     project_name=dict_of_options.get("projectName"),
                     doc_type=dict_of_options.get("docFiles"),
                     components=dict_of_options.get("components"),
                     program_type=dict_of_options.get("type"))

    order_files(language=dict_of_options.get("language"),
                compiler=dict_of_options.get("compiler"),
                project_name=dict_of_options.get("projectName"),
                logger_needed=dict_of_options.get("logger"),
                package_name=dict_of_options.get("packageName"),
                libs=dict_of_options.get("libs"))

    logger.info("Finished to generate project files")

    time_spent_in_code_gen_in_ms = (datetime.datetime.now().timestamp() - start_time) * 1000
    logger.info("Generation of code took : %.2f milliseconds", time_spent_in_code_gen_in_ms)

    check_files_and_compilation(compiler=dict_of_options.get("compiler"),
                                compilation=dict_of_options.get("compilation"),
                                project_path=final_path+dict_of_options.get("projectName"))

    time_spent_in_ms = (datetime.datetime.now().timestamp()-start_time)*1000
    if time_spent_in_ms > 10*1000:
        logger.info("Execution of project generator took : %.2f seconds", time_spent_in_ms/1000)
    else:
        logger.info("Execution of project generator took : %.2f milliseconds", time_spent_in_ms)


if __name__ == "__main__":
    # Prepare logger
    logger = logging.getLogger("project_generator")
    logger.setLevel(logging.DEBUG)

    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

    file_handler = logging.FileHandler("project_generator.log")
    file_handler.setLevel(logging.INFO)
    file_handler.setFormatter(formatter)

    console_handler = logging.StreamHandler()
    console_handler.setLevel(logging.DEBUG)
    console_handler.setFormatter(formatter)

    logger.addHandler(file_handler)
    logger.addHandler(console_handler)

    # Get and check command line options
    dict_of_options = command_line_arguments_to_dict()
    check_command_lines(dict_of_options)

    # Load environment
    env = Environment(
        loader=FileSystemLoader('templates/'),
        autoescape=select_autoescape(['md', 'adoc', 'xml', 'conf', 'sh', 'java', 'scala', 'py', 'properties'])
    )

    main()
