FROM java:8
VOLUME /tmp
ADD build/libs/auth-0.0.1-SNAPSHOT.jar app.jar
#RUN bash -c 'touch /app.jar'
EXPOSE 9999
ENTRYPOINT ["java","-jar","/app.jar"]