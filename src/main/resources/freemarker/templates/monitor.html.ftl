<#-- @ftlvariable name="pullrequests" type="org.keycloak.gitbot.model.PullRequest[]" -->
<#-- @ftlvariable name="title" type="java.lang.String" -->
<html lang="en">
<head>
    <title>Keycloak GitBot - ${title}</title>
    <#include "header.html.ftl">
</head>
<body>
<#include "navbar.html.ftl">
<section class="section">
    <div class="container">
        <h1 class="title">
            ${title}
        </h1>
        <!-- <p class="subtitle">
            This helps you to triage them!
        </p> -->
        <table class="table is-fullwidth">
            <thead>
            <tr>
                <th>Issue</th>
                <td>Assignee</td>
            </tr>
            </thead>
            <tbody>
            <#list pullrequests as pullrequest>
                <tr class="is-size-5">
                    <td>
                        <a href="https://github.com/keycloak/${pullrequest.repositoryName}/pull/#{pullrequest.number}"
                           target="assistant_pr#{pullrequest.number}">${pullrequest.title}</a><br>
                        <div class="is-size-6">
                            #${pullrequest.repositoryName}/#{pullrequest.number} opened by
                            <div class="dropdown is-hoverable pt-1 pb-1" style="vertical-align: revert">
                                <span class="dropdown-trigger">
                                        <a target="assistant_user${pullrequest.author.login}"
                                           href="https://github.com/${pullrequest.author.login}">${pullrequest.author.login}</a>
                                </span>
                                <div class="dropdown-menu" role="menu">
                                    <div class="dropdown-content">
                                        <div class="dropdown-item">
                                            <div class="columns">
                                                <div class="column is-narrow">
                                                    <figure class="image is-64x64 is-pulled-left">
                                                        <img alt="${pullrequest.author.login}"
                                                             src="${pullrequest.author.avatarUrl}&s=128"
                                                             class="is-rounded"/>
                                                    </figure>
                                                </div>
                                                <div class="column">${pullrequest.author.login}<br>
                                                    <span class="is-size-6" style="white-space: nowrap"
                                                          title="${pullrequest.authorAssociation.description}">${pullrequest.authorAssociation.role}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <span title="${pullrequest.createdAt}">${pullrequest.createdAtPretty}</span>,
                            updated <span title="${pullrequest.updatedAt}">${pullrequest.updatedAtPretty}</span>
                            <#list pullrequest.labels as label>
                                <span class="tag is-size-7" style="background: ${'#' + label.backgroundColor}; color: ${'#' + label.foregroundColor}">${label.name}</span>
                            </#list>
                        </div>
                        <#list pullrequest.commands as command>
                            <form data-hx-post="${command.url}">
                                <div class="dropdown is-hoverable" style="vertical-align: revert">
                                <button class="button is-primary dropdown-trigger" data-hx-swap="outerHTML">
                                    ${command.description}
                                </button>
                                <#if command.comment??>
                                    <div class="dropdown-menu" role="menu">
                                        <div class="dropdown-content">
                                            <div class="dropdown-item">
                                                <textarea class="textarea" style="width: 80vw" name="comment">${command.comment}</textarea>
                                            </div>
                                        </div>
                                    </div>
                                </#if>
                                </div>
                            </form>
                        </#list>
                    </td>
                    <td>
                        <#list pullrequest.assignees as assignee>
                            <div class="dropdown is-hoverable" style="vertical-align: revert">
                                <span class="dropdown-trigger">
                                        <a target="assistant_user${assignee.login}"
                                           href="https://github.com/${assignee.login}"><figure
                                                    class="image is-16x16 is-pulled-left">
                                                        <img alt="${assignee.login}" src="${assignee.avatarUrl}&s=32"
                                                             class="is-rounded"/>
                                                    </figure></a>
                                </span>
                                <div class="dropdown-menu" role="menu" style="left: unset; right: -20px">
                                    <div class="dropdown-content">
                                        <div class="dropdown-item">
                                            <div class="columns">
                                                <div class="column is-narrow">
                                                    <figure class="image is-64x64 is-pulled-left">
                                                        <img alt="${pullrequest.author.login}" src="${assignee.avatarUrl}" class="is-rounded"/>
                                                    </figure>
                                                </div>
                                                <div class="column">${assignee.login}
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</section>
<#include "csrf.html.ftl">
</body>
</html>
