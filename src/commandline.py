import logging
import argparse

logger = logging.getLogger("project_generator")


def command_line_arguments_to_dict():
    """
    From command line, generate a dict with all arguments in it, and then return it
    :return: dict of command line arguments
    """
    parser = argparse.ArgumentParser(description='Generate a project in language, version, and compiler needed',
                                     epilog="This program is intent to facilitate developer's life for CDP."
                                            "It comes with no license or support, just Enjoy it ;)")
    # Required arguments
    parser.add_argument('--version', required=True, type=str,
                        choices=["7.1.1.0-565", "7.1.2.0-96", "7.1.3.0-100", "7.1.4.0-203"],
                        help="Version of CDP to use")
    parser.add_argument('--language', required=True, type=str,
                        choices=["scala", "java", "python"],
                        help="Programming Language to write code")
    parser.add_argument('--projectName', required=True, type=str,
                        help="Name of the project to create (ex : rocket-launcher)")
    parser.add_argument('--packageName', required=True, type=str,
                        help="Name of the package where your project will be located (ex: com.cloudera.frisch)")

    parser.add_argument('--compiler', type=str, choices=["maven", "sbt", "none"], default="maven",
                        help="Compiler to use to compile the project (maven by Default) " +
                             "- Not needed if python is the language")

    # Optional arguments
    parser.add_argument('--kerberos', type=bool, choices=[True, False], default=False,
                        help="Use of Kerberos or not (False by default)" +
                             "- If True, then following options must be filled : --principal and --keytab")
    parser.add_argument('--principal', type=str,
                        help="Kerberos principal")
    parser.add_argument('--keytab', type=str,
                        help="Kerberos keytab file associated with previous principal")
    parser.add_argument('--host', type=str, default="",
                        help="Host where Spark is deployed " +
                             "- It is used to prepare script submitting files")
    parser.add_argument('--user', type=str, default="",
                        help="User to access Host where Spark is deployed " +
                             "- It is used to prepare script submitting files")
    parser.add_argument('--test', type=bool, choices=[True, False], default=False,
                        help="Add test files and directories - (False by default)")
    parser.add_argument('--logger', type=bool, choices=[True, False], default=True,
                        help="Add logger to project or not - (True by default)")
    parser.add_argument('--compilation', type=bool, choices=[True, False], default=False,
                        help="Launch a compilation/packaging of the project after its creation - (False by default)")
    parser.add_argument('--docFiles', type=str, choices=["md", "adoc"], default="md",
                        help="Type of file to generate documentation files")
    parser.add_argument('--libs', type=str, choices=["typesafe", "lombok"], default=[], nargs='*',
                        help="To include other third party libraries")
    parser.add_argument('--fatjar', type=bool, choices=[True, False], default=True,
                        help="To generate a fat jar or not")

    # TODO : Add some command line optional arguments to handle all different types of clients
    parser.add_argument('--components', type=str, nargs='*',
                        choices=["none", "hbase", "hdfs", "hive", "kudu", "kafka", "ozone"],
                        default="none",
                        help="Components that program will interact with")
    parser.add_argument('--type', type=str,
                        choices=["normal", "spark", "kafkastream", "flink"],
                        default="normal",
                        help="Program type (normal by default)")

    # Related to specific components that will be used
    parser.add_argument('--spark-feature', type=str, nargs='*',
                        choices=["core", "sql", "structured_streaming", "streaming"],
                        default="core", help="Spark Features to add to the project")
    parser.add_argument('--hdfsNameservice', type=str, default="hdfs",
                        help="Nameservice of the HDFS")
    parser.add_argument('--hdfsWorkDir', type=str, default="/tmp",
                        help="HDFS work directory setup in configuration files")

    args = parser.parse_args()

    return args.__dict__


def check_command_lines(dict_of_options: dict):

    # TODO : Implements these rules to check command-line arguments

    # Rule #1 : If language is java, compiler could not be sbt

    # Rule #2 : If language is python, compiler must be none

    # Rule #3 : projectName should have only alphanumerical values with '-' and '_'

    # Rule #4 : packageName should have only alphanumerical values with '-' and '_' and '.'

    # Rule #5 : If sendFiles is True, then compilation must be set to true also except if language is python

    # Rule #6 : Emit a WARN if compilation is set to true and language is python

    # Rule #7 : You could not have streaming and structured streaming in the same project

    # Rule #8 : if Kerberos is True, then principal and keytab must be filled

    # Rule #9 : If principal and/or keytab are filled but Kerberos is false => Emit a WARN, as they wont be used

    # Rule #10 : If --sendFiles is true, --host and --user must be filled in

    # Rule #11 : If another type than normal is chosen, no components can be added

    # Rule #12 :

    logger.info("Check on arguments passed made")
