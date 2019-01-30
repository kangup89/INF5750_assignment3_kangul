function getStudentData() {

	// This must be implemented by you. The json variable should be fetched
	// from the server, not initiated with a static value as below. 
	// You must first download the student json data from the server
	// then call populateStudentTable(json);
	// and then populateStudentLocationForm(json);
	/*var json =
			[ { "courses" : [ { "courseCode" : "FAKE5750",
		          "id" : 1,
		          "name" : "Fake data"
		        },
		        { "courseCode" : "INF5761",
		          "id" : 2,
		          "name" : "Fake data"
		        }
		      ],
		    "degrees" : [  ],
		    "id" : 1,
		    "latitude" : null,
		    "longitude" : null,
		    "name" : "John McFake"
		  },
		  { "courses" : [ { "courseCode" : "FAKE5750",
		          "id" : 1,
		          "name" : "Fake data"
		        },
		        { "courseCode" : "INF5761",
		          "id" : 2,
		          "name" : "Fake data"
		        }
		      ],
		    "degrees" : [  ],
		    "id" : 2,
		    "latitude" : null,
		    "longitude" : null,
		    "name" : "Jane Faka"
		  }
		];*/
	var json = $.ajax({
				type: "GET",
				url: "/assignment2-gui/api/student.json",
	})
	.fail(function(){
		alert("error1");
	})
	
	.done(function(json){
		populateStudentTable(json);
		populateStudentLocationForm(json);
		$("#myDiv").html(json);
	});


}

var options = {
		enableHighAccuracy: true,
		timeout: 5000,
		maximumAge: 0
		};

// This function gets called when you press the Set Location button
function get_location() {
	if (Modernizr.geolocation) {
		// Find location... fill in. 
		navigator.geolocation.getCurrentPosition(location_found, error, options);
	} else {
		x.innerHTML = "Geolocation is not supported by this browser.";
	}
}

// Call this function when you've succesfully obtained the location. 
function location_found(position){ 
	// Extract latitude and longitude and save on the server using an AJAX call. 
	// When you've updated the location, call populateStudentTable(json); again
	// to put the new location next to the student on the page. . 
	var latitude = position.coords.latitude;
	var longitude = position.coords.longitude;	
	var student = document.getElementById("selectedStudent").value;
	
	var dataset = {"longitude":longitude, "latitude":latitude};
	var url_location = "/assignment2-gui/api/student/" + student + "/location.json";
	
	$.ajax({
		type:"POST",
		url:url_location,
		contentType:"application/json;charset=UTF-8",
		dataType: "json",
		data : dataset,
		success: function(data){
			populateStudentTable(data);
		},
		error: function(){
			alert("error2");
		}		
	});
}

function error () {x.innerHTML = "location_found error";}

var map;
function initialize_map() {
        var mapOptions = {
                zoom : 10,
                mapTypeId : google.maps.MapTypeId.ROADMAP
        };
        map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
        // Try HTML5 geolocation
        if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function(position) {
                        var pos = new google.maps.LatLng(position.coords.latitude,
                                        position.coords.longitude);
                        map.setCenter(pos);
                }, function() {
                        handleNoGeolocation(true);
                });
        } else {
                // Browser doesn't support Geolocation
                // Should really tell the user…
        }
}



// No need to change javascript below this line, unless you want to...

function populateStudentTable(json) {

	$('#studentTable').empty();

	for (var s = 0; s < json.length; s++) {
		var student = json[s];
		student = explodeJSON(student);
		var tableString = "<tr>";
		console.log('Student');
		console.log(student);
		// Name
		tableString += "<td>" + student.name + "</td>";

		// Courses
		tableString += "<td>";
		for (var c = 0; c < student.courses.length; c++) {
			var course = student.courses[c];
			course = explodeJSON(course);
			tableString += course.courseCode + ' ';
			/*
			 * tableString += '<a href="/assignment2-gui/student/' + student.id +
			 * '/unenrollcourse/' + course.id + '"><img
			 * src="/assignment2-gui/images/Button-Delete-icon.png"></a>';
			 */
		}
		tableString += '</td>';

		// Location
		if (student.latitude != null && student.longitude != null) {
			tableString += '<td>' + student.latitude + ' ' + student.longitude
					+ '</td>';
			var myLatlng = new google.maps.LatLng(student.latitude, student.longitude);                        
			var marker = new google.maps.Marker({
				position: myLatlng,
				map: map,
				title: student.name
			});
		} else {
			tableString += '<td>No location</td>';
		}

		tableString += '</tr>';
		$('#studentTable').append(tableString);
	}

}

function populateStudentLocationForm(json) {
	var formString = '<tr><td><select id="selectedStudent" name="students">';
	for (var s = 0; s < json.length; s++) {
		var student = json[s];
		student = explodeJSON(student);
		formString += '<option value="' + student.id + '">' + student.name
				+ '</option>';
	}
	formString += '</select></td></tr>';
	// += '<tr><td><input class="btn btn-primary" type="submit" value="Set
	// location"></td></tr>';
	$('#studentLocationTable').append(formString);
}

$('#locationbtn').on('click', function(e) {
	e.preventDefault();
	get_location();
});


var objectStorage = new Object();

function explodeJSON(object) {
	if (object instanceof Object == true) {
		objectStorage[object['@id']] = object;
		console.log('Object is object');
	} else {
		console.log('Object is not object');
		object = objectStorage[object];
		console.log(object);
	}
	console.log(object);
	return object;
}
