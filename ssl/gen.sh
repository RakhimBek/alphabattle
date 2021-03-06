# generate client side cert
keytool -genkeypair -alias nt-gateway -keyalg RSA -keysize 2048 -storetype JKS -keystore nt-gateway.jks -validity 3650 -ext SAN=dns:localhost,ip:127.0.0.1

# export client side public key from generated client side cert
keytool -export -alias nt-gateway -file nt-gateway.crt -keystore nt-gateway.jks

# generate server side certificate
keytool -genkeypair -alias nt-ms -keyalg RSA -keysize 2048 -storetype JKS -keystore nt-ms.jks -validity 3650 -ext SAN=dns:localhost,ip:127.0.0.1

# export server side public key from generated server side cert
keytool -export -alias nt-ms -file nt-ms.crt -keystore nt-ms.jks

# Import Client Cert to Server jks File:
keytool -import -alias nt-gateway -file nt-gateway.crt -keystore nt-ms.jks

# Import Server Cert to Client jks File:
keytool -import -alias nt-ms -file nt-ms.crt -keystore nt-gateway.jks
