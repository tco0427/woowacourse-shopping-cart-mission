package woowacourse.shoppingcart.dto;

import woowacourse.shoppingcart.domain.Image;

public class ThumbnailImageDto {

    private final String url;
    private final String alt;

    public ThumbnailImageDto(Image image) {
        this.url = image.getUrl();
        this.alt = image.getAlteration();
    }

    public ThumbnailImageDto(String url, String alt) {
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
