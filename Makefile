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

start-app-with-redis:
	docker-compose up -d

start-app-in-container:
	docker build -t url-shortener .
	docker run url-shortener


.PHONY: build
