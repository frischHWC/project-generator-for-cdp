import logging
import os
import shutil

logger = logging.getLogger("project_generator")


def write_template_to_file(template: str, filename: str):
    """
    Write a string passed in argument to a file (which will be overwritten if it exists and created if it does not)
    :param template: string to write
    :param filename: path to the file to write to
    :return:
    """
    try:
        with open(filename, mode='x') as f:
            f.write(template)
    except Exception as e:
        logger.error("Could not write : \'%s\'  to file : \'%s\' with error : ",
                     template, filename, e)


def create_folder(path: str):
    """
    Create a folder and all its subdirectories if it does not exist
    :param path:
    :return:
    """
    try:
        os.makedirs(path)
    except Exception as e:
        logger.error("Could not create folder : %s", path, e)
        raise e


def clean_directory(folder_path: str):
    """
    Remove q directory with all its subdirectories and files
    :return:
    """
    try:
        if os.path.exists(folder_path):
            shutil.rmtree(folder_path)
    except FileNotFoundError as e:
        logger.warning("Folder does not exists !")


def copy_file(source: str, target: str):
    """
    Copy a file from source to target
    :param source:
    :param target:
    :return:
    """
    try:
        shutil.copyfile(source, target)
    except Exception as e:
        logger.error("Could not copy file from \'%s\' to \'%s\'", source, target, e)


def move_file(source: str, target: str):
    """
    Copy a file from source to target
    :param source:
    :param target:
    :return:
    """
    try:
        shutil.move(source, target)
    except Exception as e:
        logger.error("Could not move file from \'%s\' to \'%s\'", source, target, e)


def check_files_and_compilation(compiler: str, compilation: bool, project_path: str):
    """
    Check all files have been rendered and launch a compilation if needed
    :param compiler:
    :param project_path:
    :param compilation:
    :return:
    """
    # TODO : Check all files have been rendered

    if compilation:
        # Launch a compilation
        if compiler == "maven":
            logger.info("Launching a mvn clean package : ")
            os.system("cd " + project_path + "; mvn clean package")
        elif compiler == "sbt":
            logger.info("Launching a sbt clean assembly : ")
            os.system("cd " + project_path + "; sbt clean assembly")

    logger.info("Check on files generated : completed")
