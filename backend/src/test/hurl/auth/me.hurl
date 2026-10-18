POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jenda",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

GET {{baseUrl}}/auth/me
Authorization: Bearer {{token}}
HTTP 200
[Asserts]
jsonpath "$.name" == "Jenda"
jsonpath "$.lastname" == "Perníček"
jsonpath "$.role" contains "STUDENT"

GET {{baseUrl}}/auth/me
HTTP 401