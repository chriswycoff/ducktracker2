
<?php

$homedir = "/home/users/cwycoff";

// Write out a valid HTML header
print <<<end_of_header
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang='en'><head>
<meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>
<title>Example of MySQL with PHP</title>
</head>
end_of_header
;

// Parse the config file for this app; this one in the current directory
$conf = @parse_ini_file("$homedir/etc/myapp.conf");
$debug = $conf['debug'];

// student users can use unix domain socket to connect
$location = ":$homedir/mysql-data/mysqld_sock";

// if for some reason your database is on a different server, parse .my.cnf
$mysql_conf = parse_ini_string(preg_replace("/^#.*$/m","", file_get_contents("$homedir/.my.cnf")), TRUE);
$location = $mysql_conf['client']['host'].":".$mysql_conf['client']['port'];

//print $conf['dbuser'] . $location . $conf['password'];
// ^^ this show 'right' stuff

// connect to the database server
$conn = mysql_connect($location, $conf['dbuser'], $conf['password'])
        or die ("Could not connect MySQL");
if ($debug) echo "Connection to <b>$location</b> successful.<br />";

// select the database
$selected_db=mysql_select_db($conf['database'], $conn)
        or die ("Could not open database");
if ($debug) echo "Database <b>$database</b> selected.<br />";

// Now you are ready to use mysql_query() and mysql_fetch()

mysql_close($conn);
print "</body></html>";

?>
