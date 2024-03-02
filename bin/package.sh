#! /bin/bash
cd ..
cd web
rm -rf dist
cd ..
mvn clean package -DskipTests -Dmaven.npm.skip=false