# Pet Emporium Sample DB

This project is meant to be used with the [Pet Emporium ](git@git.catalystitservices.com:MDW_2018_/YASHASHREE/Yashashree_petemporium.git)
example project. The Dockerfile in this project creates a modified MongoDB
Database with sample data for the Pet Emporium.


This project is meant to be used with the Pet Emporium
example project. The Dockerfile in this project creates a modified MongoDB
Database with sample data for the Pet Emporium.

# Getting Started

These instructions will get you a copy of the project up and running on your local
machine for development and testing purposes.

$ git clone git@git.catalystitservices.com:MDW_2018_/YASHASHREE/yashashree_Pet_Emporium_db.git

Make sure that you have [Docker](https://www.docker.com/products/overview) installed
Make sure that you have Docker installeed
on your local machine. You will need Docker in order to run the sample database image.


# Using the Docker Image

The following sections outline the use


# Building The Docker Image
```
$ docker build -t pet_emporium .
```
# Running The Docker Image

$ docker run -d -p 27089:27089 --name pet_emporium pet_emporium

#Connecting to MongoDB

You can use MongoDB Compass, RoboMongo, or the Terminal to connect to the
running MongoDB instance. All collections are located in the  'pet_emporium' database.


$ docker build -t pet_emporium .

# Running The Docker Image

$ docker run -d -p 27017:27017 --name pet_emporium pet_emporium

# Connecting to MongoDB

You can use MongoDB Compass, RoboMongo, or the Terminal to connect to the
running MongoDB instance. All collections are located in the  pet_emporium' database.
>>>>>>> c925c3c02cd1b6947fa7d343a11e09d7fb861704

In a terminal window:

$ docker exec -it pet_emporium mongo - will connect to the running container and start the mongo shell.

```
show databases
    admin
 pet_emporium
    local
>type your commands here...
```

#Deployment
Using Postman for Testing

A full library of HTTP calls for testing is available through Postman.
Be sure that you have this application is installed, and then add the Postman
collection located here. This
collection contains an organized series of HTTP requests which can test every
function of this API.


#Using this API(git@git.catalystitservices.com:MDW_2018_/YASHASHREE/Yashashree_petemporium.git)


#Authentication


This API makes use of the spring-security library. When submitting requests
through Postman, you must provide the authentication credentials in the "Users"
collection of the database. When implementing a front-end, your users must have
the "ADMIN" role attached to the "roles" field of their User object to access GET
POST, PUSH, and DELETE requests.


Receiving results

This API can be used through an application like Postman, or can be consumed
by a front-end. In this case, create HTML forms with matching variables to the
ones in the pre-made database. Have your JavaScript convert this form input into
a JSON document, then forward this document to the matching URL with a POST request.


For reference, the URLs are:

{root}/customers
{root}/inventory
{root}/pets
{root}/purchases

Retrieving From Database

This API can retreive information from the linked database. Sending a basic
GET request to any of the above URLs will return a complete list of all matching
documents in the database. You may be specific by adding a further path to the
URL; for example, sending a GET request to the following URL:

{root}/pets/name?name=Cat
would return the matching document from the database with the passed in field.Same as for all other CRUD operation for Deleting a document you need to make delete request ,for update make put request ,and insert new document make post request 

#Built With

* [Docker] (https://www.docker.com/)- Containerization Engine

* [MongoDB ]((https://www.mongodb.com/))- Document Database Management System

* Java SDK 8

#Versioning
We use [SemVer](http://semver.org/) for versioning. For the versions available, see the 
[tags on this repository](git@git.catalystitservices.com:MDW_2018_/YASHASHREE/yashashree_Pet_Emporium_db.git)

#Author

**YashashreeTrivedi-[ytrivedi@catalyte.io]


#Bug Report

If you encounter any bugs, undesirable behavior, or lacking functionality, please
do not hesistate to reach out to one of the authors above. Thank you for taking
the time to look API

...
> show databases
    admin
 pet_emporium
    local
> type your commands here...

# Deployment

Add additional notes about how to deploy this on a live system


# Built With



Docker - Containerization Engine

MongoDB - Document Database Management System



# Versioning

We use SemVer for versioning. For the versions available, see the tags on this repository.


# Authors
YashashreeTrivedi-[ytrivedi@catalyte.io]
