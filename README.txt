https://bohutskyi.com/security-mtls-in-spring-boot-aef44316dd4b

https://medium.com/@nazeer.arus18/consuming-a-secure-api-with-mutual-tls-authentication-in-spring-boot-6ad45d7adb92

Enable mTLS in SpringBoot
========================
Step 1: Generate the CA Certificate

Step 1.1: Create the CA’s Private Key:
######################################

openssl genrsa -out ca.key 2048

springboot-restapi/src/main/resources/keystore $ openssl genrsa -out ca.key 2048
Generating RSA private key, 2048 bit long modulus (2 primes)
..................+++++
........................................................................................+++++
e is 65537 (0x010001)
37


Step 1.2: Self-Sign and Create the CA Certificate
#################################################

openssl req -x509 -new -nodes -key ca.key -sha256 -days 365 -out ca.pem

/springboot-api-mTLS-samples/server/src/main/resources/keystore (main) $ openssl req -x509 -new -nodes -key ca.key -sha256 -days 365 -out ca.pem
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:AU
State or Province Name (full name) [Some-State]:VIC
Locality Name (eg, city) []:Melbourne
Organization Name (eg, company) [Internet Widgits Pty Ltd]:Mulia Pratama Pty Ltd
Organizational Unit Name (eg, section) []:
Common Name (e.g. server FQDN or YOUR name) []:localhost
Email Address []:


39 606041@5CD423D7NQ:/c/git-projects/Example/springboot-restapi/src/main/resources/keystore $ ls -al
total 12
drwxr-xr-x 1 606041 1049089    0 Oct 16 17:44 .
drwxr-xr-x 1 606041 1049089    0 Oct 16 17:37 ..
-rw-r--r-- 1 606041 1049089 1702 Oct 16 17:42 ca.key
-rw-r--r-- 1 606041 1049089 1434 Oct 16 17:44 ca.pem

Step 2: Generate Server and Client Certificates

Step 2.1: Generate Private Keys
###############################
For the server: openssl genrsa -out server.key 2048

/springboot-restapi/src/main/resources/keystore $ openssl genrsa -out server.key 2048
Generating RSA private key, 2048 bit long modulus (2 primes)
.....+++++
..............+++++
e is 65537 (0x010001)

For the client: openssl genrsa -out client.key 2048

/springboot-restapi/src/main/resources/keystore $ openssl genrsa -out client.key 2048
Generating RSA private key, 2048 bit long modulus (2 primes)
....................................................................+++++
.........................+++++
e is 65537 (0x010001)

Step 2.2: Generate CSRs
#######################
A CSR (Certificate Signing Request) is a specially formatted encrypted message sent from a Secure Sockets Layer (SSL) 
digital certificate applicant to a certificate authority (CA). 

For the server: openssl req -new -key server.key -out server.csr

/springboot-api-mTLS-samples/server/src/main/resources/keystore (main) $ openssl req -new -key server.key -out server.csr
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:AU
State or Province Name (full name) [Some-State]:VIC
Locality Name (eg, city) []:Melbourne
Organization Name (eg, company) [Internet Widgits Pty Ltd]:Mulia Pratama Pty Ltd
Organizational Unit Name (eg, section) []:
Common Name (e.g. server FQDN or YOUR name) []:localhost
Email Address []:

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:
An optional company name []:


For the client: openssl req -new -key client.key -out client.csr

/springboot-api-mTLS-samples/server/src/main/resources/keystore (main) $ openssl req -new -key client.key -out client.csr
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:AU
State or Province Name (full name) [Some-State]:VIC
Locality Name (eg, city) []:Melbourne
Organization Name (eg, company) [Internet Widgits Pty Ltd]:Mulia Pratama Pty Ltd
Organizational Unit Name (eg, section) []:
Common Name (e.g. server FQDN or YOUR name) []:localhost
Email Address []:

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:
An optional company name []:


Step 2.3:  Sign CSRs with the CA Key
####################################
For the server: openssl x509 -req -in server.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out server.crt -days 365

/springboot-api-mTLS-samples/server/src/main/resources/keystore (main) $ openssl x509 -req -in server.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out server.crt -days 365
Signature ok
subject=C = AU, ST = VIC, L = Melbourne, O = Mulia Pratama Pty Ltd, CN = localhost
Getting CA Private Key


For the client: openssl x509 -req -in client.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out client.crt -days 365

/springboot-api-mTLS-samples/server/src/main/resources/keystore (main) $ openssl x509 -req -in client.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out client.crt -days 365
Signature ok
subject=C = AU, ST = VIC, L = Melbourne, O = Mulia Pratama Pty Ltd, CN = localhost
Getting CA Private Key

Step 3: Create PKCS#12 Keystores

Step 3.1 Convert Certificates and Keys to PKCS#12 Format
Note: (command prompt)
Use both password: password

If you're encountering the following WARNING --> WARNING: can't open config file: /usr/local/ssl/openssl.cnf

> set OPENSSL_CONF=C:\Program Files\Git\usr\ssl\openssl.cnf

For the server: openssl pkcs12 -export -out server.p12 -name "server" -inkey server.key -in server.crt -certfile ca.pem

\springboot-api-mTLS-samples\server\src\main\resources\keystore>openssl pkcs12 -export -out server.p12 -name "server" -inkey server.key -in server.crt -certfile ca.pem
Loading 'screen' into random state - done
Enter Export Password:
Verifying - Enter Export Password:

For the client: openssl pkcs12 -export -out client.p12 -name "client" -inkey client.key -in client.crt -certfile ca.pem

\springboot-restapi\src\main\resources\keystore>openssl pkcs12 -export -out client.p12 -name "client" -inkey client.key -in client.crt -certfile ca.pem
\springboot-api-mTLS-samples\server\src\main\resources\keystore>openssl pkcs12 -export -out client.p12 -name "client" -inkey client.key -in client.crt -certfile ca.pem
Loading 'screen' into random state - done
Enter Export Password:
Verifying - Enter Export Password:

Step 4: Create the Truststore

Step 4.1: Import the CA Certificate into a PKCS#12 Truststore

keytool -import -file ca.pem -alias "ca" -keystore truststore.p12 -storetype PKCS12

Password: password

\springboot-api-mTLS-samples\server\src\main\resources\keystore>keytool -import -file ca.pem -alias "ca" -keystore truststore.p12 -storetype PKCS12
Enter keystore password:
Re-enter new password:
They don't match. Try again
Enter keystore password:
Re-enter new password:
Owner: CN=localhost, O=Mulia Pratama Pty Ltd, L=Melbourne, ST=VIC, C=AU
Issuer: CN=localhost, O=Mulia Pratama Pty Ltd, L=Melbourne, ST=VIC, C=AU
Serial number: 4f42297021c480b008e0e976d533534e060f629b
Valid from: Mon Oct 21 18:05:37 AEDT 2024 until: Tue Oct 21 18:05:37 AEDT 2025
Certificate fingerprints:
         SHA1: 84:0D:CF:00:F9:44:C3:87:DF:BD:AC:69:65:92:A5:1A:E2:FD:19:D8
         SHA256: 8B:79:23:AA:BB:DF:B7:4F:B9:26:2F:09:50:0F:7C:72:FD:8E:7A:BF:D3:3B:F1:52:09:57:A2:72:B4:EF:1F:3E
Signature algorithm name: SHA256withRSA
Subject Public Key Algorithm: 2048-bit RSA key
Version: 3

Extensions:

#1: ObjectId: 2.5.29.35 Criticality=false
AuthorityKeyIdentifier [
KeyIdentifier [
0000: 75 47 73 6B 86 35 B9 F8   32 4A 5B E9 28 57 90 18  uGsk.5..2J[.(W..
0010: 29 B5 4E 3D                                        ).N=
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
0000: 75 47 73 6B 86 35 B9 F8   32 4A 5B E9 28 57 90 18  uGsk.5..2J[.(W..
0010: 29 B5 4E 3D                                        ).N=
]
]

Trust this certificate? [no]:  yes
Certificate was added to keystore

==========================================================================================================

Enable TLS Only
===============

Step 1 - Generating a key pair

keytool -genkeypair -alias springdemotls -keyalg RSA -keysize 4096 \
  -validity 3650 -dname "CN=localhost" -keypass changeit -keystore keystore.p12 \
  -storeType PKCS12 -storepass changeit
  
Explanation:
The -keypass option is used for "the password for the key" 

The -storepass option is used for "a password for the keystore"

9 606041@5CD423D7NQ:/c/git-projects/Example/springboot-restapi $ ls
build  build.gradle.kts  gradle  gradlew  gradlew.bat  HELP.md  keystore.p12  README.txt  settings.gradle.kts  src
  
Step 2 - Configuring TLS (one way) in the application.properties

# enable/disable https
server.ssl.enabled=true
# keystore format
server.ssl.key-store-type=PKCS12
# keystore location
server.ssl.key-store=classpath:keystore/keystore.p12
# keystore password
server.ssl.key-store-password=changeit

When configuring the SSL protocol, we’ll use TLS and tell the server to use TLS 1.2:
# SSL protocol to use
server.ssl.protocol=TLS
# Enabled SSL protocols
server.ssl.enabled-protocols=TLSv1.2

Step 3 - Testing using Curl

606041@5CD423D7NQ:/c/git-projects/Example/springboot-restapi/src/main/resources/keystore $ curl https://localhost:8043/employees
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0
curl: (60) SSL certificate problem: self signed certificate
More details here: https://curl.haxx.se/docs/sslcerts.html

curl failed to verify the legitimacy of the server and therefore could not
establish a secure connection to it. To learn more about this situation and
how to fix it, please visit the web page mentioned above.
26 606041@5CD423D7NQ:/c/git-projects/Example/springboot-restapi/src/main/resources/keystore $ curl https://localhost:8043/employees --cacert keystore-cacert.cer
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100    97    0    97    0     0    357      0 --:--:-- --:--:-- --:--:--   359[{"id":1,"name":"Bilbo Baggins","role":"burglar"},{"id":2,"name":"Frodo Baggins","role":"thief"}]
