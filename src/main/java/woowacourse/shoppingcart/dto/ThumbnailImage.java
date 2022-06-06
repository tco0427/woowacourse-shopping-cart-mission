package woowacourse.shoppingcart.dto;

import woowacourse.shoppingcart.domain.Image;

public class ThumbnailImage {

    private final String url;
    private final String alt;

    public ThumbnailImage(Image image) {
        this.url = image.getUrl();
        this.alt = image.getAlteration();
    }

    public ThumbnailImage(String url, String alt) {
        this.url = url;
        this.alt = alt;
    }

    public String getUrl() {
        return url;
    }

    public String getAlt() {
        return alt;
    }
}
