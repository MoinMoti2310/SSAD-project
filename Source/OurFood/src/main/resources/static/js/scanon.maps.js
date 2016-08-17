/*
 * Scanon Maps
 * asId = Action Set ID
 * showInfoWindow = Display Info Window on load
 */

loadMaps = function(asId, showInfoWindow) {

	var centralLoc = {
		lat : 23.70,
		lng : 79.30
	};

	var map = new google.maps.Map(document.getElementById('map'), {
		zoom : 4,
		center : centralLoc,
		scrollwheel : false
	});

	// Fetch markers from analytics service
	$.ajax({
		method : "GET",
		url : "/analytics/fetch-geo-locs/" + asId,
		dataType : "json",
		success : function(result) {

			if (result == null) {
				return;
			}

			// Create Markers
			for (var i = 0; i < result.length; i++) {

				var geoLocData = result[i].location;
				var scanCount = result[i].scanCount;

				var loc = new google.maps.LatLng(geoLocData.latitude,
						geoLocData.longitude);

				var marker = new google.maps.Marker({
					position : loc,
					map : map,
					title : geoLocData.city
				});

				var contentString = '<div id="content">'
						+ '<div id="siteNotice">' + '</div>'
						+ '<h3 id="firstHeading" class="firstHeading">'
						+ geoLocData.city + '</h3>' + '<div id="bodyContent">'
						+ '<p>Total Scans: ' + scanCount + ' </p>' + '</div>'
						+ '</div>';

				var infowindow = new google.maps.InfoWindow();

				// Open info window on click of marker
				google.maps.event.addListener(marker, 'click', (function(
						marker, contentString, infowindow) {
					return function() {
						infowindow.setContent(contentString);
						infowindow.open(map, marker);
					};
				})(marker, contentString, infowindow));

				if (showInfoWindow) {
					// Show info window open on map load
					infowindow.setContent(contentString);
					infowindow.open(map, marker);
				}
			}
		}
	});
}