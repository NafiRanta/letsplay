## Let's Play
This is a basic CRUP API application consisting of two entities namely User and Product. The application is developed using Spring Boot. 

## User entity
A user has the following:
- String id
- String name
- String email
- String password (hashed and salted)
- String role (ROLE_ADMIN or ROLE_USER)

## Product Entity
A product has the following:
- String id
- String name
- String description
- Double price
- String userId

## Relationship between User and Product
- A user can own >= 0 number of products
- Product cannot exist without a valid userId
- Deleted user will delete all his/her products

## Access 
- ALL: unauthenticated and authenticated users
- ALLAuth: all authenticated users
- ROLE_ADMIN: authenticated users with ROLE_ADMIN
- ROLE_USERS: authenticated users with ROLE_USER

## CRUD API for Users and Access
- authenticate: /users/authenticate [All]
- create: /users/new [All]
- read all: /users/all [ALLAuth]
- read single: /users/single/{id} [ROLE_ADMIN or the authenticated user itself]
- update: /users/update [ROLE_ADMIN]
- delete: /users/delete [ROLE_ADMIN]

## CRUD API for Products
- create: /products/create [ALLAuth]
- read all: /products/all [All]
- read single: /products/single/{id} [All]
- update: /products/single/{id} [ROLE_ADMIN]
- delete: /products/delete/single/{id} [ROLE_ADMIN]


## Setup
1. Open docker in the background
2. Open IntelliJ IDEA
3. Click on docker-compose.yaml and press the run button next to services on line 2
4. Run LetsPlayApplication
5. Open Postman