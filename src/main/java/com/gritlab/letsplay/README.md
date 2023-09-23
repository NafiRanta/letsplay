    ## Let's Play
This is a basic CRUP API application consisting of two entities namely User and Product. The application is developed using Spring Boot. 

## Setup
1. Open docker in the background
2. Open IntelliJ IDEA
3. Click on docker-compose.yaml and press the run button next to services on line 2
4. Run LetsPlayApplication
5. Open Postman


## User entity
A user has the following:
- String id (auto generated with UUID)
- String name (required)
- String email (required)
- String password (required: will be hashed and salted)
- String role (created user will be assigned ROLE_USER)

## Product Entity
A product has the following:
- String id (auto generated with UUID)
- String name (auto generated with UUID)
- String description (auto generated with UUID)
- Double price (auto generated with UUID)
- String userId (set as authenticated user's id)

## Relationship between User and Product
- Created user will be assigned ROLE_USER 
- A user can own >= 0 number of products
- Product cannot exist without a valid userId
- Deleted user will delete all his/her products

## Access 
- ALL: unauthenticated and authenticated users
- ALLAuth: all authenticated users
- ROLE_ADMIN: authenticated users with ROLE_ADMIN
- ROLE_USERS: authenticated users with ROLE_USER

## Authenticate [ALL]
- username: email
- password: password

## CRUD API endpoints for Users
- create: https://localhost:443/users/new [ALL]
- read all: https://localhost:443/users/all
- read single: https://localhost:443/users/single/{id} [ALLAuth] [ROLE_ADMIN or the authenticated user itself]
- update:https://localhost:443/users/update/{id} [ROLE_ADMIN]
- delete: https://localhost:443/users/delete/{id} [ROLE_ADMIN]

## CRUD API endpoints for Products
- create: https://localhost:443/products/create [ALLAuth]
- read all: https://localhost:443/products/all [ALL]
- read single: https://localhost:443/products/single/{id} [ALL]
- update: https://localhost:443/products/update/{id} [ROLE_ADMIN]
- delete: https://localhost:443/products/delete/{id} [ROLE_ADMIN]


