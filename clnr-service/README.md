# clnr-service

A clearing number service to lookup the bank name for the specified clearing number.

## Notes

To be able to start the service you need to specify a profile:
```
-Dapplication.profile=local
```

## Running with curl

When service is up and running you access the service like this:
```
$ curl -i http://localhost:8090/clnr-service/numbers/6761
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sun, 29 Jan 2017 12:42:24 GMT

{"name":"Handelsbanken"}
```

