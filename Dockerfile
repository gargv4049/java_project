FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app
COPY pom.xml ./
COPY src ./src

RUN mvn --batch-mode --no-transfer-progress clean package -DskipTests

FROM tomcat:11.0.26-jdk25-temurin-noble

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/found.war /usr/local/tomcat/webapps/ROOT.war

# Supply PORT and PostgreSQL credentials as runtime environment variables, never build arguments.
EXPOSE 10000

CMD ["sh", "-c", "sed -i \"s/port=\\\"8080\\\"/port=\\\"${PORT:-10000}\\\"/\" /usr/local/tomcat/conf/server.xml && catalina.sh run"]
