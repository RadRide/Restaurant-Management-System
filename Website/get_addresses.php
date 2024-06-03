<?php
session_start();

if (!isset($_SESSION["customer_id"])) {
    echo "Error: Customer not logged in";
    exit();
}

include 'db_connection.php';
global $conn;

$customer_id = $_SESSION["customer_id"];
$sql = "SELECT address_name ,saved_address FROM customer_address WHERE customer_id = $customer_id";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        echo '<ul>';
            echo '<li class="user-profile-address-container">';
                echo '<div class="user-profile-address-first-row">
                         <span>' .$row["address_name"]. '</span> <button class="removeAddressBtn" data-address="' . $row["saved_address"] . '">Delete</button> 
                    </div>';
                echo '<div>' .$row["saved_address"]. '</div>';
        echo '</li>';
        echo '</ul>';
//        echo $row["address_name"];
//        echo '<nav>' . $row["saved_address"] . ' <button class="removeAddressBtn" data-address="' . $row["saved_address"] . '">Remove Address</button></nav>';
    }
} else {
    echo "No addresses saved.";
}

$conn->close();
?>
