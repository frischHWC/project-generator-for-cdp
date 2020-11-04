import logging
import datetime
from jinja2 import Environment, select_autoescape, FileSystemLoader
from src.renderer import render_code_files, render_compiler_files, render_configuration_files, render_doc_files, \
    render_script_files
from src.orderer import order_files
from src.utils import clean_directory, check_files_and_compilation, create_folder
from src.commandline import command_line_arguments_to_dict, check_command_lines

generated_files_path = "/tmp/generated_files"
final_path = "../"


def main():

    start_time = datetime.datetime.now().timestamp()

    logger.info("Start to generate project files")

    clean_directory(generated_files_path)
    create_folder(generated_files_path)

    render_code_files(env=env,
                      language=dict_of_options.get("language"),
                      spark_feature=dict_of_options.get("spark-feature"),
                      logger_needed=dict_of_options.get("logger"),
                      package_name=dict_of_options.get("packageName"),
                      user=dict_of_options.get("user"),
                      hdfs_nameservice=dict_of_options.get("hdfsNameservice"),
                      hdfs_work_dir=dict_of_options.get("hdfsWorkDir"),
                      components=dict_of_options.get("components"),
                      type=dict_of_options.get("type"),
                      project_name=dict_of_options.get("projectName"))

    render_compiler_files(env=env,
                          compiler=dict_of_options.get("compiler"),
                          version=dict_of_options.get("version"),
                          spark_feature=dict_of_options.get("spark-feature"),
                          project_name=dict_of_options.get("projectName"),
                          language=dict_of_options.get("language"),
                          package_name=dict_of_options.get("packageName"),
                          components=dict_of_options.get("components"),
                          logger_enabled=dict_of_options.get("logger"),
                          libs=dict_of_options.get("libs"),
                          fat_jar=dict_of_options.get("fatjar"),
                          type=dict_of_options.get("type"))

    render_script_files(env=env,
                        language=dict_of_options.get("language"),
                        spark_feature=dict_of_options.get("spark-feature"),
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
                        type=dict_of_options.get("type"))

    render_configuration_files(env=env,
                               language=dict_of_options.get("language"),
                               spark_feature=dict_of_options.get("spark-feature"),
                               project_name=dict_of_options.get("projectName"),
                               user=dict_of_options.get("user"),
                               hdfs_nameservice=dict_of_options.get("hdfsNameservice"),
                               logger_needed=dict_of_options.get("logger"),
                               hdfs_work_dir=dict_of_options.get("hdfsWorkDir"),
                               components=dict_of_options.get("components"),
                               libs=dict_of_options.get("libs"),
                               type=dict_of_options.get("type"))

    render_doc_files(env=env,
                     language=dict_of_options.get("language"),
                     compiler=dict_of_options.get("compiler"),
                     version=dict_of_options.get("version"),
                     kerberos=dict_of_options.get("kerberos"),
                     project_name=dict_of_options.get("projectName"),
                     doc_type=dict_of_options.get("docFiles"),
                     components=dict_of_options.get("components"),
                     type=dict_of_options.get("type"))

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
