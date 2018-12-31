<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
	<head>
		<title>Solr Search</title>
	</head>
	<body>
        <h3>Enter Search Query</h3>
        <form method="POST" action="/solr-search"  modelAttribute="search">
             <table>
				<tr>
					<td><label path="content">Query: </label></td>
					<td><input path="content" name="content"/></td>
				</tr>
				<tr>
					<td><input type="submit" value="Submit"/></td>
			</table>
        </form>
    </body>
</html>