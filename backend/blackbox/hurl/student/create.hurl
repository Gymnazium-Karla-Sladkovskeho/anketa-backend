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
  "lastname": "jenicek",
  "class": "3.B",
  "username": "pepicek.jenicek"
}

HTTP 201

POST {{baseUrl}}/student
Authorization: Bearer {{token}}

{
  "name": "Alois",
  "lastname": "Durdič",
  "class": "3.A",
  "username": "alois.durdic"
}

HTTP 201

POST {{baseUrl}}/student
Authorization: Bearer {{token}}

{
  "name": "Oldřich",
  "lastname": "Kramný",
  "class": "3.B",
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

POST {{baseUrl}}/student
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

POST {{baseUrl}}/student
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

DELETE {{baseUrl}}/student/4
Authorization: Bearer {{token}}

HTTP 204

DELETE {{baseUrl}}/student/5
Authorization: Bearer {{token}}

HTTP 204