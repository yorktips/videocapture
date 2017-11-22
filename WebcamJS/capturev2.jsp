<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="error.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Capture Picture</title>
	<style type="text/css">
		body { font-family: Helvetica, sans-serif; }
		h2, h3 { margin-top:0; }
		form { margin-top: 15px; }
		form > input { margin-right: 15px; }
		#results { float:right; margin:0px; padding:0px; border:0px solid;  }
	</style>
</head>
<body>
   <form>
	<h4>Capture Picture</h4>
    <table>
    <tr><td>
	         <div id="my_camera"></div>
        </td>

        <td>
            <div id="results">Your captured image will appear here...</div>
        </td>
    </tr>
    </table>

	<!-- First, include the Webcam.js JavaScript Library -->
	<script type="text/javascript" src="../webcam.js"></script>

	<!-- Configure a few settings and attach camera -->
	<script language="JavaScript">
		Webcam.set({
			// live preview size
			width: 640,
			height: 480,

			// device capture size
			dest_width: 640,
			dest_height: 480,

			// final cropped size
			crop_width: 326,
			crop_height: 456,

			// format and quality
			image_format: 'jpeg',
			jpeg_quality: 90
		});

		Webcam.attach( '#my_camera' );
	</script>

	<!-- A button for taking snaps -->


		 <select name="Resolution">
			  <option value="volvo">Volvo</option>
		</select>

		<input type=button id="Capture" value="Capture" onClick="take_snapshot()">
		<input type=button id="Save" value="Save" onClick="save_snapshot()">
		<input type=button id="Exit" value="Exit" onClick="take_exit()">
		Student ID: <input type="text" name="id">
	</form>

	<-- Code to handle taking the snapshot and displaying it locally -->
	<script language="JavaScript">

		function take_exit() {
		    //alert("exit");
		    window.close();
		}

		function uploadcomplete(event){
			Webcam.unfreeze();
		    //document.getElementById("loading").innerHTML="";
		    var image_return=event.target.responseText;
		    alert("reponse=" + image_return);
		    //var showup=document.getElementById("uploaded").src=image_return;
		}

		function take_snapshot() {
			// take snapshot and get image data
			Webcam.snap( function(data_uri) {
				// display results in page
				document.getElementById('results').innerHTML =
					'' +
					'<img id="base64image" src="'+data_uri+'"/>';
			} );
		}

		function save_snapshot(){

		    Webcam.freeze();

			// swap buttons back
			//document.getElementById('Capture').style.display = '';
			//document.getElementById('Capture').style.display = 'none';

		    //document.getElementById("loading").innerHTML="Saving, please wait...";
		    var file =  document.getElementById("base64image").src;
		    var formdata = new FormData();
		    formdata.append("base64image", file);
		    var ajax = new XMLHttpRequest();
		    ajax.addEventListener("load", function(event) { uploadcomplete(event);}, false);
		    var id=<%=request.getParameter("id")%>;
		    alert("id=" + id);
		    var URL="getPhotofromApplet?id=<%=request.getParameter("id")%>";
		    alert("URL="+ URL);
		    ajax.open("POST", URL);
		    ajax.send(formdata);
		}
	</script>

</body>
</html>















<body>
<applet code="WebCang.class" archive="jmf.jar,imagleTool.jar" height="560" width="800">
<param name=id value=<%=request.getParameter("id")%> />
</applet>
</body>
</html>