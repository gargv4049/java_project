# Multi-stage Docker build for Campus Lost & Found Management System
# Build stage
FROM maven:3-eclipse-temurin-25 AS build

WORKDIR /app
COPY pom.xml ./
COPY src ./src

# Build production WAR package skipping tests for fast deployment
RUN mvn --batch-mode --no-transfer-progress clean package -DskipTests

# Runtime stage with Apache Tomcat 11 & Eclipse Temurin JDK 25
FROM tomcat:11-jdk25

# Remove default Tomcat web applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Deploy WAR to both root context (/) and application context (/LostFoundManagementSystem)
# to guarantee zero 404 context-path routing issues across all hosting environments.
COPY --from=build /app/target/LostFoundManagementSystem.war /usr/local/tomcat/webapps/ROOT.war
COPY --from=build /app/target/LostFoundManagementSystem.war /usr/local/tomcat/webapps/LostFoundManagementSystem.war

# Ensure upload directory exists
RUN mkdir -p /usr/local/tomcat/uploads/items

# Expose default HTTP ports (8080 standard, 10000 Render)
EXPOSE 8080 10000

# Dynamically bind Tomcat HTTP port from $PORT (default 8080 locally or 10000 on Render)
CMD ["sh", "-c", "sed -i \"s/port=\\\"8080\\\"/port=\\\"${PORT:-8080}\\\"/\" /usr/local/tomcat/conf/server.xml && catalina.sh run"]