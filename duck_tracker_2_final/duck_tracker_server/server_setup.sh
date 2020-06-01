#! /bin/bash


. duck_tracker.ini

mysql -p -e "grant all privileges on *.* to '$dbuser'@'%' identified by '$password' ;" 

mysql -u"$dbuser" -p"$password" -e "
CREATE DATABASE $database;
show databases;
use $database;

CREATE TABLE $table (
ID VARCHAR(255),
time VARCHAR(255),
latitude VARCHAR(255),
longitude VARCHAR(255),
tal VARCHAR(255),
date VARCHAR(255)
);
SHOW TABLES;" 


# mysql 