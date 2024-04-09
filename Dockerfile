FROM gradle:8.3.0-jdk20

WORKDIR /app

COPY /app/src/main /app/src/main
COPY /app/build.gradle.kts /app
COPY /app/settings.gradle.kts /app

RUN gradle installDist

EXPOSE 7070

CMD ./build/install/app/bin/app