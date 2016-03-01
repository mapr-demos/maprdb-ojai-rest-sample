# MapR-DB REST Application

This project shows how to integrate MapR-DB JSON and Java wit the OJAI API to build a REST Application.

It contains:

* A Main class that start Jetty Server and configure a REST Interface using JAX-RS
* Swagger to easily access the API http 
* An Angular JS Application that consumes this API


### Pre-requisites

* Java SDK 7 or newer
* Maven 3
* MapR 5.1 Sandbox or Cluster

In your MapR-DB environment, using a terminal, create the following folder, and change the permissions: 

```
ssh mapr@maprdemo
 
cd /mapr/demo.mapr.com/

chmod 777 apps

mkdir apps/blog

chmod 777 apps/blog

```


## Usage

Clone this repository, then

```
mvn clean package
```

To Run the application:

```
mvn exec:java -Dexec.mainClass="com.mapr.db.samples.rest.Main"
```


Once the server is started you can access the Swagger UI using the following URI:

* [http://localhost:8080/swagger](http://localhost:8080/swagger)

Or the Web applications

* [http://localhost:8080/app/#/](http://localhost:8080/app/#/)


You can use the Web application to create a user and a blog post.

Then in the Swagger UI you can discover some interesting features:

* Create user from a predefined JSON Object (POJO)
* Create user from "any" JSON String
* List users using a simple projection
* Update user to add a nickname, add interests, and remove these attributes. You see here the flexible schema in action
* Query users by interest (list)
* Similar features are exposed in the Articles REST API.




