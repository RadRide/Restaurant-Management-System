<?php

include 'db_connection.php';
global $conn;


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// get the last delivery_fee from the delivery_fee table
$sql = "SELECT fee FROM delivery_fee ORDER BY id DESC LIMIT 1";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $delivery_fee = $row["fee"];
} else {
    // IN CASE no delivery fee were found, take 4 as a default delivery fee
    $delivery_fee = 4;
}

$conn->close();

echo json_encode($delivery_fee);
?>
<?php
