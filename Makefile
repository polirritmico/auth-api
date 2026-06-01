# Makefile for Spring Boot Microservices v0.1

.PHONY: run clean

default: run

help:
	@echo "Available targets:"
	@echo "  run    Start DB container and run the project (default)"
	@echo "  clean  Clean the project"

run:
	docker compose up -d
	./mvnw spring-boot:run

clean:
	docker compose down -v
	./mvnw clean
