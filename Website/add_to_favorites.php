<?php

include 'db_connection.php';
global $conn;

session_start();

// Check 2eza l customer is logged in
if(isset($_SESSION["customer_id"])) {

    // Jib customer ID from session
    $customer_id = $_SESSION["customer_id"];

    // bt jib dish ID from POST request
    $dish_id = $_POST["dish_id"];

    // bta3mol check if the dish is already in favorites
    $check_stmt = $conn->prepare("SELECT * FROM customer_favorites WHERE customer_id = ? AND dish_id = ?");
    $check_stmt->bind_param("ii", $customer_id, $dish_id);
    $check_stmt->execute();
    $check_result = $check_stmt->get_result();

    if ($check_result->num_rows > 0) {
        echo "Item is already in the favorites";
    } else {
        // insert l dish into favorites
        $insert_stmt = $conn->prepare("INSERT INTO customer_favorites (customer_id, dish_id) VALUES (?, ?)");
        $insert_stmt->bind_param("ii", $customer_id, $dish_id);

        if ($insert_stmt->execute()) {
            echo "Item added to favorites successfully!";
        } else {
            echo "Error adding item to favorites: " . $conn->error;
        }

        $insert_stmt->close();
    }

    $check_stmt->close();
    $conn->close();
} else {
    echo "You are not logged in";
}
?>
