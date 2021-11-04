#!/bin/bash

echo "<<< Step 1: Build Package >>>"
./mvnw package

echo "<<< Step 2: Make Docker Image >>>"

sudo docker build -t rocketcorner .

echo "<<< Step 3: Heroku Login >>>"
heroku login
sudo heroku container:login

echo "<<< Step 4: Tag Heroku Container >>>"

sudo docker tag rocketcorner registry.heroku.com/rocketcorner/web

echo "<<< Step 5: Upload To Registry and Deploy Heroku App >>>"
sudo docker push registry.heroku.com/rocketcorner/web
heroku container:release web --app=rocketcorner







