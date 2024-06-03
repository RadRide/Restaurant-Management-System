<?php
session_start();

if (!isset($_SESSION["customer_id"])) {
    echo "Error: Customer not logged in";
    exit();
}

include 'db_connection.php';
global $conn;

$customer_id = $_SESSION["customer_id"];
$address = $_POST["address"];
$addressName = $_POST["address_name"];

$sql = "INSERT INTO customer_address (customer_id, saved_address, address_name) VALUES (?, ?, ?)";
$stmt = $conn->prepare($sql);
$stmt->bind_param("iss", $customer_id, $address, $addressName);

if ($stmt->execute()) {
    echo "Address saved successfully!";
} else {
    echo "Error: " . $stmt->error;
}

$stmt->close();
$conn->close();

?>
