package {{ package_name }}.config


import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.security.UserGroupInformation
{% if logger is sameas true %}import org.apache.logging.log4j.scala.Logging{% endif %}

import java.io.FileWriter


object Utils {% if logger is sameas true %}extends Logging{% endif %} {

    /**
     * Login to kerberos using a given user and its associated keytab
     * @param kerberosUser is the kerberos user
     * @param pathToKeytab path to the keytab associated with the user, note that unix read-right are needed to access it
     * @param config hadoop configuration used further
     */
    def loginUserWithKerberos(kerberosUser: String, pathToKeytab: String, config: Configuration): Unit =  {
        if(config != null) {
            config.set("hadoop.security.authentication", "kerberos")
            UserGroupInformation.setConfiguration(config)
        }
            try {
                UserGroupInformation.loginUserFromKeytab(kerberosUser, pathToKeytab)
            } catch {
                case e: Exception => {% if logger is sameas true %}logger.error("Could not load keytab file", e){% endif %}
            }

    }


    /**
     * Setup hadoop env by setting up needed Hadoop system property and adding to configuration required files
     * @param config Hadoop configuration to set up
     */
    def setupHadoopEnv(config: Configuration)  {

        {% if "hdfs" is in components or "ozone" is in components %}config.addResource(new Path("file://" + AppConfig.coreSitePath))
        config.addResource(new Path("file://" + AppConfig.hdfsSitePath)){% endif %}
        {% if "ozone" is in components %}config.addResource(new Path("file://"+ AppConfig.ozoneSitePath)){% endif %}
        {% if "hbase" is in components %}config.addResource(new Path("file://"+ AppConfig.hbaseSitePath)){% endif %}

        System.setProperty("HADOOP_USER_NAME", AppConfig.hadoopUser)
        System.setProperty("hadoop.home.dir", AppConfig.hadoopHome)
    }


 /**
     * Write an JAAS config file that will be used by the application
     * Note that it overrides any existing files and its content
     * @param fileName File path + nam of jaas config file that will be created
     * @param clientName that will represent the client in the JAAS config file
     * @param keytabPath and name of the keytab to put on the file
     * @param principal in the form of principal@REALM as a string
     * @param useKeytab true/false or null if must not be set in the JAAS file
     * @param storeKey true/false or null if must not be set in the JAAS file
     */
    def createJaasConfigFile(fileName: String, clientName: String, keytabPath: String, principal: String, useKeytab: Boolean, storeKey: Boolean, appendToFile: Boolean) {

        try {
            val fileWriter = new FileWriter(fileName, appendToFile)
            if(appendToFile) { fileWriter.append(System.getProperty("line.separator")) }
            fileWriter.append(clientName)
            fileWriter.append(" { ")
            fileWriter.append(System.getProperty("line.separator"))
            fileWriter.append("com.sun.security.auth.module.Krb5LoginModule required")
            fileWriter.append(System.getProperty("line.separator"))
            if(useKeytab) {
                fileWriter.append("useKeyTab=")
                fileWriter.append(useKeytab.toString())
                fileWriter.append(System.getProperty("line.separator"))
            }
            if(storeKey) {
                fileWriter.append("storeKey=")
                fileWriter.append(storeKey.toString())
                fileWriter.append(System.getProperty("line.separator"))
            }
            fileWriter.append("keyTab=\"")
            fileWriter.append(keytabPath)
            fileWriter.append("\"")
            fileWriter.append(System.getProperty("line.separator"))
            fileWriter.append("principal=\"")
            fileWriter.append(principal)
            fileWriter.append("\"")
            fileWriter.append(System.getProperty("line.separator"))
            fileWriter.append("}")
            fileWriter.flush()
        } catch {
            case e: Exception => {% if logger is sameas true %}logger.error("Could not write proper JAAS config file : " + fileName + " due to error : ", e){% endif %}
        }
    }

}