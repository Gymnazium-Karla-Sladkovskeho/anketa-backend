POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "kramny",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

PUT {{baseUrl}}/student/1
Authorization: Bearer {{token}}

{
  "name": "Jenda",
  "lastname": "Rohlíček",
  "class": "3.A"
}

HTTP 204

PUT {{baseUrl}}/student/2
Authorization: Bearer {{token}}

{
  "name": "Jenda",
  "lastname": "Rohlíček",
  "class": "3.A"
}

HTTP 404

PUT {{baseUrl}}/student/1
Authorization: Bearer {{token}}

{
  "name": "Jenda",
  "lastname": "Perníček",
  "class": "3.B"
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

PUT {{baseUrl}}/student/1
Authorization: Bearer {{token}}

{
  "name": "Jenda",
  "lastname": "Rohlíček",
  "class": "3.A"
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

PUT {{baseUrl}}/student/1
Authorization: Bearer {{token}}

{
  "name": "Jenda",
  "lastname": "Rohlíček",
  "class": "3.A"
}

HTTP 403