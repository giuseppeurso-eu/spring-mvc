Project stef-sso-cas
============================
Generic starting template for local CAS WAR. Build this project to get a runnable CAS WAR.

## Requirements

* JDK 1.8+
* Maven 3.2+
* Apache Tomcat 8 (only for runtime environments)

## Versions
Update pom.xml with your preferred CAS version

```xml
<cas.version>5.2.x</cas.version>
```

## Quick Start

### 1) Update CAS configuration path in build.sh
```bash
## build.sh
SOURCE_CAS_CONFIG="/home/project/cas-server-config"
DEST_CAS_CONFIG="/your_server_path/cas-server-config"
```
### 2) Build war
```bash
$ ./build.sh package
```

### 3) Start embedded Tomcat (CAS standalone mode)
```bash
$ ./build.sh run
```

## Build commands

To see what commands are available to the build script, run:

```bash
./build.sh help
```

To package the final web application, run:

```bash
./build.sh package
```

To generate HTTPS certificates run:

```bash
./build.sh gencert
```


## Runtime

On a successful WAR deployment, CAS will be available at:
* `http://localhost:8080/cas`
* `https://localhost:8443/cas`

Defaul credentials:
* `user: admin `
* `password: admin `

## Spring Boot

Run the CAS web application as an executable WAR via Spring Boot. This is most useful during development and testing.

```bash
./build.sh bootrun
```

### Warning!

Be careful with this method of deployment. `bootRun` is not designed to work with already executable WAR artifacts such that CAS server web application. YMMV. Today, uses of this mode ONLY work when there is **NO OTHER** dependency added to the build script and the `cas-server-webapp` is the only present module. See [this issue](https://github.com/apereo/cas/issues/2334) and [this issue](https://github.com/spring-projects/spring-boot/issues/8320) for more info.


## Embedded App Server Selection (standalone mode)

There is an app.server property in the `pom.xml` that can be used to select a spring boot application server:


 `-tomcat` (default) | `-jetty` | `-undertow`  

It can also be set to an empty value (nothing) if you want to deploy CAS to an external application server of your choice.

```xml
<app.server>-tomcat<app.server>
```
## Resources
https://www.apereo.org/projects/cas
https://apereo.github.io/cas/5.2.x/index.html
https://apereo.github.io/cas/5.2.x/installation/Configuration-Server-Management.html

(Cas and Spring Security)

https://docs.spring.io/spring-security/site/docs/current/reference/html/cas.html
https://github.com/spring-projects/spring-security/tree/master/samples/xml/cas
 

