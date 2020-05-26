

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

}//for each




/// BEGIN CONNECTION //////

$con=mysqli_connect("ix-dev:3267","group6user","hello1","my_test_db");

if (mysqli_connect_errno()) {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$result = mysqli_query($con,"SELECT * FROM names");

while($row = mysqli_fetch_array($result)) {
	$a_name = $row['first_name'];
	echo $a_name;
}

/// begin inserting

$sql = "INSERT INTO gsdata (ID, date, time, latitude, longitude)
VALUES ('$id', '$date', '$time', 22.22254, 25.21265)";

if ($con->query($sql) === TRUE) {
  echo "New record created successfully";
} else {
  echo "Error: " . $sql . "<br>" . $con->error;
}

//// end inserting 
echo "<br>";

//// after insert


$result = mysqli_query($con,"SELECT * FROM gsdata");

while($row = mysqli_fetch_array($result)) {
	$a_ID = $row['ID'];
	$date = $row['date'];
	$time = $row['time'];
	$latitude = $row['latitude'];
	$longitude = $row['longitude'];
	echo $a_ID . $date . $time . $latitude . $longitude;
}

echo "<br>";

$unit = "not_set";

echo "Hello World<br>"; 

foreach($_REQUEST as $key => $value){

	if($key =="message"){
		$message = $value;
	}
	if($key =="pass"){
		$pass = $value;
	}
	
}//for each

echo "the message: $message <br>";
echo "the pass: $pass <br>";

?> 

</html>