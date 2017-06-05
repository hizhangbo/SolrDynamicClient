package service.solrclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.dom4j.Document;
import org.dom4j.Element;

import container.file.FileHelper;
import container.xml.XmlHelper;

public class SolrDynamicClient implements ISolrClient {

    private String Solr_Host;

    private String Solr_Core;

    private String USER;

    private String PASSWORD;

    protected SolrClient solrClient;
    
    protected Map<String, Class<?>> coreProperties;
    
    public SolrDynamicClient(String core){
        SetSolrHost();
        SetSolrCore(core);
        InitSolrClient(Solr_Host, Solr_Core);
        CorePropertiesConstruct();
    }

	@Override
	public void SetSolrHost() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config/solr.properties");
        Properties p = new Properties();
        try {
            p.load(inputStream);
            Solr_Host = p.getProperty("solr.host");
            USER = p.getProperty("solr.user");
            PASSWORD = p.getProperty("solr.password");	
        } catch (IOException e1) {
            e1.printStackTrace();
        }        	
	}

	@Override
	public void SetSolrCore(String core) {
		Solr_Core = core;
	}

	@Override
	public void InitSolrClient(String host, String core) {
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(USER, PASSWORD);

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                AuthScope.ANY,
                creds);

        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.addInterceptorFirst(new PreemptiveAuthInterceptor());
        builder.setDefaultCredentialsProvider(credsProvider);
        CloseableHttpClient httpClient = builder.build();

        solrClient = new HttpSolrClient.Builder(host + '/' + core).withHttpClient(httpClient).build();
	}
	
    static class PreemptiveAuthInterceptor implements HttpRequestInterceptor {

        public void process(HttpRequest httpRequest, org.apache.http.protocol.HttpContext httpContext) throws HttpException, IOException {
            AuthState authState = (AuthState) httpContext.getAttribute(HttpClientContext.TARGET_AUTH_STATE);
            // If no auth scheme available yet, try to initialize it
            // preemptively
            if (authState.getAuthScheme() == null) {
                CredentialsProvider credsProvider = (CredentialsProvider)
                        httpContext.getAttribute(HttpClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) httpContext.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
                AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
                Credentials creds = credsProvider.getCredentials(authScope);
                if(creds == null){

                }
                authState.update(new BasicScheme(), creds);
            }
        }
    }

    private void CorePropertiesConstruct(){
    	XmlHelper xmlHelper = new XmlHelper();
    	File file = xmlHelper.getConfig(Solr_Core);

    	try{
            Document document = xmlHelper.parse(file);
            List<Element> fields = xmlHelper.getNodesByName(document, "field");
            List<Element> field_types = xmlHelper.getNodesByName(document, "fieldType");
            
            this.coreProperties = xmlHelper.getProperties(fields, field_types);
    		
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
}
