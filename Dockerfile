FROM gradle:8.3.0-jdk20

WORKDIR /app

COPY . /app

RUN gradle installDist

CMD ./build/install/java-project-72/bin/java-project-72