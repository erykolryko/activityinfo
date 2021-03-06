<#include "../page/Scaffolding.ftl">

<!DOCTYPE html>
<html>
<head>
  <title>${entity.name}</title>
</head>
<body>
	<h1>${entity.name}</h1>
	
	<p><a href="/resources/adminLevel/${entity.level.id?c}">${entity.level.name}</a> in <a href="/resources/country/${entity.level.country.codeISO}">${entity.level.country.name}</a></p>
	<#if parents?has_content>
	<h2>Parents</h2>
	<ul>
		<#list parents as parent>
		<li>${parent.level.name}: <a href="/resources/adminEntity/${parent.id?c}">${parent.name}</a></li>
		</#list>
	</ul>
	</#if>
	<#if entity.level.childLevels?has_content>
		<h2>Child units</h2>
		<#list entity.level.childLevels as childLevel>
		<h3>${childLevel.name}</h3>
		<ul>
			<#list childLevel.entities?sort_by("name") as childEntity>
			<#if childEntity.parent.id == entity.id>
				<li><a href="/resources/adminEntity/${childEntity.id?c}">${childEntity.name}</a></li>
			</#if>
			</#list>
		</ul> 
		</#list>
	</#if>
</body>
</html>
