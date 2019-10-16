# Pet Emporium API
This is the Pet Emporium API. This API will allow users to access and modify data for Catalyte's Pet Emporium.


# Getting Started
In Kitematic, please run the container. Make a note of the port number once it starts up. In src/main/resources open application.properties. Pay attention to the following lines:

   spring.data.mongodb.database= petemporium
    spring.data.mongodb.port= 27089

Make sure that the database matches the database name and the port matches as well. This may change each time you boot up the container, so please change accordingly.
Now that everything is ready. Please go to the Security.Config.java file in the io.catalyte.pet_emporium package. This will contain security information as well as the username and passwords for the roles. You can change the password  and username by editing the following lines:


    .withUser("employee").
        password("password").
        roles("USER")
        .and()
        .withUser("manager")
        .password("encryption")
        .roles("USER", "ADMIN");

This will be very important for accessing different routes. You will have to use this information.
Now, run the application from the entry point PetEmporium.java



# Accessing information


When the application runs, it will show the localhost port in the console. Use that address (localhost:8080 for example) as the home route.
Look for a line that looks like this:


The application allows the user to create, read, update, and delete data based on their role. In the io.catalyte.petemporium.controller package, each class has an annotation over it (@PreAuthorize), that will explain which roles can do which method. Currently only admins can create, update, and delete data. Please use Postman to grasp the results in full.
Please go the following URL to see the API documentation. This will show all of the different routes the user can utilize to access and modify information


http://localhost:8080/swagger-ui.html

Please make sure that when you POST and PUT, that you are not leaving fields blank. 
The application allows the user to create, read, update, and delete data based on their role. In the io.catalyte.petemporium.controller package, each class has an annotation over it (@PreAuthorize), that will explain which roles can do which method. Currently only admins can create, update, and delete data. Please use Postman to grasp the results in full.

In each controller, please look at the topmost @RequestMapping to know how to structure your URL.

For instance, @RequestMapping("/customers") will have your URL look like localhost:8080/customers to access the customer data. Same for pet ,Inventory and purchases. for get , put ,delete method you need a specific field to perform operation like  provide Id you can get all record based on ID


Each method matches to a URL. If the @RequestMapping above the methods have no value field, then enter the URL to match the topmost @RequestMapping
Along with this file, we have provided a document showing sample URLs and methods for each collection in the database. These URLs and methods were tested with Postman.




# Built With

Eclipse - IDE used

Maven - Dependency Management

Spring - Framework handling connections to the database and security

MongoDB - database


# Author

Yashashree Trivedi - ytrivedi@catalytr.io