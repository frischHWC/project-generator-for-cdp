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
                      hdfs_nameservice: str,
                      hdfs_work_dir: str,
                      components,
                      program_type: str,
                      project_name: str):
    """
        Generate code files according to language and features needed
    :param env:
    :param language:
    :param spark_feature:
    :param logger_needed:
    :param package_name:
    :param user:
    :param hdfs_nameservice:
    :param hdfs_work_dir:
    :param components:
    :param program_type:
    :param project_name:
    :return:
    """
    if language == "python":
        language_extension = "py"
    else:
        language_extension = language

    # TODO : Add all components and their features here
    # TODO: Add different config file, one for each component and one for each client

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
            env.get_template("code/" + language + "/AppConfig." + language_extension)
            .render(spark_feature=spark_feature,
                    package_name=package_name,
                    user=user,
                    hdfsNameservice=hdfs_nameservice,
                    hdfsWorkDir=hdfs_work_dir,
                    components=components,
                    program_type=program_type,
                    project_name=project_name),
            generated_files_path + "/AppConfig." + language_extension)

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
                          program_type: str):
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
    # TODO: Add dependencies foreach component in compiler files
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
                    type=program_type),
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
                    package_name=package_name),
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
                 "and feature : %s", compiler, version, spark_feature)


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
                               hdfs_nameservice: str,
                               logger_needed: bool,
                               hdfs_work_dir: str,
                               components,
                               libs,
                               program_type: str):
    """
        Generate configuration file (for logging and external variables)
    :param env:
    :param language:
    :param spark_feature:
    :param project_name:
    :param user:
    :param hdfs_nameservice:
    :param logger_needed:
    :param hdfs_work_dir:
    :param components:
    :param libs:
    :param program_type:
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

    # TODO : Add other components configuration here
    # TODO: Add other configuration for other components in args of function to render proper conf file
    if language == "scala" or "typesafe" in libs:

        write_template_to_file(
            env.get_template("configuration/application.conf")
            .render(spark_feature=spark_feature,
                    project_name=project_name,
                    user=user,
                    hdfsNameservice=hdfs_nameservice,
                    hdfsWorkDir=hdfs_work_dir,
                    components=components),
            generated_files_path + "/application.conf")

    elif language == "java":

        write_template_to_file(
            env.get_template("configuration/application.properties")
            .render(spark_feature=spark_feature,
                    project_name=project_name,
                    user=user,
                    hdfsNameservice=hdfs_nameservice,
                    hdfsWorkDir=hdfs_work_dir,
                    components=components),
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
