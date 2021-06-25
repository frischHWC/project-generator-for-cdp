package {{ package_name }}.client;

import {{ package_name }}.config.AppConfig;
import {{ package_name }}.config.Utils;
{% if logger is sameas true %}import org.apache.log4j.Logger;{% endif %}
import org.apache.hadoop.conf.Configuration;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.client.solrj.impl.BaseHttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.Krb5HttpClientBuilder;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


public class SolRClient {

  {% if logger is sameas true %}private static Logger logger = Logger.getLogger(SolRClient.class);{% endif %}
  String collection = "test_collection";
  Integer replicas = 1;
  Integer shards = 1;
  private HttpSolrClient httpSolrClient;


  public SolRClient() {

    String protocol = "http";

  if(Boolean.parseBoolean(AppConfig.getProperty("tls.auth"))) {
            System.setProperty("javax.net.ssl.trustStore", AppConfig.getProperty("truststore.location"));
            System.setProperty("javax.net.ssl.trustStorePassword", AppConfig.getProperty("truststore.password"));
            protocol = "https";
        }

     HttpSolrClient.Builder solrClientBuilder = new HttpSolrClient.Builder(protocol + "://" +
                AppConfig.getProperty("solr.server") + ":" +
                AppConfig.getProperty("solr.port") + "/solr")
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000);

        if (Boolean.parseBoolean(AppConfig.getProperty("kerberos.auth"))) {
            Utils.createJaasConfigFile("solr-jaas-randomdatagen.config", "SolrJClient",
                    AppConfig.getProperty("kerberos.keytab"),
                    AppConfig.getProperty("kerberos.user"),
                    true, null, false);
            System.setProperty("java.security.auth.login.config", "solr-jaas-randomdatagen.config");
            System.setProperty("solr.kerberos.jaas.appname", "SolrJClient");

            try(Krb5HttpClientBuilder krb5HttpClientBuilder = new Krb5HttpClientBuilder()) {
                HttpClientUtil.setHttpClientBuilder(krb5HttpClientBuilder.getHttpClientBuilder(java.util.Optional.empty()));
            } catch (Exception e) {
                logger.warn("Could set Kerberos for HTTP client due to error:", e);
            }
         }

       httpSolrClient = solrClientBuilder.build();

       try {
            logger.debug("Creating collection : " + collection + " in SolR");
            httpSolrClient.request(
                    CollectionAdminRequest.createCollection(collection, shards, replicas)
            );
            logger.debug("Finished to create collection : " + collection + " in SolR");
        } catch (BaseHttpSolrClient.RemoteSolrException e) {
            if (e.getMessage().contains("collection already exists")) {
                logger.warn("Collection already exists so it has not been created");
            } else {
                logger.error("Could not create SolR collection : " + collection + " due to error: ", e);
            }
        } catch (Exception e) {
            logger.error("Could not create SolR collection : " + collection + " due to error: ", e);
        }

       // Set base URL directly to the collection, note that this is required
        httpSolrClient.setBaseURL(protocol + "://" + AppConfig.getProperty("solr.server") + ":" +
                AppConfig.getProperty("solr.port") + "/solr/" + collection);

  }


  public void write(String toWrite) {

    try {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("test", toWrite);

        httpSolrClient.add(doc);

        httpSolrClient.commit();
    } catch (Exception e) {
            logger.error("An unexpected error occurred while adding documents to SolR collection : " +
                    collection + " due to error:", e);
    }

  }

  public void read(String toRead) {
    try {
        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("q", "*:*");
        MapSolrParams queryParams = new MapSolrParams(queryParamMap);

        QueryResponse response = httpSolrClient.query(collection, queryParams);
        SolrDocumentList documents = response.getResults();

        logger.info("Found " + documents.getNumFound() + " documents");
        for(SolrDocument document : documents) {
            String row = (String) document.getFirstValue("test");
            logger.info("value read: " + row);
        }

        httpSolrClient.close();
    } catch (Exception e) {
            logger.error("An unexpected error occurred while reading documents from SolR collection : " +
                    collection + " due to error:", e);
    }
  }


}