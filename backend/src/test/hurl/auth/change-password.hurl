PATCH {{baseUrl}}/auth/password
Content-Type: application/json

{
  "password": "NewPassword-123!"
}
HTTP 401

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jenda",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

PATCH {{baseUrl}}/auth/password
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "password": "abcd"
}
HTTP 400

PATCH {{baseUrl}}/auth/password
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "password": "abcdefghi"
}
HTTP 400

PATCH {{baseUrl}}/auth/password
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "password": "p2%123a"
}
HTTP 204
