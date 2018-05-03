#!/bin/bash

## ------------------------------------
## Change here your configuration!
## ------------------------------------

SOURCE_CAS_CONFIG=/home/giuseppe/git/giuseppeurso-eu/spring-mvc/mvc-security-cas/cas-server/src/main/cas-server-config
DEST_CAS_CONFIG=/tmp/cas-server-config

DNAME="${DNAME:-CN=localhost,OU=Example,OU=Org,C=US}"
CERT_SUBJ_ALT_NAMES="${CERT_SUBJ_ALT_NAMES:-dns:localhost,dns:localhost,ip:127.0.0.1}"

JAVA_OPTS="-Xms256m -Xmx2048m -Djava.net.preferIPv4Stack=true -DCAS_BANNER_SKIP=true $JAVA_OPTS"

## ------------------------------------

MVN=`which mvn`

function help() {
	echo "Usage: build.sh [copy|clean|package|bootrun|run|debug|gencert]"
	echo "	copy: Copy config from $SOURCE_CAS_CONFIG to $DEST_CAS_CONFIG"
	echo "	clean: Clean Maven build directory"
	echo "	package: Clean and build CAS war"
	echo "	bootrun: Run with maven spring boot plugin"
	echo "	run: Build and run cas.war via Java (i.e. java -jar target/cas.war)"
	echo "	debug: Run CAS.war and listen for Java debugger on port 5000"
	echo "	gencert: Create keystore with SSL certificate in location where CAS looks by default"	
}

function copy() {
	echo -e "Creating CAS configuration directory under $DEST_CAS_CONFIG"
	mkdir -p $DEST_CAS_CONFIG

	echo -e "Copying configuration files from $SOURCE_CAS_CONFIG to $DEST_CAS_CONFIG"
	cp -rfv $SOURCE_CAS_CONFIG/* $DEST_CAS_CONFIG/
}

function clean() {
   $MVN clean
}

function package() {
    $MVN clean package
	copy
	gencert
}

function bootrun() {
    echo "---------------------------"
    echo "[SPRINGBOOT RUNNER] Building CAS war and Starting the embedded server via SpringBoot with option -Dcas.standalone.config=$DEST_CAS_CONFIG..."
    echo "---------------------------"
	$MVN clean package spring-boot:run -P bootiful -Dcas.standalone.config=$DEST_CAS_CONFIG
}

function run() {
    echo "---------------------------"
    echo "[JAVA RUNNER] Starting the embedded server via java with option --cas.standalone.config=$DEST_CAS_CONFIG..."
    echo "---------------------------"
	java $JAVA_OPTS -jar target/cas.war --cas.standalone.config=$DEST_CAS_CONFIG 
}

function debug() {
	$MVN clean package && java -Xdebug -Xrunjdwp:transport=dt_socket,address=5000,server=y,suspend=n -jar target/cas.war
}

function gencert() {
    # Creating cas config dir if not exists.
	if [[ ! -d $DEST_CAS_CONFIG ]] ; then
		copy
	fi
	which keytool
	if [[ $? -ne 0 ]] ; then
		echo Error: Java JDK \'keytool\' is not installed or is not in the path
		exit 1
	fi
	
	echo "Generating keystore for CAS with DN ${DNAME}"
	keytool -genkeypair -alias cas -keyalg RSA -keypass changeit -storepass changeit -keystore $DEST_CAS_CONFIG/thekeystore -dname ${DNAME} -ext SAN=${CERT_SUBJ_ALT_NAMES}
	keytool -exportcert -alias cas -storepass changeit -keystore $DEST_CAS_CONFIG/thekeystore -file $DEST_CAS_CONFIG/cas.cer
}


if [ $# -eq 0 ]; then
    echo -e "No commands provided. Defaulting to [run]\n"
    run
    exit 0
fi

case "$1" in
"copy")
    copy
    ;;
"clean")
	shift
    clean "$@"
    ;;
"package")
	shift
    package "$@"
    ;;
"bootrun")
	shift
    bootrun "$@"
    ;;
"debug")
    debug "$@"
    ;;
"run")
    run "$@"
    ;;
"gencert")
    gencert "$@"
    ;;
*)
    help
    ;;
esac


