

// this script is for the animated switch in the login.php page
// (switching between Login and create account)
// note that the css is used and this script is for adding and removing a css class

const container = document.getElementById('account-container');
const togglerCreateAccount = document.getElementById('toggler-create-account');
const togglerLogin = document.getElementById('toggler-login');

togglerCreateAccount.addEventListener('click', () => {
    container.classList.add("active");

    // document.body.style.background = 'linear-gradient(to right, #640000, white)';
});

togglerLogin.addEventListener('click', () => {
    container.classList.remove("active");

    // document.body.style.background = 'linear-gradient(to left, #640000, white)';
});

