<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<!--  Homepage, prve tlacitko logne uzivatela, druhym moze vytvorit account -->

	<a
		href="<%=response.encodeUrl(request.getContextPath() + "/Controller?action=login")%>">
		<img src="http://www.bestmarg.in/web/images/Login.jpg"
		alt="HTML tutorial" style="width: 300px; height: 150px; border: 0;">
	</a>
	<p></p>
	<a
		href="<%=response.encodeUrl(request.getContextPath() + "/Controller?action=createaccount")%>">
		<img src="http://s3.amazonaws.com/churchplantmedia-cms/christ_community_tucson/createaccountbutton-1.jpg"
		alt="HTML tutorial" style="width: 300px; height: 150px; border: 0;">
	</a>

</body>
</html>