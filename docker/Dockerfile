FROM maven:3.8.6-openjdk-18 as buildstep
RUN mkdir -p /project/

#RUN pwd
#RUN ls -la
WORKDIR /project/
COPY . /project
RUN ls -al
#Skipping tests because needs more environment setup
RUN mvn -Dmaven.test.skip=true clean package spring-boot:repackage
RUN ls /project

FROM maven:3.8.6-openjdk-18
RUN mkdir -p /project/config
#WORKDIR /project
COPY --from=buildstep /project/twitchGreetings-*.jar /project

WORKDIR /project/
CMD ["java", "-jar", "twitchGreetings-1.0-SNAPSHOT.jar"]
