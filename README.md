# camel-quarkus-quickstart project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Camel Quickstart in Quarkus
In this quickstart project, it demos 4 Camel Routes running in Quarkus runtime. Each routes can be invoked with a REST API.

### REST - SQL Routes
The Camel REST-SQL routes demostrate how to use REST API to insert and retreve sql data from database. The demo is using derby database. You can subsitude with any JDBC database of your own choice.

Insert db users using a json file

```
curl --location --request POST 'http://localhost:8080/db/users' \
--header 'Content-Type: application/json' \
--data-raw '[
	{
		"name": "Ram3",
		"email": "ram@gmail.com",
		"age": 23
	},
	{
		"name": "Shyam4",
		"email": "shyam23@gmail.com",
		"age": 28
	}
]'
```

Retrieve db users from the database
```
curl --location --request GET 'http://localhost:8080/db/users'
```

### REST - JMS Routes
The Camel REST-JMS routes demostrate how to use REST API to insert and retrieve json message in the Artemis MQ broker.

Send a json message to QUEUE FOO
```
curl --location --request POST 'http://localhost:8080/queue/FOO' \
--header 'Content-Type: application/json' \
--data-raw '[
	{
		"name": "Ram3",
		"email": "ram@gmail.com",
		"age": 23
	},
	{
		"name": "Shyam4",
		"email": "shyam23@gmail.com",
		"age": 28
	}
]'
```
Retrieve json message from QUEUE FOO
```
curl --location --request GET 'http://localhost:8080/queue/FOO'
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw clean quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `kubernetes-quickstart-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/camel-quarkus-quickstart-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/camel-quarkus-quickstart-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.