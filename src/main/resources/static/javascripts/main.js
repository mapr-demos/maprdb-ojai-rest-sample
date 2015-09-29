$(function () {

// create a map in the "map" div, set the view to a given place and zoom
    var map = L.map('map').setView([48.8670, 2.3521], 12);


    L.tileLayer('http://localhost:8080/tiles/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

// add a marker in the given location, attach some popup content to it and open the popup
    L.marker([48.8670, 2.3521]).addTo(map)
        .bindPopup('A pretty CSS3 popup. <br> Easily customizable.')
        .openPopup();
});
