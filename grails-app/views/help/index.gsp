<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style>
body {
	font-family:sans-serif;
}
h2 {
	border-top:1px solid grey;
	font-family:monospace;
}
.example, .returns, .exceptions {
	margin-left:10px;
}
.example {
	background-color:#CCC;
	padding:5px;
}
.returns code, .exceptions code {
	background-color:#CCC;
}
</style>
<g:javascript library="jquery" plugin="jquery" />
<jq:jquery>
$('.example').before('<h3>Example</h3>');
$('.returns').before('<h3>Returns</h3>');
$('.exceptions').before('<h3>Exceptions</h3>');
</jq:jquery>
</head>
<body>

<h1>Semantifier</h1>

&copy; 2011 Sebastian Kurf&uuml;rst

<h2>/type/isLiteral/[fullyQualifiedUri]</h2>
	<code class="example">GET /type/isLiteral/http://xmlns.com/foaf/0.1/givenName</code>
	<div class="returns">string <code>true</code> or <code>false</code>. <code>true</code> if resource is a literal, <code>false</code> otherwise.</div>
	<div class="exceptions">404 if <code>fullyQualifiedUri</code> does not point to a resource.

<h2>/annotate</h2>
	<code class="example">GET /annotate?lang=de</code>
	<code class="example">GET /annotate?lang=en</code>
	<div class="returns">JSON string of annotated content</div>
</body>
</html>