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

# Run your project (boot run)
