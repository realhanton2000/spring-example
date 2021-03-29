# spring-playground
Playground to try spring features

launch rich jar as
```
java -jar spring-playground-0.0.1.jar --spring.profiles.active=mongo
```
application-mongo.properties
```
spring.data.mongodb.uri=mongodb+srv://XX:XX@XX/XX?XX
bankapp.dbAccessType=MONGO
```
application-h2.properties
```
bankapp.dbAccessType=JPA
```
