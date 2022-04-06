<#-- @ftlvariable name="notSingleCommit" type="boolean" -->
<#-- @ftlvariable name="missingLinkedIssue" type="boolean" -->
<#-- @ftlvariable name="hasLinkedJiraIssue" type="boolean" -->
Thanks for contributing to this project with this pull request!

When creating PRs, this project asks in its [contributing guidelines](https://github.com/keycloak/keycloak/blob/main/CONTRIBUTING.md) to:

<#if missingLinkedIssue>* create a GitHub issue first
</#if>
<#if notSingleCommit>* squash all commits in this pull request to a single commit
</#if>
<#if !hasLinkedJiraIssue>* reference the GitHub issue in the commit message of the pull request with a message as the following:
  ```
  A brief descriptive summary

  Optionally, more details around how it was implemented

  Closes #1234
  ```
</#if>
<#if hasLinkedJiraIssue>* reference the Jira issue in the commit message of the pull request with a message as the following:
  ```
  KEYCLOAK-1234 A brief descriptive summary

  Optionally, more details around how it was implemented
  ```
</#if>
<#if missingLinkedIssue>* update the description of this PR to include `Closes #1234`.
</#if>

<#if missingLinkedIssue && !hasLinkedJiraIssue>This will help collaboration on GitHub in two ways:

1. It populates the right-hand column on both the issue and the PR with a reference to the issue you're about to fix.
2. It will close the issue once the PR has been merged automatically.

All of this allows us to track all changes in the Git commit log to the issue on GitHub.
</#if>

Please update your PR to align with this. Thanks!