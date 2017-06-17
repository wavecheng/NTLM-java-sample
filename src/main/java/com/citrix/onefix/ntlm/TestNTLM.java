package com.citrix.onefix.ntlm;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TestNTLM {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		Registry<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create()
		        .register(AuthSchemes.NTLM, new JCIFSNTLMSchemeFactory())
		        .register(AuthSchemes.BASIC, new BasicSchemeFactory())
		        .register(AuthSchemes.DIGEST, new DigestSchemeFactory())
		        .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())
		        .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory())
		        .build();

		
		NTCredentials credentials = new NTCredentials("boch", "****", null, "citrite");
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, credentials);

		CloseableHttpClient httpClient = HttpClients.custom()
		        .setDefaultAuthSchemeRegistry(authSchemeRegistry)
		        .setDefaultCredentialsProvider(credentialsProvider)
		        .build();

		HttpUriRequest req = new HttpGet("http://www.bing.com");
		
		
		CloseableHttpResponse rep = httpClient.execute(req);
		String result = EntityUtils.toString(rep.getEntity()); 		
		System.out.println(result);
		
		Document doc  = Jsoup.parse(result);
		System.out.println(doc.title());
		
	}

}
