<?php

session_start();

if (!isset($_SESSION["customer_id"])) {
    echo "Error: Customer not logged in";
    exit();
}

include 'db_connection.php';
global $conn;

$customer_id = $_SESSION["customer_id"];
$addressToRemove = $_POST["address"];

$sql = "DELETE FROM customer_address WHERE customer_id = ? AND saved_address = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("is", $customer_id, $addressToRemove);

if ($stmt->execute()) {
    echo "Address removed successfully!";
} else {
    echo "Error: " . $stmt->error;
}

$stmt->close();
$conn->close();
?>
