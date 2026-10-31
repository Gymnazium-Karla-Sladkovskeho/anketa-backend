POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "kramny",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

GET {{baseUrl}}/class
Authorization: Bearer {{token}}
HTTP 200
[Asserts]
jsonpath "$[*].name" contains "3.B"
jsonpath "$[*].name" contains "9.A"
