package {{ package_name }}.config;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
{% if logger is sameas true %}import org.apache.log4j.Logger;{% endif %}

import {{ package_name }}.config.AppConfig;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class Utils {

    private Utils() { throw new IllegalStateException("Could not initialize this class"); }

    {% if logger is sameas true %}private static final Logger logger = Logger.getLogger(Utils.class);{% endif %}

    /**
     * Login to kerberos using a given user and its associated keytab
     * @param kerberosUser is the kerberos user
     * @param pathToKeytab path to the keytab associated with the user, note that unix read-right are needed to access it
     * @param config hadoop configuration used further
     */
    public static void loginUserWithKerberos(String kerberosUser, String pathToKeytab, Configuration config) {
        if(config != null) {
            config.set("hadoop.security.authentication", "kerberos");
            UserGroupInformation.setConfiguration(config);
        }
            try {
                UserGroupInformation.loginUserFromKeytab(kerberosUser, pathToKeytab);
            } catch (IOException e) {
                logger.error("Could not load keytab file",e);
            }

    }

    /**
     * Setup haddop env by setting up needed Hadoop system property and adding to configuration required files
     * @param config Hadoop configuration to set up
     */
    public static void setupHadoopEnv(Configuration config) {

        {% if "hdfs" is in components %}config.addResource(new Path("file://"+ AppConfig.getProperty("hadoop.core.site.path")));
        config.addResource(new Path("file://"+ AppConfig.getProperty("hadoop.hdfs.site.path")));{% endif %}
        {% if "ozone" is in components %}config.addResource(new Path("file://"+ AppConfig.getProperty("hadoop.ozone.site.path")));{% endif %}
        {% if "hbase" is in components %}config.addResource(new Path("file://"+ AppConfig.getProperty("hadoop.hbase.site.path")));{% endif %}

        System.setProperty("HADOOP_USER_NAME", AppConfig.getProperty("hadoop.user"));
        System.setProperty("hadoop.home.dir", AppConfig.getProperty("hadoop.home"));
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
    public static void createJaasConfigFile(String fileName, String clientName, String keytabPath, String principal, Boolean useKeytab, Boolean storeKey, Boolean appendToFile) {
        try(Writer fileWriter = new FileWriter(fileName, appendToFile)) {
            if(Boolean.TRUE.equals(appendToFile)) { fileWriter.append(System.getProperty("line.separator")); }
            fileWriter.append(clientName);
            fileWriter.append(" { ");
            fileWriter.append(System.getProperty("line.separator"));
            fileWriter.append("com.sun.security.auth.module.Krb5LoginModule required");
            fileWriter.append(System.getProperty("line.separator"));
            if(useKeytab!=null) {
                fileWriter.append("useKeyTab=");
                fileWriter.append(useKeytab.toString());
                fileWriter.append(System.getProperty("line.separator"));
            }
            if(storeKey!=null) {
                fileWriter.append("storeKey=");
                fileWriter.append(storeKey.toString());
                fileWriter.append(System.getProperty("line.separator"));
            }
            fileWriter.append("keyTab=\"");
            fileWriter.append(keytabPath);
            fileWriter.append("\"");
            fileWriter.append(System.getProperty("line.separator"));
            fileWriter.append("principal=\"");
            fileWriter.append(principal);
            fileWriter.append("\";");
            fileWriter.append(System.getProperty("line.separator"));
            fileWriter.append("};");
            fileWriter.flush();
        } catch (IOException e) {
            logger.error("Could not write proper JAAS config file : " + fileName + " due to error : ", e);
        }
    }

}