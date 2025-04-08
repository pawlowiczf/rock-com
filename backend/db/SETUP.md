## Rockcom database

---

A guide on how to set up container with postgresql database.

All instructions assume working directory to be the parent directory of current markdown file. 
With that in mind, you should adjust commands with relative paths in them (or change directory). 

## Prerequisites
- Docker
- Docker desktop running

## How to set up
- Run `docker-compose -f db/docker-compose.yml up --build -d`

Now, you can run it via docker desktop. 

Keep in mind, if database schema has been changed, you will need to run above command again.