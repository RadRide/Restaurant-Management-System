<?php
session_start();

if (!isset($_SESSION["customer_id"])) {
    echo json_encode(["error" => "Customer not logged in"]);
    exit();
}

include 'db_connection.php';
global $conn;

$customer_id = $_SESSION["customer_id"];
$sql = "SELECT saved_address, address_name FROM customer_address WHERE customer_id = $customer_id";
$result = $conn->query($sql);

$addresses = [];

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $addresses[] = ["address" => $row["saved_address"], "name" => $row["address_name"]];
    }
}

echo json_encode($addresses);

$conn->close();
?>
