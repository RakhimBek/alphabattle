FROM amd64/gradle

COPY . /usr/src/starter
WORKDIR /usr/src/starter

RUN gradle build -Dorg.gradle.daemon=false --no-daemon
RUN gradle war -Dorg.gradle.daemon=false --no-daemon

CMD ["java", "-jar", "build/libs/alphabattle-0.0.1-SNAPSHOT.war"]
