#! /bin/bash
cd ..
cd web
npm run build
cd ..
mvn clean compile -DskipTests
cp -rf web/dist/* target/classes/static/
mvn package -DskipTests
echo 打包完成
