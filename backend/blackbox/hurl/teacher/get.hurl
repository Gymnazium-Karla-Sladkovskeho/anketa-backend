POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "kramny",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

GET {{baseUrl}}/teacher/3
Authorization: Bearer {{token}}

HTTP 200

[Asserts]
jsonpath "$.id" == 3
jsonpath "$.name" contains "Jindřich"
jsonpath "$.lastname" contains "ZeSkalice"
jsonpath "$.active" == true
jsonpath "$.username" contains "jindra"

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jindra",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

GET {{baseUrl}}/teacher/3
Authorization: Bearer {{token}}

HTTP 200

GET {{baseUrl}}/teacher/2
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

GET {{baseUrl}}/teacher/2
Authorization: Bearer {{token}}

HTTP 403

GET {{baseUrl}}/teacher/3
Authorization: Bearer {{token}}

HTTP 403

