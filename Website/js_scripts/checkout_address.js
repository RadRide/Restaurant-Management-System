

// hay l function to display addresses as options in select tag
// Modify the displayAddresses function to handle both address and name
// Modify the displayAddresses function to create radio buttons
function displayAddresses(addresses) {
    const addressContainer = document.getElementById('address-radio-buttons');
    addressContainer.innerHTML = '';

    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    if (cart.length === 0) {
        // 2eza l cart empty do not display: confirm btn, addresses, clear btn
        document.getElementById('addresses').style.display = 'none';
        document.getElementById("confirm-order-btn").style.display = 'none';
        document.getElementById("cart-clear-btn").style.display = 'none';
        document.getElementById("all-address-container").style.display = 'none';
        // Exit the function if the cart is empty
        return;
    } else {
        document.getElementById('addresses').style.display = 'block';
    }

    if (addresses.length === 0) {
        addressContainer.textContent = 'No addresses saved';
    } else {
        addresses.forEach((address, index) => {
            const radioContainer = document.createElement('div');
            radioContainer.classList.add('address-radio');

            const radioInput = document.createElement('input');
            radioInput.classList.add('radioInput');
            radioInput.type = 'radio';
            radioInput.name = 'address';
            radioInput.value = address.address;
            radioInput.id = `address-${index}`;

            const radioLabel = document.createElement('label');
            radioLabel.htmlFor = `address-${index}`;
            // radioLabel.textContent = `${address.name}: ${address.address}`;

            // display name and address
            radioLabel.innerHTML = `
            <div>
                <span class="checkout-address-name">
                    ${address.name}
                </span>
                <div class="checkout-address">
                    ${address.address}
                </div>
            </div>
            `;

            radioContainer.appendChild(radioInput);
            radioContainer.appendChild(radioLabel);
            addressContainer.appendChild(radioContainer);
        });
    }
}



function getAddresses() {
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                const addresses = JSON.parse(xhr.responseText);
                displayAddresses(addresses);
            } else {
                console.error('Error fetching addresses:', xhr.status);
            }
        }
    };
    xhr.open('GET', 'get_address_checkout.php', true);
    xhr.send();
}


getAddresses();
