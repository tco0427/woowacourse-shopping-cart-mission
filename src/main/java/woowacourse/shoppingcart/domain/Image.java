package woowacourse.shoppingcart.domain;

import java.util.Objects;

public class Image {

    private final String url;
    private final String alteration;

    public Image(String url, String alteration) {
        this.url = url;
        this.alteration = alteration;
    }

    public String getUrl() {
        return url;
    }

    public String getAlteration() {
        return alteration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Image image = (Image) o;
        return Objects.equals(url, image.url) && Objects.equals(alteration, image.alteration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, alteration);
    }
}
