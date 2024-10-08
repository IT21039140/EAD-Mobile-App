package com.example.eadapp.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class RetrofitClient {

    private static final String BASE_URL = "https://10.0.2.2:7207"; // Your API URL

    public static Retrofit getClient() {

        try {
            // Trust manager that trusts all certificates for development purposes
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            // Set up SSL context with the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // OkHttp client with custom SSL setup
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;  // Allow all hostnames for testing purposes
                        }
                    })
                    .build();

            // Build the Retrofit instance
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    // Add ScalarsConverterFactory for plain text responses
                    .addConverterFactory(ScalarsConverterFactory.create())
                    // Add GsonConverterFactory for JSON responses
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
