# S3_FriendManagementAPI_LenHo
## The Friend Management RESTFul Api

Introduction simple api built using Java spring

##### Build the project

docker-compose build

##### Launch the project
docker-compose up
```
Before:
Access login to get Authorication: http://localhost:8080/login
username: admin
password: password
```
##### RESTFul Api for User
```
1 (POST)Add user: http://localhost:8080/api/users
  Example Request
    {
	  "email" : "jason@gmail.com"
    }
  Response Example
    {
        "email" : "jason@gmail.com"
    }
```
```
2 (GET)Get users list: http://localhost:8080/api/users
  Response Example
    [
    {
        "email": "jason@gmail.com"
    },
    {
        "email": "kati@gmail.com"
    }
]
```
```
3 (GET)Get user: http://localhost:8080/api/users/{jason@gmail.com}
   Response Example
        {
        "email": "jason@gmail.com"
    	}
```
```
4 (DELETE)Delete user: http://localhost:8080/api/users/{jason@gmail.com}
  Response Example
    {
        "Delete user success": true
    }
```
```
##### RESTFul Api for Relationship
1 (GET)Get all relationships: http://localhost:8080/api/relationship
  Response Example
    [
      {
          "relationshipPK": {
              "userEmail": "jason@gmail.com",
              "friendEmail": "kati@gmail.com"
          },
          "areFriends": true,
          "isSubscriber": false,
          "isBlock": false
      },
      {
          "relationshipPK": {
              "userEmail": "jason@gmail.com",
              "friendEmail": "david@gmail.com"
          },
          "areFriends": true,
          "isSubscriber": false,
          "isBlock": false
      }
    ]
```
```
2 (POST)Get relationship: http://localhost:8080/api/relationship/find-relationship-by-id
  Example Request
    {
      "userEmail": "jason@gmail.com",
      "friendEmail": "kati@gmail.com"
    }
  Response Example
    {
      "relationshipPK": {
          "userEmail": "jason@gmail.com",
          "friendEmail": "kati@gmail.com"
      },
      "areFriends": true,
      "isSubscriber": false,
      "isBlock": false
    }
```
```
3 (POST)Create a friend connection : http://localhost:8080/api/relationship
  Example Request
    {
      "friends":["jason@gmail.com","kati@gmail.com"]
    }
  Response Example
    {
    "success": true
    }
```
```
4 (POST)Retrieve the friends list for an email address :  http://localhost:8080/api/relationship/friends-list
  Example Request
    {
      "email":"jason@gmail.com"
    }
  Response Example
  {
      "success": true,
      "friends": [
          "kati@gmail.com"
      ],
      "count": 1
  }
```
```
5 (POST)Retrieve the common friends list between two email addresses :  http://localhost:8080/api/relationship/common-friends-list
  Example Request
    {
     "friends":["jason@gmail.com","david@gmail.com"]
    }
  Response Example
      {
      "success": true,
      "friends": [
          "kati@gmail.com"
      ],
      "count": 1
      }
```
```
6 (POST)Create subscribe to updates from an email address : http://localhost:8080/api/relationship/be-subscriber
  Example Request
    {
      "requestor" : "jason@gmail.com",
      "target" : "kati@gmail.com"
    }
  Response Example
    {
    "success": true
    }
```
```
7 (POST)Block updates from an email address: http://localhost:8080/api/relationship/to-block
  Example Request
    {
      "requestor" : "jason@gmail.com",
      "target" : "kati@gmail.com"
    }
  Response Example
    {
    "success": true
    }
```
```
8 (POST)Create API to retrieve all email addresses that can receive updates from an email address :  http://localhost:8080/api/relationship/receive-update
  Example Request
    {
      "sender" :"jason@gmail.com",
      "text" : "Hello World! test@s3corp.com"
    }
   Response Example
    {
    "success": true,
    "recipients": [
        "kati@gmail.com",
        "test@s3corp.com"
    ]
}
```
#### Unit Testing
```
1 User Service Test
2 Relationship Service Test
3 User Controller Test
4 Relationship Controller Test
```
