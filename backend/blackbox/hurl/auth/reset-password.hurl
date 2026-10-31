POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "kramny",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

POST {{baseUrl}}/auth/password/1
Authorization: Bearer {{token}}
HTTP 201
[Captures]
password: jsonpath "$.password"

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jenda",
  "password": "{{password}}"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"
PATCH {{baseUrl}}/auth/password
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "password": "p2%123a"
}
HTTP 204
