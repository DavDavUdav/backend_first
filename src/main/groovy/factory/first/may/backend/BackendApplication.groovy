package factory.first.may.backend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate

@SpringBootApplication
class BackendApplication {

    // создание и настройка trust manager'а
    static X509TrustManager trustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };


    static void main(String[] args) {
        // создание и настройка ssl context'а
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());

// применение ssl context к клиенту
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

// отключение проверки имени хоста (hostname verification)
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
        SpringApplication.run(BackendApplication, args)
        println 'Server started'
    }
}