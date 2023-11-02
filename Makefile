#Makefile

clean:
	./gradlew clean

prepare:
	./gradlew clean installDist

build-and-check:
	./gradlew clean build test checkstyleMain checkstyleTest

report:
	./gradlew jacocoTestReport

test:
	./gradlew test

.PHONY: build
