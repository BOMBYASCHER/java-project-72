.DEFAULT_GOAL := build
build:
	./gradlew clean build
run-dist:
	./build/install/app/bin/app
report:
	./gradlew jacocoTestReport
test:
	./gradlew test
lint:
	./gradlew checkstyleMain checkstyleTest
.PHONY: build