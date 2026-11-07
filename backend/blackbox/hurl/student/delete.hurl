POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "kramny",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

POST {{baseUrl}}/student
Authorization: Bearer {{token}}

{
  "name": "pepicek",
  "lastname": "koblizek",
  "class": "8.B",
  "username": "pepicek.koblizek"
}

HTTP 201

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jindra",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

DELETE {{baseUrl}}/student/6
Authorization: Bearer {{token}}

HTTP 403

DELETE {{baseUrl}}/student/7
Authorization: Bearer {{token}}

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

DELETE {{baseUrl}}/student/6
Authorization: Bearer {{token}}

HTTP 403

DELETE {{baseUrl}}/student/7
Authorization: Bearer {{token}}

HTTP 403

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "kramny",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

DELETE {{baseUrl}}/student/6
Authorization: Bearer {{token}}

HTTP 204

DELETE {{baseUrl}}/student/7
Authorization: Bearer {{token}}

HTTP 404