import logging
import datetime
from jinja2 import Environment, select_autoescape, FileSystemLoader
from renderer import render_code_files, render_compiler_files, render_configuration_files, render_doc_files, \
    render_script_files
from orderer import order_files
from utils import clean_directory, check_files_and_compilation, create_folder
from commandline import command_line_arguments_to_dict, check_command_lines

target_path = "/tmp/target"

def main():

    start_time = datetime.datetime.now().timestamp()

    logger.info("Start to generate project files")

    clean_directory(target_path)
    create_folder(target_path)

    render_code_files(env, dict_of_options.get("language"), dict_of_options.get("feature"),
                      dict_of_options.get("logger"), dict_of_options.get("packageName"),
                      dict_of_options.get("user"), dict_of_options.get("hdfsNameservice"),
                      dict_of_options.get("hdfsWorkDir"), dict_of_options.get("components"))

    render_compiler_files(env, dict_of_options.get("compiler"), dict_of_options.get("version"),
                          dict_of_options.get("spark-feature"), dict_of_options.get("projectName"),
                          dict_of_options.get("language"), dict_of_options.get("packageName"),
                          dict_of_options.get("components"), dict_of_options.get("logger"),
                          dict_of_options.get("libs"), dict_of_options.get("fatjar"))

    render_script_files(env, dict_of_options.get("language"),
                        dict_of_options.get("spark-feature"), dict_of_options.get("kerberos"),
                        dict_of_options.get("projectName"), dict_of_options.get("logger"),
                        dict_of_options.get("packageName"), dict_of_options.get("compiler"),
                        dict_of_options.get("principal"), dict_of_options.get("keytab"),
                        dict_of_options.get("host"), dict_of_options.get("user"),
                        dict_of_options.get("fatjar"))

    render_configuration_files(env, dict_of_options.get("language"), dict_of_options.get("feature"),
                               dict_of_options.get("projectName"),
                               dict_of_options.get("user"), dict_of_options.get("hdfsNameservice"),
                               dict_of_options.get("logger"), dict_of_options.get("hdfsWorkDir"),
                               dict_of_options.get("components"), dict_of_options.get("libs"))

    render_doc_files(env, dict_of_options.get("language"), dict_of_options.get("compiler"),
                     dict_of_options.get("version"), dict_of_options.get("kerberos"),
                     dict_of_options.get("projectName"), dict_of_options.get("docFiles"),
                     dict_of_options.get("components"))

    logger.info("Finished to generate project files")

    order_files(dict_of_options.get("language"), dict_of_options.get("compiler"),
                dict_of_options.get("projectName"), dict_of_options.get("logger"),
                dict_of_options.get("packageName"), dict_of_options.get("libs"))

    time_spent_in_code_gen_in_ms = (datetime.datetime.now().timestamp() - start_time) * 1000

    check_files_and_compilation(dict_of_options.get("compiler"),
                                dict_of_options.get("projectName"), dict_of_options.get("compilation"))

    logger.info("Generation of code took : %.2f milliseconds", time_spent_in_code_gen_in_ms)
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
        loader=FileSystemLoader('../templates/'),
        autoescape=select_autoescape(['md', 'adoc', 'xml', 'conf', 'sh', 'java', 'scala', 'py', 'properties'])
    )

    main()
