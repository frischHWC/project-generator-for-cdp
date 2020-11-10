import logging

logger = logging.getLogger("project_generator")


def get_versions(cdp_version: str):
    """
    Gives associated versions of components for a specified CDP version
    :param cdp_version:
    :return:
    """
    versions = \
        {'7.1.1.0':
             {'hdfs': '',
              'hbase': '',
              'hive': '',
              'flink': '',
              'kafka': '',
              'kudu': '',
              'ozone': '',
              'solr': '',
              'spark': '',
              'build_version': '565'},
         '7.1.2.0':
             {'hdfs': '',
              'hbase': '',
              'hive': '',
              'flink': '',
              'kafka': '',
              'kudu': '',
              'ozone': '',
              'solr': '',
              'spark': '',
              'build_version': '96'},
         '7.1.3.0':
             {'hdfs': '',
              'hbase': '',
              'hive': '',
              'flink': '',
              'kafka': '',
              'kudu': '',
              'ozone': '',
              'solr': '',
              'spark': '',
              'build_version': '100'},
         '7.1.4.0':
             {'hdfs': '',
              'hbase': '',
              'hive': '',
              'flink': '',
              'kafka': '',
              'kudu': '',
              'ozone': '',
              'solr': '',
              'spark': '',
              'build_version': '203'},
         '7.1.5.0':
             {'hdfs': '',
              'hbase': '',
              'hive': '',
              'flink': '',
              'kafka': '',
              'kudu': '',
              'ozone': '',
              'solr': '',
              'spark': '',
              'build_version': ''},
        }

    return versions.get(cdp_version)


def is_above(version1: str, version2: str):
    """
    Compares two version (supposedly of a X.Y.Z form, with an optional .W at the end)
    :param version1:
    :param version2:
    :return: true if first parameter is above second one, else False
    """
    if version1.__len__() == 5:
        version1 += ".0"
    if version2.__len__() == 5:
        version2 += ".0"

    if version1.__len__() == 7 and version2.__len__() == 7:
        version1.replace(".", "")
        version2.replace(".", "")
        for i in range(0,  3):
            if int(version1[i]) > int(version2[i]):
                return True
    else:
        logger.warning("These two versions are not versions: {} and {}",
            version1, version2)

    return False


def is_below(version1: str, version2: str):
    """
    Compares two version (supposedly of a X.Y.Z form, with an optional .W at the end)
    :param version1:
    :param version2:
    :return: true if first parameter is below second one, else False
    """
    if version1.__len__() == 5:
        version1 += ".0"
    if version2.__len__() == 5:
        version2 += ".0"

    if version1.__len__() == 7 and version2.__len__() == 7:
        version1.replace(".", "")
        version2.replace(".", "")
        for i in range(0,  3):
            if int(version1[i]) < int(version2[i]):
                return True
    else:
        logger.warning("These two versions are not versions: {} and {}",
            version1, version2)

    return False

