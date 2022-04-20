package org.keycloak.gitbot.model;

import java.util.Objects;

public class Label {
    public static final Label AREA_DOCS = Label.of("area/docs");
    public static final Label AREA_TRANSLATIONS = Label.of("area/translations");
    public static final Label AREA_QUARKUS = Label.of("area/quarkus");
    private final org.keycloak.gitbot.graphql.Label delegate;

    public Label(org.keycloak.gitbot.graphql.Label delegate) {
        this.delegate = delegate;
    }

    public static Label of(String l) {
        org.keycloak.gitbot.graphql.Label delegate = new org.keycloak.gitbot.graphql.Label();
        delegate.setName(l);
        return new Label(delegate);
    }

    public String getName() {
        return delegate.getName();
    }

    public String getBackgroundColor() {
        return delegate.getColor();
    }

    public String getForegroundColor() {
        int hexR = Integer.parseInt(getBackgroundColor().substring(0, 2), 16);
        int hexG = Integer.parseInt(getBackgroundColor().substring(2, 4), 16);
        int hexB = Integer.parseInt(getBackgroundColor().substring(4, 6), 16);
        // Gets the average value of the colors
        double contrastRatio = ((double) (hexR + hexG + hexB)) / (255 * 3);
        return contrastRatio >= 0.5
                ? "000000"
                : "ffffff";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Label label = (Label) o;
        return Objects.equals(delegate.getName(), label.delegate.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(delegate.getName());
    }
}
