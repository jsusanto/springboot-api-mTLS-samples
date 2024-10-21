# springboot-api-mTLS-samples
# Starting with Spring Initializr
1. Navigate to https://start.spring.io. This service pulls in all the dependencies you need for an application and does most of the setup for you.

2. Choose either Gradle or Maven and the language you want to use. This guide assumes that you chose Java.

3. Click Dependencies and select Spring Web.

4. Click Generate.

5. Download the resulting ZIP file, which is an archive of a web application that is configured with your choices.

![image](https://github.com/user-attachments/assets/32ebd709-5d0c-4ccb-870e-ea5433abf6b7)

# IDE - Intellij IDEA 2024.2.2 (Community Edition)
![image](https://github.com/user-attachments/assets/6e747fda-cb38-46af-955e-1a740d719960)

# Understanding Certificates and Keys
- CA (Certificate Authority) - An entity that issues digital certificates.
- Certificate - A digital form of identification, like a passport, for your application.
- Private Key - A secret key that is used in conjunction with a public certificate to encrypt and decrypt data.
- CSR (Certificate Signing Request) - A request sent from an applicant to a CA to obtain a digital identity certificate.
- Truststore - A repository that holds trusted certificates (usually CA certificates).
- Keystore - A repository that holds certificates along with their private keys.

Note: On Windows, we're using Git Bash.

# Generating Certificates and Keys
<h2>Step 1: Generate the CA Certificate</h2>
<h3>1.1: Create the CA’s Private Key</h3>
<pre>
  openssl genrsa -out ca.key 2048
</pre>

/springboot-api-mTLS-samples/src/main/resources/keystore (main) $ openssl genrsa -out ca.key 2048
Generating RSA private key, 2048 bit long modulus (2 primes)
.....................................................+++++
....................................................+++++
e is 65537 (0x010001)

<h3>1.2: Self-Sign and Create the CA Certificate:</h3>
<pre>
  openssl req -x509 -new -nodes -key ca.key -sha256 -days 365 -out ca.pem
</pre>

Output
<pre>
/springboot-api-mTLS-samples/src/main/resources/keystore (main) $ openssl req -x509 -new -nodes -key ca.key -sha256 -days 365 -out ca.pem
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.

Country Name (2 letter code) [AU]:
State or Province Name (full name) [Some-State]:
Locality Name (eg, city) []:
Organization Name (eg, company) [Internet Widgits Pty Ltd]:Medibank Limited
Organizational Unit Name (eg, section) []:
Common Name (e.g. server FQDN or YOUR name) []:Medibank Limited
Email Address []:
</pre>

<h2>Step 2: Generate Server and Client Certificates</h2>
<h3>2.1: Generate Private Keys:</h3>
For the server: openssl genrsa -out server.key 2048
<pre>
  openssl genrsa -out server.key 2048
</pre>

Output
<pre>
/springboot-api-mTLS-samples/src/main/resources/keystore (main) $ openssl genrsa -out server.key 2048
Generating RSA private key, 2048 bit long modulus (2 primes)
..........+++++
.............+++++
e is 65537 (0x010001)
</pre>

For the client: openssl genrsa -out client.key 2048

<pre>
  openssl genrsa -out client.key 2048
</pre>

Output
<pre>
/springboot-api-mTLS-samples/src/main/resources/keystore (main) $ openssl genrsa -out client.key 2048
Generating RSA private key, 2048 bit long modulus (2 primes)
................................................................+++++
.......................+++++
e is 65537 (0x010001)
</pre>

<h3>2.2: Generate CSRs:</h3>
For the server: openssl req -new -key server.key -out server.csr
<pre>
  openssl req -new -key server.key -out server.csr
</pre>

Output
<pre>
  /springboot-api-mTLS-samples/src/main/resources/keystore (main) $ openssl req -new -key server.key -out server.csr
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:
State or Province Name (full name) [Some-State]:
Locality Name (eg, city) []:
Organization Name (eg, company) [Internet Widgits Pty Ltd]:Medibank Limited
Organizational Unit Name (eg, section) []:
Common Name (e.g. server FQDN or YOUR name) []:Medibank Limited
Email Address []:

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:
An optional company name []:
</pre>

For the client: openssl req -new -key client.key -out client.csr
<pre>
  openssl req -new -key client.key -out client.csr
</pre>
Output
<pre>
  /springboot-api-mTLS-samples/src/main/resources/keystore (main) $ openssl req -new -key client.key -out client.csr
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:
State or Province Name (full name) [Some-State]:
Locality Name (eg, city) []:
Organization Name (eg, company) [Internet Widgits Pty Ltd]:Medibank Limited
Organizational Unit Name (eg, section) []:
Common Name (e.g. server FQDN or YOUR name) []:Medibank Limited
Email Address []:

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:
An optional company name []:
</pre>

<h3>2.3: Sign CSRs with the CA Key:</h3>
For the server: openssl x509 -req -in server.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out server.crt -days 365
<pre>
  openssl x509 -req -in server.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out server.crt -days 365
</pre>

Output
<pre>
  /springboot-api-mTLS-samples/src/main/resources/keystore (main) $ openssl x509 -req -in server.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out server.crt -days 365
Signature ok
subject=C = AU, ST = Some-State, O = Medibank Limited, CN = Medibank Limited
Getting CA Private Key
</pre>

For the client: openssl x509 -req -in client.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out client.crt -days 365
<pre>
  openssl x509 -req -in client.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out client.crt -days 365
</pre>

Output
<pre>
  /springboot-api-mTLS-samples/src/main/resources/keystore (main) $ openssl x509 -req -in client.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out client.crt -days 365
Signature ok
subject=C = AU, ST = Some-State, O = Medibank Limited, CN = Medibank Limited
Getting CA Private Key
</pre>

<h2>Step 3: Create PKCS#12 Keystores</h2>
<h3>3.1 Convert Certificates and Keys to PKCS#12 Format:</h3>
Note: Please run this command using Command Prompt instead of Git Bash.

<pre>
\springboot-api-mTLS-samples\src\main\resources\keystore>set OPENSSL_CONF=C:\Program Files\Git\usr\ssl\openssl.cnf
</pre>

For the server: openssl pkcs12 -export -out server.p12 -name "server" -inkey server.key -in server.crt -certfile ca.pem
<pre>
  openssl pkcs12 -export -out server.p12 -name "server" -inkey server.key -in server.crt -certfile ca.pem
</pre>

Output:
<pre>
  \springboot-api-mTLS-samples\src\main\resources\keystore>openssl pkcs12 -export -out server.p12 -name "server" -inkey server.key -in server.crt -certfile ca.pem
Loading 'screen' into random state - done
Enter Export Password:
Verifying - Enter Export Password:
</pre>
For the client: openssl pkcs12 -export -out client.p12 -name "client" -inkey client.key -in client.crt -certfile ca.pem
<pre>
  \springboot-api-mTLS-samples\src\main\resources\keystore>openssl pkcs12 -export -out client.p12 -name "client" -inkey client.key -in client.crt -certfile ca.pem
Loading 'screen' into random state - done
Enter Export Password:
Verifying - Enter Export Password:
</pre>

<h2>Step 4: Create the Truststore</h2>
<h3>4.1 Import the CA Certificate into a PKCS#12 Truststore:</h3>

<pre>
  keytool -import -file ca.pem -alias "ca" -keystore truststore.p12 -storetype PKCS12
</pre>

Output:
<pre>
  \springboot-api-mTLS-samples\src\main\resources\keystore>keytool -import -file ca.pem -alias "ca" -keystore truststore.p12 -storetype PKCS12
Enter keystore password:
Re-enter new password:
Owner: CN=Medibank Limited, O=Medibank Limited, ST=Some-State, C=AU
Issuer: CN=Medibank Limited, O=Medibank Limited, ST=Some-State, C=AU
Serial number: 98f95708e8c78967a4540ccf88267ca43116255
Valid from: Mon Oct 21 14:57:23 AEDT 2024 until: Tue Oct 21 14:57:23 AEDT 2025
Certificate fingerprints:
         SHA1: F0:76:79:A2:D7:EE:BD:29:BE:FC:51:33:64:D7:8F:4A:8F:49:C9:76
         SHA256: 3E:C3:E5:F1:FE:CC:52:F7:94:BA:55:44:0B:F1:72:6A:5C:B0:C1:DC:A8:F6:F6:C1:F1:8C:20:81:9F:5C:5A:FB
Signature algorithm name: SHA256withRSA
Subject Public Key Algorithm: 2048-bit RSA key
Version: 3

Extensions:

#1: ObjectId: 2.5.29.35 Criticality=false
AuthorityKeyIdentifier [
KeyIdentifier [
0000: 1C 84 7D DB 50 58 E6 22   14 95 D1 51 43 7C 98 EB  ....PX."...QC...
0010: EF CD 77 C9                                        ..w.
]
]

#2: ObjectId: 2.5.29.19 Criticality=true
BasicConstraints:[
  CA:true
  PathLen: no limit
]

#3: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 1C 84 7D DB 50 58 E6 22   14 95 D1 51 43 7C 98 EB  ....PX."...QC...
0010: EF CD 77 C9                                        ..w.
]
]

Trust this certificate? [no]:  yes
Certificate was added to keystore
</pre>

<h2>Configuring Springboot for mTLS</h2>

Server application.properties
<pre>
server.port=8443
server.ssl.key-store=classpath:cert/server.p12
server.ssl.key-store-password=[server_keystore_password]
server.ssl.key-store-type=PKCS12
server.ssl.client-auth=NEED
server.ssl.trust-store=classpath:cert/truststore.p12
server.ssl.trust-store-password=password
server.ssl.trust-store-type=PKCS12
</pre>

# Server Project Controller / Codes
controller/ServerController.java
<pre>
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {

    @GetMapping("/connect")
    public String get() {
        return "Successfully connected!";
    }
}
</pre>

# Client Project Controller / Codes
configuration/RestClientConfig.java
<pre>
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Configuration
public class RestClientConfig {

    private static final String PKCS12_FILE_PATH = "C:\\git-projects\\Example\\springboot-api-mTLS-samples\\client\\src\\main\\resources\\cert\\client.p12";
    private static final String PKCS12_PASSWORD = "password";
    private static final String TRUSTSTORE_FILE_PATH = "C:\\git-projects\\Example\\springboot-api-mTLS-samples\\client\\src\\main\\resources\\cert\\truststore.p12";
    private static final String TRUSTSTORE_PASSWORD = "password";

    @Bean
    public RestTemplate restTemplate() {
        SSLContext sslContext = configureSSLContext();
        return new RestTemplate(createRequestFactory(sslContext));
    }

    private SSLContext configureSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            char[] keyStorePassword = PKCS12_PASSWORD.toCharArray();
            keyStore.load(new FileInputStream(PKCS12_FILE_PATH), keyStorePassword);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword);

            KeyStore trustStore = KeyStore.getInstance("JKS");
            char[] trustStorePassword = TRUSTSTORE_PASSWORD.toCharArray();
            trustStore.load(new FileInputStream(TRUSTSTORE_FILE_PATH), trustStorePassword);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            return sslContext;
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException |
                 UnrecoverableKeyException | KeyManagementException e) {
            throw new RuntimeException("Error configuring SSLContext", e);
        }
    }

    private ClientHttpRequestFactory createRequestFactory(SSLContext sslContext) {
        return new CustomRequestFactory(sslContext);
    }

    private static class CustomRequestFactory extends SimpleClientHttpRequestFactory {

        private final SSLContext sslContext;

        public CustomRequestFactory(SSLContext sslContext) {
            this.sslContext = sslContext;
        }

        @Override
        protected void prepareConnection(java.net.HttpURLConnection connection, String httpMethod) throws IOException {
            if (connection instanceof javax.net.ssl.HttpsURLConnection) {
                ((javax.net.ssl.HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
            }
            super.prepareConnection(connection, httpMethod);
        }
    }
}
</pre>

controller/SSLClient.java
<pre>
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class SSLClient {

    private final RestTemplate restTemplate;

    @Autowired
    public SSLClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/send-request")
    public ResponseEntity<String> sendRequest() {
        String url = "https://localhost:8443/connect";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response;
    }
}
</pre>

# Run your project (boot run)

<h3>Step 1: Run the Server - Boot Run</h3>
![image](https://github.com/user-attachments/assets/12561657-1cf5-40d0-84f5-a1bf9ef4cde1)

<h3>Step 2: Run the Client - Boot Run</h3>
![image](https://github.com/user-attachments/assets/36cdbfe0-a310-4fcb-9708-1c6250e4be08)

<h3>Step 3: Use Postman / Browser and hit http://localhost:8080/api/send-request</h3>
![image](https://github.com/user-attachments/assets/718f3da4-6782-4e5c-bafb-4756dcf96408)


# References
- https://bohutskyi.com/security-mtls-in-spring-boot-aef44316dd4b
- https://medium.com/@nazeer.arus18/consuming-a-secure-api-with-mutual-tls-authentication-in-spring-boot-6ad45d7adb92
