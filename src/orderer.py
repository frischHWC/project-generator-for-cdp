import logging
import os
from utils import create_folder, clean_directory, copy_file, move_file

logger = logging.getLogger("project_generator")
target_path = "/tmp/target"


def order_files(language: str, compiler: str, project_name: str, logger_needed: bool, package_name: str, libs):
    """
    Create project and all other needed folders
    Copy generated files into right folders
    :param language:
    :param compiler:
    :param project_name:
    :param logger_needed:
    :param package_name:
    :return:
    """
    folder_path = "../../" + project_name + "/"
    package_path = package_name.replace(".", "/") + "/"

    # Remove older folder if it exists
    clean_directory(folder_path)

    # Create main folder
    create_folder(folder_path)

    # Create and order resources files folder
    if language == "scala" or language == "java":
        create_folder(folder_path + "src/main/resources/")
        if language == "scala" or "typesafe" in libs:
            copy_file(target_path + "/application.conf", folder_path + "src/main/resources/application.conf")
        else:
            copy_file(target_path + "/application.properties", folder_path + "src/main/resources/application.properties")
        if logger_needed:
            copy_file(target_path + "/log4j.properties", folder_path + "src/main/resources/log4j.properties")
    elif language == "python":
        create_folder(folder_path + "resources/")

    # Create and order scripts files folder
    if language == "scala" or language == "java":
        create_folder(folder_path + "src/main/resources/scripts/")
        copy_file(target_path + "/launch.sh", folder_path + "src/main/resources/scripts/launch.sh")
        copy_file(target_path + "/launchFromIDE.sh", folder_path + "src/main/resources/scripts/launchFromIDE.sh")
        copy_file(target_path + "/launchToCDP.sh", folder_path + "src/main/resources/scripts/launchToCDP.sh")
        if "spark" in libs:
            copy_file(target_path + "/spark-submit.sh", folder_path + "src/main/resources/scripts/spark-submit.sh")
    elif language == "python":
        create_folder(folder_path + "resources/scripts/")
        copy_file(target_path + "/launch.sh", folder_path + "resources/scripts/launch.sh")
        copy_file(target_path + "/launchFromIDE.sh", folder_path + "resources/scripts/launchFromIDE.sh")
        copy_file(target_path + "/launchToCDP.sh", folder_path + "resources/scripts/launchToCDP.sh")
        if "spark" in libs:
            copy_file(target_path + "/spark-submit.sh", folder_path + "resources/scripts/spark-submit.sh")

    # Create and order code files folder
    if language == "scala" or language == "java":
        create_folder(folder_path + "src/main/" + language + "/" + package_path)
        create_folder(folder_path + "src/main/" + language + "/" + package_path + "config")
        copy_file(target_path + "/App." + language,
                  folder_path + "src/main/" + language + "/" + package_path + "/App." + language)
        copy_file(target_path + "/Treatment." + language,
                  folder_path + "src/main/" + language + "/" + package_path + "/Treatment." + language)
        copy_file(target_path + "/AppConfig." + language,
                  folder_path + "src/main/" + language + "/" + package_path + "config/AppConfig." + language)
    elif language == "python":
        create_folder(folder_path + "src/")
        files = [f for f in os.listdir(target_path + "/") if ".py" in f and "test" not in f]
        for file in files:
            copy_file(target_path + "/" + file, folder_path + file)

    # Create and order test files folder
    if language == "scala" or language == "java":
        create_folder(folder_path + "src/test/")
        files = [f for f in os.listdir(target_path + "/") if "." + language in f and "test" in f]
        for file in files:
            copy_file(target_path + "/" + file, folder_path + "src/test/" + language + "/" + package_path + file)
    elif language == "python":
        create_folder(folder_path + "test/")
        files = [f for f in os.listdir(target_path + "/") if ".py" in f and "test" in f]
        for file in files:
            copy_file(target_path + "/" + file, folder_path + "test/" + file)

    # Put compiler and doc files
    files = [f for f in os.listdir(target_path + "/") if ".xml" in f or ".sbt" in f or ".md" in f
             or ".adoc" in f]
    for file in files:
        copy_file(target_path + "/" + file, folder_path + file)

    # Arrange sbt compiler files
    if compiler == "sbt":
        create_folder(folder_path + "project/")
        move_file(folder_path + "build.properties", folder_path + "project/build.properties")
        move_file(folder_path + "assembly.sbt", folder_path + "project/assembly.sbt")
        move_file(folder_path + "plugins.sbt", folder_path + "project/plugins.sbt")

    # Make .sh files executable
    sh_files = [f for f in os.listdir(folder_path) if f.endswith(".sh")]
    for file in sh_files:
        os.chmod(os.path.join(folder_path, file), 0o755)

    logger.info("Finished to order files for language : %s and compiler : %s", language, compiler)
