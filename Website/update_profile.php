<?php

// this page is to handle the ajax code for updating the account
// settings (name and phone) in the user_profile.php

session_start();


include 'db_connection.php';
global $conn;


if (!isset($_SESSION["customer_id"])) {
    // redirect user to login page if not logged in
    header("Location: login_page.php");
    exit();
}


$customer_id = $_SESSION["customer_id"];


if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST["customer_name"]) && isset($_POST["customer_phone"])) {

    $customer_name = $_POST["customer_name"];
    $customer_phone = $_POST["customer_phone"];


    $stmt = $conn->prepare("UPDATE customer SET customer_name = ?, customer_phone = ? WHERE customer_id = ?");
    $stmt->bind_param("ssi", $customer_name, $customer_phone, $customer_id);


    if ($stmt->execute()) {

        echo '<script>
                Swal.fire({
                  title: "Account Changes Saved",
                  icon: "success",
                  showConfirmButton: false,
                  timer: 1500
                });
              </script>';
    } else {

        echo '<p>Error updating profile: ' . $conn->error . '</p>';
    }


    $stmt->close();
}


$conn->close();
?>
