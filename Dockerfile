FROM java:8
ADD "/target/spider-0.0.1-SNAPSHOT.jar" "app.jar"
RUN echo "Asia/Shanghai" > /etc/timezone
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app.jar" ]