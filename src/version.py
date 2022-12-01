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

logger = logging.getLogger("project_generator")


def get_versions(cdp_version: str):
    """
    Gives associated versions of components for a specified CDP version
    :param cdp_version:
    :return:
    """
    versions = \
        {'7.1.1.0':
             {'hdfs': '3.1.1',
              'hbase': '2.2.3',
              'hive': '3.1.3000',
              'flink': '',
              'kafka': '2.4.1',
              'kudu': '1.10.0',
              'ozone': '0.5.0',
              'solr': '8.4.1',
              'spark': '',
              'build_version': '565'},
         '7.1.2.0':
             {'hdfs': '3.1.1',
              'hbase': '2.2.3',
              'hive': '3.1.3000',
              'flink': '',
              'kafka': '2.4.1',
              'kudu': '1.10.0',
              'ozone': '0.5.0',
              'solr': '8.4.1',
              'spark': '',
              'build_version': '96'},
         '7.1.3.0':
             {'hdfs': '3.1.1',
              'hbase': '2.2.3',
              'hive': '3.1.3000',
              'flink': '',
              'kafka': '2.4.1',
              'kudu': '1.10.0',
              'ozone': '0.5.0',
              'solr': '8.4.1',
              'spark': '',
              'build_version': '100'},
         '7.1.4.0':
             {'hdfs': '3.1.1',
              'hbase': '2.2.3',
              'hive': '3.1.3000',
              'flink': '',
              'kafka': '2.4.1',
              'kudu': '1.12.0',
              'ozone': '0.5.0',
              'solr': '8.4.1',
              'spark': '',
              'build_version': '203'},
         '7.1.5.0':
             {'hdfs': '3.1.1',
              'hbase': '2.2.3',
              'hive': '3.1.3000',
              'flink': '',
              'kafka': '2.4.1',
              'kudu': '1.13.0',
              'ozone': '1.0.0',
              'solr': '8.4.1',
              'spark': '',
              'build_version': '257'},
         '7.1.6.0':
             {'hdfs': '3.1.1',
              'hbase': '2.2.3',
              'hive': '3.1.3000',
              'flink': '',
              'kafka': '2.5.0',
              'kudu': '1.13.0',
              'ozone': '1.0.0',
              'solr': '8.4.1',
              'spark': '',
              'build_version': '297'},
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

