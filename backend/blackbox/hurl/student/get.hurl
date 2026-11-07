POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "kramny",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

GET {{baseUrl}}/student/1
Authorization: Bearer {{token}}

HTTP 200

[Asserts]
jsonpath "$.id" == 1
jsonpath "$.name" contains "Jenda"
jsonpath "$.lastname" contains "Perníček"
jsonpath "$.class" contains "3.B"
jsonpath "$.username" contains "jenda"

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jindra",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

GET {{baseUrl}}/student/1
Authorization: Bearer {{token}}

HTTP 200

GET {{baseUrl}}/student/2
Authorization: Bearer {{token}}

HTTP 404

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jenda",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

GET {{baseUrl}}/student/1
Authorization: Bearer {{token}}

HTTP 200

GET {{baseUrl}}/student/2
Authorization: Bearer {{token}}

HTTP 403

