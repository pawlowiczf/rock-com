FROM postgres

ENV POSTGRES_USER=admin
ENV POSTGRES_PASSWORD=password2137
ENV POSTGRES_DB=rockcom

COPY rock-com_create.sql /docker-entrypoint-initdb.d/

EXPOSE 5432