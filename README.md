# MultiRegionalPubsubApplication

PoC for publishing simultaneously to multiple GCP Cloud Pub/Sub regional endpoints and topics.

## About The Project

This is a minimal implementation example of a Spring Boot app to demonstrate how to easily configure your Spring Boot app to publish the same message to multiple Pub/Sub topics using regional endpoints.

## Prerequisites

JDK 17 (may work fine with JDK 11 as well).

## Usage

Try running as is first, then tweak the configuration and try running again.

### Running

Run the Spring Boot app:

```bash
./gradlew clean build bootRun
```

Issue an HTTP POST request to `http://localhost:8080/publish`, for example:

```bash
curl -X POST --location "http://localhost:8080/publish" \
    -H "Content-Type: application/json" \
    -d "test-message"
```

Observe the logs prefixed with `####`. Go to your project's GCP console and browse the subscriptions in Pub/Sub.

### Configuring

Prepare your GCP environment - create a project, enable Pub/Sub API, create a couple topics. Make sure your GCP credentials are available to the app. It needs to have `pubsub.topics.publish` permission to be able to publish to Pub/Sub.

Update the configuration file in `src/main/resources/application.yml`. For example, replace the project ID part of the topic name with your project's ID, and try adding/removing topics with endpoints.

After configuring, try running again.
