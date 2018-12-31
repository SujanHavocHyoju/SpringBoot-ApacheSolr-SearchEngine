<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<head>
		<title>Solr Search</title>
	</head>
	<body>
		<h3>Search Result for : ${search.content}</h3>
		<table>
			<tbody>
	
			</tbody>
			<c:forEach var="s" items="${search.fierceNews}">
				<tr>
					<td><strong><a href="${s.urlLink}" />${s.title}</strong></td>
				</tr> 
				<tr>
					<td>URL: <a href="${s.urlLink}" />${s.urlLink}</td>
				</tr>
				 <tr>
					<td>Published Date: ${s.datePublished}</td>
				</tr> 
				<tr>
					<td>Abstract: ${s.abstractContent}</td>
				</tr>
				<tr>
					<td>Authors: ${s.authors}</td>
				</tr>
			</c:forEach>
			<tr>
				<a href="/solr-search">Submit another query</a>
			</tr>
		</table>
	</body>
</html>