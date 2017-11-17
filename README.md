# java-jwt-auth
Java authentication and authorization with JwtToken example using Maven, Jersey, Hibernate and H2

## Getting started
First you need to have Maven installed then import the project as Maven project
## Endpoints
### /api/register - User registration. Method: POST
Request:
```
{
	"name":"Test",
	"email":"test@example.com",
	"password":"1234"
}
```
Response:
```
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI1YTBlYTJjZmIwNzc0ODIwNjA0MjZmMDUiLCJpYXQiOjE1MTA5MDg2MjMsImV4cCI6MTUxMDk5NTAyM30.X5XDB6_m1R9P87ErslknRs1qP6gVV815HyPLPRrKP8s"
}
```
### /api/login - User login. Method: POST
Request:
```
{
	"email":"test@example.com",
	"password":"1234"
}
```
Response:
```
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI1YTBlYTJjZmIwNzc0ODIwNjA0MjZmMDUiLCJpYXQiOjE1MTA5MDg2MjMsImV4cCI6MTUxMDk5NTAyM30.X5XDB6_m1R9P87ErslknRs1qP6gVV815HyPLPRrKP8s"
}
```
### /api/me - User details. Method: GET
Request:
```
Header:
authorization: <token>
```
Response:
```
{
    "id": 1,
    "name": "Test",
    "email": "test@example.com"
}
```

## Built With

* [Java](https://www.java.com/en/)
* [Maven](https://maven.apache.org/)
* [Hibernate](http://hibernate.org/)
* [Jersey](https://jersey.github.io/)
* [Lombok](https://projectlombok.org/)

## Authors

* **Petar Petrov** - *Initial work* - [Petrakus](https://github.com/Petrakus)

See also the list of [contributors](https://github.com/Petrakus/java-jwt-auth/graphs/contributors) who participated in this project.
