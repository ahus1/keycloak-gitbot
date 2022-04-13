package org.keycloak.gitbot.endpoints;

import io.smallrye.graphql.client.core.Argument;
import io.smallrye.graphql.client.core.Field;

import java.util.List;

import static io.smallrye.graphql.client.core.Field.field;

public class BaseEndpoint {

    protected Field commonFields() {
        return field("edges",
                field("node",
                        field("... on PullRequest",
                                field("number"),
                                field("title"),
                                field("body"),
                                field("createdAt"),
                                field("updatedAt"),
                                field("commits", List.of(
                                                Argument.arg("first", 100)
                                        ),
                                        field("nodes",
                                                field("commit",
                                                        field("message"),
                                                        field("oid")
                                                )
                                        )
                                ),
                                field("files", List.of(
                                                Argument.arg("first", 100)
                                        ),
                                        field("nodes",
                                                field("path")
                                        )
                                ),
                                field("reactions", List.of(
                                                Argument.arg("first", 10)
                                        ),
                                        field("nodes",
                                                field("content"),
                                                field("user",
                                                        field("login")
                                                )
                                        )
                                ),
                                field("labels", List.of(
                                                Argument.arg("first", 10)
                                        ),
                                        field("nodes",
                                                field("name"),
                                                field("color"),
                                                field("description")
                                        )
                                ),
                                field("comments", List.of(
                                                Argument.arg("first", 10)
                                        ),
                                        field("nodes",
                                                field("body"),
                                                field("author",
                                                        field("login"),
                                                        field("avatarUrl")
                                                )
                                        )
                                ),
                                field("closingIssuesReferences", List.of(
                                                Argument.arg("first", 10)
                                        ),
                                        field("nodes",
                                                field("number"),
                                                field("url"),
                                                field("labels", List.of(
                                                                Argument.arg("first", 10)
                                                        ),
                                                        field("nodes",
                                                                field("name"),
                                                                field("color"),
                                                                field("description")
                                                        )
                                                )
                                        )
                                ),
                                field("author",
                                        field("login"),
                                        field("avatarUrl")
                                ),
                                field("authorAssociation"),
                                field("assignees", List.of(
                                                Argument.arg("first", 10)
                                        ),
                                        field("nodes",
                                                field("login"),
                                                field("avatarUrl")
                                        )
                                )
                        )
                )
        );
    }

}
