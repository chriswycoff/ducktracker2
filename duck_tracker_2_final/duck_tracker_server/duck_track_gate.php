
<?php
/// recieve post request /////
// iterate through requests and assign variables to them
foreach($_REQUEST as $key => $value){
	if($key == "ID"){
	$id = $value;
	    
	}
	if($key =="date"){
	    $date = $value;
	    
	}     
	if($key =="time"){
		$time = $value;
     
	}
	if($key =="pass"){
		$pass = $value;
     	
	}
	if($key =="latitude"){
		$latitude = $value;
     
	}
	if($key =="longitude"){
		$longitude = $value;
     
	}
	if($key =="tal"){
		$tal = $value;
     
	}

}//for each

// grab the variables from duck_tracker.ini file //

$my_array = parse_ini_file("duck_tracker.ini");

$the_username = $my_array['dbuser'];

$the_password = $my_array['password'];

$the_database = $my_array['database'];

$the_port = $my_array['port'];

/// BEGIN CONNECTION TO MYSQL DATABASE //////
$con=mysqli_connect($the_port, $the_username, $the_password ,$the_database);

if (mysqli_connect_errno()) {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
}


/// begin inserting
/// check if set  then continue

if (!empty($id)){

	$sql = "INSERT INTO duck_table (ID, time, latitude, longitude, tal, date)
	VALUES ('$id', '$time', '$latitude', '$longitude', '$tal', '$date')";

	if ($con->query($sql) === TRUE) {
	  echo "New record created successfully";
	} else {
	  echo "Error: " . $sql . "<br>" . $con->error;
	}

	//// end inserting 
	//// after insert
}
else{
 // pass
}

// BEGIN logic to create the downloadable data file //
if (empty($id)){ 
	$result = mysqli_query($con,"SELECT * FROM duck_table");

	$array_for_jason = array();
	$handle = fopen("file.txt", "w");
	fwrite($handle, "ID" ."\t". "date" ."\t". "time" ."\t". "latitude"."\t". "longitude" ."\t". "tal" ."\n" );
	while($row = mysqli_fetch_array($result)) {
		$array_for_jason[] = $row;
		$a_ID = $row['ID'];
		$date = $row['date'];
		$time = $row['time'];
		$latitude = $row['latitude'];
		$longitude = $row['longitude'];
		$tal = $row['tal'];
		fwrite($handle, $a_ID ."\t". $date ."\t". $time ."\t". $latitude ."\t". $longitude ."\t". $tal ."\n" );
	}
	
	    fclose($handle);

	    header('Content-Type: application/octet-stream');
	    header('Content-Disposition: attachment; filename='.basename('file.txt'));
	    header('Expires: 0');
	    header('Cache-Control: must-revalidate');
	    header('Pragma: public');
	    header('Content-Length: ' . filesize('file.txt'));
	    readfile('file.txt');
	    exit;

// END logic to create the downloadable data file //

}


?> 

</html>