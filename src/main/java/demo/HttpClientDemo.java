package demo;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;

import java.io.IOException;
import java.util.Arrays;

public class HttpClientDemo {

    public static void main(String[] args) throws IOException {

        configureSystemProperties();

        CloseableHttpClient httpClient = HttpClientBuilder.create().useSystemProperties().build();
        ClassicHttpRequest request = new HttpGet("https://www.google.com");
        httpClient.execute(request, response -> {
            // MIME TYPE
            String contentMIMEType = ContentType.parse(response.getEntity().getContentType()).getMimeType();
            System.out.println(contentMIMEType);

            // Body

            return response;
        });
    }

    public static void configureSystemProperties() {
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "7897");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "7897");
    }
}
