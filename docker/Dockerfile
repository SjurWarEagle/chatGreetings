FROM maven:3.8.6-openjdk-18 as buildstep
RUN mkdir -p /project/

WORKDIR /project/
RUN git clone https://github.com/SjurWarEagle/chatGreetings .
RUN mvn clean compile assembly:single

FROM nginx:mainline-alpine
WORKDIR /project
COPY --from=buildstep /project/chatGreetings-1.0-SNAPSHOT-jar-with-dependencies.jar /project

WORKDIR /project/chatGreetings/target
CMD ["java", "-jar", "/project/chatGreetings-1.0-SNAPSHOT-jar-with-dependencies.jar"]
