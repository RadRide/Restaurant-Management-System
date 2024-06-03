
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css_styles/login_page.css">
    <title>Login</title>

    <!-- Sweet Alert   -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

</head>

<body>

<div class="everything-container">

    <div class="account-container" id="account-container">

        <div class="form-container sign-up">


            <form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>">

                <h1>Create Account</h1>
                <p>Use your email for registration</p>

                <input type="text" name="customer_name" minlength="8" placeholder="Name" required><br>
                <input type="email" name="customer_email" placeholder="Email" required><br>
                <input type="password" name="customer_password" minlength="8" placeholder="Password" required><br>
                <input type="text" name="customer_phone" minlength="8" placeholder="Phone" required><br>

                <input type="submit" name="create_account" value="Create Account">
            </form>

        </div>

        <div class="form-container log-in">

            <form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>">

                <h1>Log In</h1>
                <p>Use your email and password</p>

                <input type="email" name="customer_email"  placeholder="Email" required><br>
                <input type="password" name="customer_password" placeholder="Password" required><br>

                <input type="submit" name="login" value="Login">
            </form>

        </div>



        <div class="toggle-container">
            <div class="toggle">
                <div class="toggle-panel toggle-left">
                    <h1>Already have an Account?</h1>
                    <p>Login to place your orders!</p>
                    <button class="hidden" id="toggler-login">Login</button>
                </div>

                <div class="toggle-panel toggle-right">
                    <h1>Don't have an Account?</h1>
                    <p>Create one to benefit from our deals!</p>
                    <button class="hidden" id="toggler-create-account">Create Account</button>
                </div>
            </div>
        </div>


    </div>


</div>



<script src="js_scripts/login_page.js"></script>
</body>

</html>


<?php
include 'db_connection.php';
global $conn;

session_start();

// haw are used to sanitize data entered, for example removing any characters
// (w kamen to prevent sql injection)
function sanitizeInput($data) {
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    return $data;
}


// check if the form is submitted lal "create account"
if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST["create_account"])) {

    $customer_name = sanitizeInput($_POST["customer_name"]);
    $customer_email = sanitizeInput($_POST["customer_email"]);
    $customer_password = sanitizeInput($_POST["customer_password"]);
    $customer_phone = sanitizeInput($_POST["customer_phone"]);

    // Check if the email already exists in the database
    $stmt_check_email = $conn->prepare("SELECT customer_id FROM customer WHERE customer_email = ?");
    $stmt_check_email->bind_param("s", $customer_email);
    $stmt_check_email->execute();
    $result_check_email = $stmt_check_email->get_result();

    if ($result_check_email->num_rows > 0) {
        // Email already exists, display error message
        echo '
            <script>
                Swal.fire({
                  position: "center",
                  icon: "error",
                  title: "Email Already Exists!",
                  showConfirmButton: false,
                  timer: 2000
                });
            </script>
        ';
    } else {
        // Email doesn't exist, proceed with account creation

        $hashed_password = password_hash($customer_password, PASSWORD_DEFAULT);

        $stmt_insert = $conn->prepare("INSERT INTO customer (customer_name, customer_email, customer_password, customer_phone) VALUES (?, ?, ?, ?)");
        $stmt_insert->bind_param("ssss", $customer_name, $customer_email, $hashed_password, $customer_phone);

        if ($stmt_insert->execute()) {
            echo '
                <script>
                    Swal.fire({
                      position: "center",
                      icon: "success",
                      title: "Account Created Successfully!",
                      showConfirmButton: false,
                      timer: 2000
                    });
                </script>
            ';
        } else {
            echo "Error creating account: " . $conn->error;
        }

        $stmt_insert->close();
    }

    $stmt_check_email->close();
    $conn->close();
}








// check if the form is submitted lal "login"
if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST["login"])) {


    $customer_email = sanitizeInput($_POST["customer_email"]);
    $customer_password = sanitizeInput($_POST["customer_password"]);


    $stmt = $conn->prepare("SELECT * FROM customer WHERE customer_email = ?");
    $stmt->bind_param("s", $customer_email);


    $stmt->execute();
    $result = $stmt->get_result();


    if ($result->num_rows == 1) {
        $row = $result->fetch_assoc();
        if (password_verify($customer_password, $row["customer_password"])) {

            $_SESSION["customer_id"] = $row["customer_id"];
            $_SESSION["customer_email"] = $customer_email;

//            echo "Login successful";
            echo '
                <script>
                        
                Swal.fire({
                  position: "center",
                  icon: "success",
                  title: "Login Successful!",
                  showConfirmButton: false,
                  timer: 2000
                });

                // go to menu after 3 seconds of successful login
                setTimeout(function() {
                    window.location.href = "menu.php";
                }, 3000);

                </script>
            ';

        } else {
//            echo "Incorrect password";
            echo '
                <script>
                        
                Swal.fire({
                  position: "center",
                  icon: "error",
                  title: "Incorrect Password!",
                  showConfirmButton: false,
                  timer: 2000
                });
                </script>
        ';

        }
    } else {
//        echo "User not found";
        echo '
            <script>
                    
            Swal.fire({
              position: "center",
              icon: "error",
              title: "User Not Found!",
              showConfirmButton: false,
              timer: 2000
            });
            </script>
        ';

    }


    $stmt->close();
    $conn->close();
}



// sign out
if (isset($_POST["sign_out"])) {
    $_SESSION = array();

        session_destroy();
}

?>


