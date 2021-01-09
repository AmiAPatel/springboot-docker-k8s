FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=target/employee-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
COPY config/employee.json .
COPY config/employees.json .
ENTRYPOINT ["java","-jar","/app.jar"]

# FROM adoptopenjdk:11-jre-hotspot
# ARG JAR_FILE=target/employee-service-0.0.1-SNAPSHOT.jar
# RUN mkdir app
# COPY ${JAR_FILE} app/app.jar
# COPY config/employee.json app
# COPY config/employees.json app
# ENTRYPOINT ["java","-jar","app/app.jar"]