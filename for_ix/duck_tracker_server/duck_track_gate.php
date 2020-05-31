
<?php
/// recieve post request /////

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

/// BEGIN CONNECTION //////

$con=mysqli_connect("ix-dev:3267","group6user","hello1","my_test_db");

if (mysqli_connect_errno()) {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$result = mysqli_query($con,"SELECT * FROM names");

while($row = mysqli_fetch_array($result)) {
	$a_name = $row['first_name'];
	//echo $a_name;
}

//echo "<br>";

/// ^^^ some tests from dummy mysql table ///// 

/// begin inserting
/// check if set  then continue

if (!empty($id)){

	$sql = "INSERT INTO gsdata_strings (ID, time, latitude, longitude, tal, date)
	VALUES ('$id', '$time', '$latitude', '$longitude', '$tal', '$date')";

	if ($con->query($sql) === TRUE) {
	  echo "New record created successfully";
	} else {
	  echo "Error: " . $sql . "<br>" . $con->error;
	}

	//// end inserting 
	echo "<br>";

	//// after insert
}
else{
	//echo "TESTING MODE";
	//echo "<br>";
}


if (empty($id)){ 
	$result = mysqli_query($con,"SELECT * FROM gsdata_strings");

	$array_for_jason = array();
	$handle = fopen("file.txt", "w");
	while($row = mysqli_fetch_array($result)) {
		$array_for_jason[] = $row;
		$a_ID = $row['ID'];
		$date = $row['date'];
		$time = $row['time'];
		$latitude = $row['latitude'];
		$longitude = $row['longitude'];
		$tal = $row['tal'];
		fwrite($handle, $a_ID ."\t". $date ."\t". $time ."\t". $latitude ."\t". $longitude ."\t". $tal ."\n" );
		//echo $a_ID . $date . $time . $latitude . $longitude;
	}
	//$handle = fopen("file.txt", "w");
	   // fwrite($handle, json_encode($array_for_jason));
	    fclose($handle);

	    header('Content-Type: application/octet-stream');
	    header('Content-Disposition: attachment; filename='.basename('file.txt'));
	    header('Expires: 0');
	    header('Cache-Control: must-revalidate');
	    header('Pragma: public');
	    header('Content-Length: ' . filesize('file.txt'));
	    readfile('file.txt');
	    exit;

	//echo "<br>";

}


?> 

</html>