FROM maven:3.3.3 as BUILD
USER root
COPY pom.xml /opt/pom.xml
COPY src /opt/src
WORKDIR /opt
RUN mvn -B -DskipTests=true package

FROM java:8
USER root
COPY --from=BUILD /opt/target/jizhang-0.0.1-SNAPSHOT.jar /opt/jizhang.jar
ENV LANG C.UTF-8
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
EXPOSE 44444
CMD java -jar jizhang.jar