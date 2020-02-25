#!/bin/sh

## build the jar ##
gradle bootJar

## add sscon to service ##
echo y | cp ./luckinx.service /etc/systemd/system/
systemctl daemon-reload
systemctl enable luckinx
#systemctl start luckinx
systemctl restart luckinx

## add config to nginx ##
echo y | cp ./luckinx.nginx.http.conf /etc/nginx/conf.d/
systemctl restart nginx ## please make sure nginx installed ##
