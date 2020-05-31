#! /bin/bash


. duck_tracker.ini

echo "USER" "$dbuser" "PASSWORD" "$password" 

mysql -p -e "grant all privileges on *.* to '$dbuser'@'%' identified by '$password' ;" 


# mysql 