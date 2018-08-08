<html>
<head>
<title>Cooker Secured Page</title>
<link rel="stylesheet"	href="${pageContext.request.contextPath}/resources/my_page.css" type="text/css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"><jsp:text /></script>
</head>
<body>
<h1>${message}</h1>
	
<nav>
  <label for="touch"><span>menu</span></label>               
  <input type="checkbox" id="touch"> 

  <ul class="slide">
    <li><a href="../cookerFlow">Cooker page</a></li> 
    <li><a href="../pizzaFlow">Pizza Store</a></li>
    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>    
  </ul>
</nav> 
</body>
</html>
