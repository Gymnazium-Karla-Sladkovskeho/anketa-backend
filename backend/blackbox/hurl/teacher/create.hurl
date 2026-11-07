POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "kramny",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

POST {{baseUrl}}/teacher
Authorization: Bearer {{token}}

{
  "name": "pepicek",
  "lastname": "jenicek",
  "username": "pepicek.jenicek"
}

HTTP 201

POST {{baseUrl}}/teacher
Authorization: Bearer {{token}}

{
  "name": "Oldřich",
  "lastname": "Kramný",
  "username": "kramny"
}

HTTP 400

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jenda",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

POST {{baseUrl}}/teacher
Authorization: Bearer {{token}}

{
  "name": "Jakub",
  "lastname": "Pokorný",
  "class": "3.A",
  "username": "jakub.pokorny"
}

HTTP 403

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jindra",
  "password": "p2%123a"
}
HTTP 201
[Captures]
token: jsonpath "$.access_token"

POST {{baseUrl}}/teacher
Authorization: Bearer {{token}}

{
  "name": "Jakub",
  "lastname": "Pokorný",
  "class": "3.A",
  "username": "jakub.pokorny2"
}

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

DELETE {{baseUrl}}/teacher/7
Authorization: Bearer {{token}}

HTTP 204