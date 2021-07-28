FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/ContributoryLoanApp-0.0.1-SNAPSHOT.jar contributoryLoanApp.jar
ENTRYPOINT ["java", "-jar", "contributoryLoanApp.jar"]