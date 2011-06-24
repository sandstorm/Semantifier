<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style>
.example {
	border: 1px solid red;
}
</style>
</head>
<body>

<h1>Multi Annotator</h1>

&copy; 2011 Sebastian Kurfürst

<h2>/type/isLiteral/[fullyQualifiedUri]</h2>
	<code class="example">GET /type/isLiteral/http://xmlns.com/foaf/0.1/givenName</code>
	<div class="returns">string <code>true</code> or <code>false</code>. <code>true</code> if resource is literal, <code>false</code> otherwise.</div>
	<div class="exceptions">404 if <code>fullyQualifiedUri</code> does not point to a resource.

<h2>/annotate</h2>
1) Language Detection
2) OpenCalais
3) Freebase
</body>
</html>