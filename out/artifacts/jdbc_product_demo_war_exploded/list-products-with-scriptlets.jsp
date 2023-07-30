<%@ page import="java.util.*, com.techno.web.jdbc.*" %>
<!DOCTYPE html>
<html>

<head>
	<title>Product Tracker App</title>
	
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<%
	// get the products from the request object (sent by servlet)
	List<Product> theProducts =
					(List<Product>) request.getAttribute("PRODUCT_LIST");
%>

<body>

	<div id="wrapper">
		<div id="header">
			<h2>FooBar University</h2>
		</div>
	</div>

	<div id="container">
	
		<div id="content">
		
			<table>
			
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
				</tr>
				
				<% for (Product tempProduct : theProducts) { %>
				
					<tr>
						<td> <%= tempProduct.getFirstName() %> </td>
						<td> <%= tempProduct.getLastName() %> </td>
						<td> <%= tempProduct.getEmail() %> </td>
					</tr>
				
				<% } %>
				
			</table>
		
		</div>
	
	</div>
</body>


</html>








