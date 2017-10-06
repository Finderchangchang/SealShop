package dw.seal.shop.http;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/9.
 */

public class MobileVersionModel implements Serializable {
    private String Version;
    private String Link;

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
