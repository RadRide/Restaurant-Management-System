<?php
session_start();


if (!isset($_SESSION["customer_id"])) {
    // redirect user to login page if not logged in
    header("Location: login_page.php");
    exit();
}


include 'db_connection.php';
global $conn;


$customer_id = $_SESSION["customer_id"];

// check if dish ID is provided and valid
if (isset($_POST['dish_id']) && is_numeric($_POST['dish_id'])) {
    $dish_id = $_POST['dish_id'];

    // remove the dish from favorites
    $sql = "DELETE FROM customer_favorites WHERE customer_id = $customer_id AND dish_id = $dish_id";

    if ($conn->query($sql) === TRUE) {
        echo "success";
    } else {
        echo "error";
    }
} else {
    echo "error";
}


$conn->close();
?>
