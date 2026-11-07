POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "kramny",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

PUT {{baseUrl}}/teacher/3
Authorization: Bearer {{token}}

{
  "name": "Jindřich",
  "lastname": "ZKutnéHory",
  "active": false
}

HTTP 204

PUT {{baseUrl}}/teacher/2
Authorization: Bearer {{token}}

{
    "name": "Jaroslav",
    "lastname": "Uhlíř",
    "active": true
}

HTTP 404

PUT {{baseUrl}}/teacher/3
Authorization: Bearer {{token}}

{
  "name": "Jindřich",
  "lastname": "ZeSkalice",
  "active": true
}

HTTP 204

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jindra",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

PUT {{baseUrl}}/teacher/3
Authorization: Bearer {{token}}

{
  "name": "Jenda",
  "lastname": "Rohlíček",
  "active": true
}

HTTP 403

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jenda",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

PUT {{baseUrl}}/teacher/3
Authorization: Bearer {{token}}

{
  "name": "Jenda",
  "lastname": "Rohlíček",
  "active": false
}

HTTP 403