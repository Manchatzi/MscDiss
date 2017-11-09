		var x;
		var y;
		var mymap;
		var curLayer;
		
		function loadDoc(str) {
		  var xhttp = new XMLHttpRequest();
		  xhttp.onreadystatechange = function() {
			if (xhttp.readyState == 4 && xhttp.status == 200) {
			  if (str=="fileman.xml"){
				myFunction2(xhttp);
			  }
			  else if (str=="info.xml"){
				myFunction1(xhttp);
			  }
			}
		  };
		  xhttp.open("GET", str, true);
		  xhttp.send();
		}

		
		function myFunction1(xml) {
			var xmlDoc1 = xml.responseXML;
			y = xmlDoc1.getElementsByTagName("image");
			
			loadImgList();
		}
		
		function loadImgList(){
			document.getElementById('imglist').innerHTML = "";
			
			var container = document.createElement('div');
			container.id = "wrapper";
			var layout = document.getElementById('imglist');
			layout.appendChild(container);
			
			var html = '<ul>';
			for(i=0; i<y.length; i++){
				var url = (y[i].getElementsByTagName("url")[0].childNodes[0].nodeValue).replace("Size4","Size0");
				
				html+= "<li><img src=\""+url+"\" width=\"100\" height=\"100\" border=\"1\"></img>"+
				"<div id=\"ttl\"><p>"+y[i].getElementsByTagName("title1")[0].childNodes[0].nodeValue+"</p></div>"+
				"<div id=\"but\"><button id=\""+i+"\" onclick=\"details(this.id)\">Details\></button></div></li>";
			}
			html+= '</ul>';

			container.innerHTML = html;
			document.getElementById('display').innerHTML = "<center>Displaying <span style=\"color: #ff0000\">"+y.length+"</span> results</center>";
		}
		
		function loadNullImg(){
			document.getElementById('imglist').innerHTML = "";
			
			var container = document.createElement('div');
			container.id = "wrapper";
			var layout = document.getElementById('imglist');
			layout.appendChild(container);

			var counter=0;
			var html = '<ul>';
			for(i=0; i<x.length; i++){
				if (x[i].getElementsByTagName("latitude")[0].childNodes[0].nodeValue == 10.0 && x[i].getElementsByTagName("longitude")[0].childNodes[0].nodeValue == 10.0){
					counter++;
					id = x[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
					html+= "<li><img src=\""+y[id].getElementsByTagName("url")[0].childNodes[0].nodeValue+"\" width=\"100\" height=\"100\" border=\"1\"></img>"+
					"<div id=\"ttl\"><p>"+y[id].getElementsByTagName("title1")[0].childNodes[0].nodeValue+"</p></div>"+
					"<div id=\"but\"><button id=\""+i+"\" onclick=\"details(this.id)\">Details\></button></div></li>";
				}
			}
			html+= '</ul>';

			container.innerHTML = html;
			document.getElementById('display').innerHTML = "<center>Displaying <span style=\"color: #ff0000\">"+counter+"</span> results</center>";
		}
		
		function loadNearImg(array){
			document.getElementById('imglist').innerHTML = "";
			
			var container = document.createElement('div');
			container.id = "wrapper";
			var layout = document.getElementById('imglist');
			layout.appendChild(container);

			var html = '<ul>';
			for(i=0; i<array.length; i++){
				id = array[i];
				html+= "<li><img src=\""+y[id].getElementsByTagName("url")[0].childNodes[0].nodeValue+"\" width=\"100\" height=\"100\" border=\"1\"></img>"+
				"<div id=\"ttl\"><p>"+y[id].getElementsByTagName("title1")[0].childNodes[0].nodeValue+"</p></div>"+
				"<div id=\"but\"><button id=\""+id+"\" onclick=\"details(this.id)\">Details\></button></div></li>";
			}
			html+= '</ul>';

			container.innerHTML = html;
			document.getElementById('display').innerHTML = "<center>Displaying <span style=\"color: #ff0000\">"+array.length+"</span> results</center>";
		}

		function myFunction2(xml) {
			var i;
			var xmlDoc = xml.responseXML;
			x = xmlDoc.getElementsByTagName("entry");

		}

		
		function details(clicked_id){
			var geoResults = "";
			
			mymap.removeLayer(curLayer);
			var newLayer = L.layerGroup([]);
			for(i=0; i<x.length; i++){
				if (x[i].getElementsByTagName("id")[0].childNodes[0].nodeValue == clicked_id){
					var lt = x[i].getElementsByTagName("latitude")[0].childNodes[0].nodeValue;
					var ln = x[i].getElementsByTagName("longitude")[0].childNodes[0].nodeValue;
					var lc = x[i].getElementsByTagName("location")[0].childNodes[0].nodeValue;
					geoResults += (lc+ ", ");
					
					var marker = L.marker([lt, ln]); 
					marker.bindPopup(x[i].getElementsByTagName("location")[0].childNodes[0].nodeValue);//.openPopup();
					marker.on('mouseover', function (e) {
						this.openPopup();
					});
					marker.on('mouseout', function (e) {
						this.closePopup();
					});
					newLayer.addLayer(marker);
				}
			}
			newLayer.addTo(mymap);
			curLayer = newLayer;
			
			document.getElementById("subtitle").innerHTML = "";
			
			document.getElementById("image").innerHTML="<img src=\""+y[clicked_id].getElementsByTagName("url")[0].childNodes[0].nodeValue +"\"/>";
			document.getElementById("details").innerHTML=""+
														"<p><b><u>Title: </b></u>"+y[clicked_id].getElementsByTagName("title1")[0].childNodes[0].nodeValue+"</p>" +
														"<p><b><u>Alternative Title: </b></u>"+y[clicked_id].getElementsByTagName("title2")[0].childNodes[0].nodeValue+"</p>" +
														"<p><b><u>Creator: </b></u>"+y[clicked_id].getElementsByTagName("creator")[0].childNodes[0].nodeValue+"</p>" +
														"<p><b><u>Description: </b></u>"+y[clicked_id].getElementsByTagName("description")[0].childNodes[0].nodeValue+"</p>" +
														"<p><b><u>Geoparsed Locations: </b></u>"+geoResults+"</p>" +
														"";
		}
		
		function searchCollection(){
			
			var val = document.getElementById("textarea").value; console.log(val.toLowerCase());
			var match=[];
			var flag = 0;
			for (i=0; i<y.length; i++){
				if (((y[i].getElementsByTagName("title1")[0].childNodes[0].nodeValue).toLowerCase()).includes(val.toLowerCase())){
					if (flag==0){
						click = y[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
						details(click);
					}
					flag = 1;
					match[match.length] = i;
				}
			}
			loadNearImg(match);
			if (flag == 0){
				document.getElementById("subtitle").innerHTML = "";
				document.getElementById("details").innerHTML="<center><h1>Image Not Found</h1></center>";
				document.getElementById("image").innerHTML="";
			}
			else{
				document.getElementById("subtitle").innerHTML = "<center><i><span style=\"color: #ff0000\">Displaying first match, check the list for the rest</span></i></center>";
			}
		}
		
		function onC(e){
			mymap.removeLayer(curLayer);
			var l = L.marker(e.latlng);
			l.bindPopup("You clicked here: "+e.latlng).openPopup();
			var nL = L.layerGroup([l]);
			nL.addTo(mymap);
			curLayer = nL;
			
			var arr = [];
			for(i=0; i<x.length; i++){
				var m = L.latLng(x[i].getElementsByTagName("latitude")[0].childNodes[0].nodeValue,x[i].getElementsByTagName("longitude")[0].childNodes[0].nodeValue);
				if (e.latlng.distanceTo(m)<100000.0){
					arr[arr.length] = x[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
				}
			}
			
			loadNearImg(arr);
		}
//----------------------------------------------
			window.scrollTo(0,0);
			loadDoc("fileman.xml");
			
			loadDoc("info.xml");

			mymap = L.map('mapid').setView([10.0, 5.0], 2);

			L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpandmbXliNDBjZWd2M2x6bDk3c2ZtOTkifQ._QA7i5Mpkd_m30IGElHziw', {
				maxZoom: 18,
				attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
					'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
					'Imagery © <a href="http://mapbox.com">Mapbox</a>',
				id: 'mapbox.streets'
			}).addTo(mymap);
			
			curLayer = L.layerGroup([]);			
			curLayer.addTo(mymap);
			
			mymap.on('click',onC);
