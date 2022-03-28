<#-- @ftlvariable name="search" type="org.keycloak.gitbot.graphql.SearchEdges[]" -->
<html>
<head>
    <title>List of PRs</title>
    <#include "header.html.ftl">
</head>
<body>
<#include "navbar.html.ftl">
<section class="section">
    <div class="container">
        <h1 class="title">
            List of PRs
        </h1>
        <p class="subtitle">
            My first website with <strong>Bulma</strong>!
        </p>
        <ul>
            <#list search as edge>
                <li><a href="https://github.com/keycloak/keycloak/pull/#{edge.node.number}" target="assistant_pr#{edge.node.number}">##{edge.node.number}</a> ${edge.node.title}
                    <button data-hx-post="/prs/#{edge.node.number}/click" data-hx-swap="outerHTML">
                        Click Me
                    </button>
                </li>
            </#list>
        </ul>
    </div>
</section>
</body>
</html>