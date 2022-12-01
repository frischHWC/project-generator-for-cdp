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
import os
from src.utils import create_folder, clean_directory, copy_file, move_file

logger = logging.getLogger("project_generator")
generated_files_path = "/tmp/generated_files"
final_path = "../"


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
    folder_path = final_path + project_name + "/"
    package_path = package_name.replace(".", "/") + "/"

    # Remove older folder if it exists
    clean_directory(folder_path)

    # Create main folder
    create_folder(folder_path)

    # Create and order resources files folder
    if language == "scala" or language == "java":
        create_folder(folder_path + "src/main/resources/")
        if language == "scala" or "typesafe" in libs:
            copy_file(generated_files_path + "/application.conf", folder_path + "src/main/resources/application.conf")
        else:
            copy_file(generated_files_path + "/application.properties", folder_path + "src/main/resources/application.properties")
        if logger_needed:
            copy_file(generated_files_path + "/log4j.properties", folder_path + "src/main/resources/log4j.properties")
            copy_file(generated_files_path + "/log4j2.properties", folder_path + "src/main/resources/log4j2.properties")
    elif language == "python":
        create_folder(folder_path + "resources/")

    # Create and order scripts files folder
    if language == "scala" or language == "java":
        create_folder(folder_path + "src/main/resources/scripts/")
        copy_file(generated_files_path + "/launch.sh", folder_path + "src/main/resources/scripts/launch.sh")
        copy_file(generated_files_path + "/launchFromIDE.sh", folder_path + "src/main/resources/scripts/launchFromIDE.sh")
        copy_file(generated_files_path + "/launchToCDP.sh", folder_path + "src/main/resources/scripts/launchToCDP.sh")
        if "spark" in libs:
            copy_file(generated_files_path + "/spark-submit.sh", folder_path + "src/main/resources/scripts/spark-submit.sh")
    elif language == "python":
        create_folder(folder_path + "resources/scripts/")
        copy_file(generated_files_path + "/launch.sh", folder_path + "resources/scripts/launch.sh")
        copy_file(generated_files_path + "/launchFromIDE.sh", folder_path + "resources/scripts/launchFromIDE.sh")
        copy_file(generated_files_path + "/launchToCDP.sh", folder_path + "resources/scripts/launchToCDP.sh")
        if "spark" in libs:
            copy_file(generated_files_path + "/spark-submit.sh", folder_path + "resources/scripts/spark-submit.sh")

    # Create and order code files folder
    if language == "scala" or language == "java":
        create_folder(folder_path + "src/main/" + language + "/" + package_path)
        create_folder(folder_path + "src/main/" + language + "/" + package_path + "config")
        copy_file(generated_files_path + "/App." + language,
                  folder_path + "src/main/" + language + "/" + package_path + "/App." + language)
        copy_file(generated_files_path + "/Treatment." + language,
                  folder_path + "src/main/" + language + "/" + package_path + "/Treatment." + language)
        copy_file(generated_files_path + "/AppConfig." + language,
                  folder_path + "src/main/" + language + "/" + package_path + "config/AppConfig." + language)
        copy_file(generated_files_path + "/Utils." + language,
                  folder_path + "src/main/" + language + "/" + package_path + "config/Utils." + language)
        # Copy client files
        client_files = [f for f in os.listdir(generated_files_path + "/") if "Client" in f]
        if client_files.__len__() != 0:
            create_folder(folder_path + "src/main/" + language + "/" + package_path + "client/")
        for file in client_files:
            copy_file(generated_files_path + "/" + file, folder_path + "src/main/" + language + "/" + package_path
                      + "client/" + file)
    elif language == "python":
        create_folder(folder_path + "src/")
        files = [f for f in os.listdir(generated_files_path + "/") if ".py" in f and "test" not in f]
        for file in files:
            copy_file(generated_files_path + "/" + file, folder_path + "src/" + file)

    # Create and order test files folder
    if language == "scala" or language == "java":
        create_folder(folder_path + "src/test/" + language)
        files = [f for f in os.listdir(generated_files_path + "/") if "." + language in f and "test" in f]
        for file in files:
            copy_file(generated_files_path + "/" + file, folder_path + "src/test/" + language + "/" + package_path + file)
    elif language == "python":
        create_folder(folder_path + "test/")
        files = [f for f in os.listdir(generated_files_path + "/") if ".py" in f and "test" in f]
        for file in files:
            copy_file(generated_files_path + "/" + file, folder_path + "test/" + file)

    # Put doc files
    files = [f for f in os.listdir(generated_files_path + "/")
             if ".md" in f or ".adoc" in f]
    for file in files:
        copy_file(generated_files_path + "/" + file, folder_path + file)

    # Arrange sbt compiler files
    if compiler == "maven":
        copy_file(generated_files_path + "/pom.xml", folder_path + "pom.xml")
    elif compiler == "sbt":
        create_folder(folder_path + "project/")
        copy_file(generated_files_path + "/build.sbt", folder_path + "build.sbt")
        copy_file(generated_files_path + "/build.properties", folder_path + "project/build.properties")
        copy_file(generated_files_path + "/assembly.sbt", folder_path + "project/assembly.sbt")
        copy_file(generated_files_path + "/plugins.sbt", folder_path + "project/plugins.sbt")

    # Make .sh files executable
    sh_files = [f for f in os.listdir(folder_path) if f.endswith(".sh")]
    for file in sh_files:
        os.chmod(os.path.join(folder_path, file), 0o755)

    logger.info("Finished to order files for language : %s and compiler : %s", language, compiler)
