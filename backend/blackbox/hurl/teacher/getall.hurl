POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "kramny",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

GET {{baseUrl}}/teacher
Authorization: Bearer {{token}}

HTTP 200

[Asserts]

jsonpath "$.page" == 1
jsonpath "$.total_pages" == 1
jsonpath "$.page_size" == 25
jsonpath "$.payload[*].id" == 3
jsonpath "$.payload[*].name" contains "Jindřich"
jsonpath "$.payload[*].lastname" contains "ZeSkalice"
jsonpath "$.payload[*].active" == true
jsonpath "$.payload[*].username" contains "jindra"

GET {{baseUrl}}/teacher?page=1&pageSize=20
Authorization: Bearer {{token}}

HTTP 200

[Asserts]

jsonpath "$.page" == 1
jsonpath "$.total_pages" == 1
jsonpath "$.page_size" == 20

GET {{baseUrl}}/teacher?page=2
Authorization: Bearer {{token}}

HTTP 200

[Asserts]
jsonpath "$.page" == 2
jsonpath "$.total_pages" == 1
jsonpath "$.page_size" == 25

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jenda",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

GET {{baseUrl}}/teacher
Authorization: Bearer {{token}}

HTTP 403