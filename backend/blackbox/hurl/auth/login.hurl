POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jenda",
  "password": "p2%123a"
}
HTTP 201
[Asserts]
jsonpath "$.access_token" exists
jsonpath "$.token_type" == "Bearer"
jsonpath "$.expires_in" exists

POST {{baseUrl}}/auth
Content-Type: application/json

{
  "username": "jenda",
  "password": "wrong-password"
}
HTTP 401