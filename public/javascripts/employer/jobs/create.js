$(document).ready(function() {
    CKEDITOR.replace('description');

    var input = document.getElementById('location')
    var autocomplete = new google.maps.places.Autocomplete(input);
    autocomplete.addListener('place_changed', function() {
        var place = autocomplete.getPlace();
        var geometry = place.geometry.location;
        $("input[name=lat]").val(geometry.lat())
        $("input[name=lng]").val(geometry.lng())
    });

});