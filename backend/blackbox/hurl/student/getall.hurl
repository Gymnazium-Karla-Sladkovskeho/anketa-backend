POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "kramny",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

GET {{baseUrl}}/student?classes=3.B,9.A
Authorization: Bearer {{token}}

HTTP 200

[Asserts]
jsonpath "$[*].name" contains "Jenda"
jsonpath "$[*].lastname" contains "Perníček"
jsonpath "$[*].username" contains "jenda"

GET {{baseUrl}}/student?classes=
Authorization: Bearer {{token}}

HTTP 200

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jenda",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

GET {{baseUrl}}/student?classes=3.B,9.A
Authorization: Bearer {{token}}

HTTP 403