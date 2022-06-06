package woowacourse.shoppingcart.domain;

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
}
