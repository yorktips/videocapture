<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="error.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">



<html>

<head>Capture Picture</head>



<body>


<applet code="WebCang.class" archive="webcam-capture-0.3.10.jar, bridj-0.6.2.jar, jslf4j-jdk14-1.7.2.jar, slf4j-api-1.7.2.jar, imagleTool2.jar" height="560" width="800" >
<param name=id value=<%=request.getParameter("id")%> />
</applet>

</body>

</html>