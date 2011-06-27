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

<h1>API Usage</h1>
<h2>/type/isLiteral/[fullyQualifiedUri]</h2>
	<code class="example">GET <a href="/semantifier/type/isLiteral/http://xmlns.com/foaf/0.1/givenName">/type/isLiteral/http://xmlns.com/foaf/0.1/givenName</a></code>
	<div class="returns">string <code>true</code> or <code>false</code>. <code>true</code> if resource is a literal, <code>false</code> otherwise.</div>
	<div class="exceptions">404 if <code>fullyQualifiedUri</code> does not point to a resource.

<h2>/annotate</h2>
	<code class="example">GET <a href="/semantifier/annotate?lang=de">/annotate?lang=de</a></code>
	<code class="example">GET <a href="/semantifier/annotate?lang=en">/annotate?lang=en</a></code>
	<div class="returns">JSON string of annotated content</div>

<h1>Environment check</h1>

After correcting environment errors, <b>make sure to restart the server</b>

<ul>
	<li>Configuration file:
		<g:if test="${configurationFileExists}">
			found
		</g:if>
		<g:else>
			NOT FOUND at location <%=settings.grails.config.locations%>
		</g:else>
	</li>
	<li>OpenCalais API Key:
		<g:if test="${settings.ner.OpenCalais.apiKey}">
			OK
		</g:if>
		<g:else>
			NOT FOUND!!
		</g:else>
	</li>
	<li>Alchemy API Key:
		<g:if test="${settings.ner.Alchemy.apiKey}">
			OK
		</g:if>
		<g:else>
			NOT FOUND!!
		</g:else>
	</li>
</ul>

<h1>Backend Server check</h1>
</body>
</html>