FROM mariadb:11.4-rc-jammy

ADD ./create_database.sql /docker-entrypoint-initdb.d

RUN chmod a+r /docker-entrypoint-initdb.d/*

EXPOSE 3306