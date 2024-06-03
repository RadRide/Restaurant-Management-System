

// this page handles the map load and other map features in the
// address section in user_profile.php
// it also calls (get_address.php, save_address.php, remove_address.php)
// from ajax functions


var map;
var marker;
var geocoder;

function loadMapScenario() {
    map = new Microsoft.Maps.Map(document.getElementById('myMap'), {
        credentials: 'AlQAz5kZ66WhGJMdE-5a59YlBkOJprBTuLITHbETeBPKI8uid1ehw-M4ECrtuUJP',
        center: new Microsoft.Maps.Location(33.88625045015641, 35.507537927246105),
        zoom: 12 // Default zoom level
    });


    Microsoft.Maps.loadModule('Microsoft.Maps.Search', function () {
        geocoder = new Microsoft.Maps.Search.SearchManager(map);
    });

    // 3melna draggable marker
    marker = new Microsoft.Maps.Pushpin(map.getCenter(), {
        color: 'red',
        draggable: true,
    });

    Microsoft.Maps.Events.addHandler(marker, 'dragend', function () {
        showLocationInfo(marker.getLocation());
    });

    Microsoft.Maps.Events.addHandler(map, 'click', function (e) {
        var point = new Microsoft.Maps.Point(e.getX(), e.getY());
        var loc = e.target.tryPixelToLocation(point);
        if (loc) {
            marker.setLocation(loc);
            showLocationInfo(loc);
        }
    });

    map.entities.push(marker);
}

function searchLocation() {
    var query = document.getElementById('searchBox').value;
    if (query === '') {
        alert('Please enter a location.');
        return;
    }

    if (!geocoder) return;

    geocoder.geocode({
        where: query,
        callback: function (r) {
            if (r && r.results && r.results.length > 0) {
                var loc = r.results[0].location;
                map.setView({ center: loc, zoom: 12 });
                marker.setLocation(loc);
                showLocationInfo(loc);
            } else {
                alert('Location not found.');
            }
        },
        errorCallback: function (e) {
            alert('Error searching for location: ' + e.message);
        }
    });
}

function showLocationInfo(loc) {
    var coordinates = 'Latitude: ' + loc.latitude + ', Longitude: ' + loc.longitude;
    document.getElementById('locationInfo').innerHTML = coordinates;

    if (!geocoder) return;

    const latitude = loc.latitude;
    const longitude = loc.longitude;

    if (!latitude || !longitude) {
        alert('Please enter both latitude and longitude values.');
        return;
    }

    // he my api key
    const apiKey = 'AlQAz5kZ66WhGJMdE-5a59YlBkOJprBTuLITHbETeBPKI8uid1ehw-M4ECrtuUJP';
    const url = `https://dev.virtualearth.net/REST/v1/Locations/${latitude},${longitude}?key=${apiKey}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            if (data.resourceSets.length > 0 && data.resourceSets[0].resources.length > 0) {
                const address = data.resourceSets[0].resources[0].address.formattedAddress;
                document.getElementById('geocodeResult').textContent = address;
            } else {
                document.getElementById('geocodeResult').textContent = 'Geocode conversion failed.';
            }
        })
        .catch(error => {
            document.getElementById('geocodeResult').textContent = 'Error fetching geocode: ' + error.message;
        });
}






// addresses



// function to save the address
$("#saveAddressBtn").click(function() {
    var address = $("#geocodeResult").text();
    var addressName = $("#address-name-input").val();

    if (address.trim() === "") {
        // alert("Address is required!");
        Swal.fire({
            title: 'Address is Missing!',
            icon: 'error',
            showConfirmButton: false,
            timer: 2000,
        }).then((result) => {
            if (result.isConfirmed) {

            }
        });
        return;
    }

    if (addressName.trim() === "") {
        // alert("Address name is required!");
        Swal.fire({
            title: 'Address Name Missing!',
            icon: 'error',
            showConfirmButton: false,
            timer: 2000,
        }).then((result) => {
            if (result.isConfirmed) {

            }
        });

        return;
    }

    console.log("Address:", address);
    console.log("Address Name:", addressName);

    $.ajax({
        type: "POST",
        url: "save_address.php",
        data: { address: address, address_name: addressName },
        success: function(response) {
            // alert("Address saved successfully!");
            Swal.fire({
                title: 'Address Saved!',
                icon: 'success',
                showConfirmButton: false,
                timer: 2000,
            }).then((result) => {
                if (result.isConfirmed) {

                }
            });
            loadSavedAddresses();
            // clear l name input field
            $("#address-name-input").val("");
        },
        error: function(xhr, status, error) {
            console.error(xhr.responseText);
        }
    });
});



// hy l fct to load saved addresses
function loadSavedAddresses() {
    $.ajax({
        type: "GET",
        url: "get_addresses.php",
        success: function(response) {
            $("#savedAddresses").html(response);
        },
        error: function(xhr, status, error) {

            console.error(xhr.responseText);
        }
    });
}

// lload l saved addresses on page load
loadSavedAddresses();



// remove address button click
$(document).on("click", ".removeAddressBtn", function() {
    var addressToRemove = $(this).data("address");
    $.ajax({
        type: "POST",
        url: "remove_address.php",
        data: { address: addressToRemove },
        success: function(response) {
            // alert("Address removed successfully!");

            loadSavedAddresses();
        },
        error: function(xhr, status, error) {
            console.error(xhr.responseText);
        }
    });
});




let mapWrapper = document.getElementById('mapWrapper');
function showTheMap(){
    mapWrapper.style.display = 'block';
}
function closeTheMap(){
    mapWrapper.style.display = 'none';
}
