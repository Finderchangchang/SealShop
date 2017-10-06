package dw.seal.shop.http;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/9.
 */
public class AreaModel implements Serializable {
    private String Ar_area_id;
    private String Ar_area_name;

    public String getAr_area_id() {
        return Ar_area_id;
    }

    public void setAr_area_id(String ar_area_id) {
        Ar_area_id = ar_area_id;
    }

    public String getAr_area_name() {
        return Ar_area_name;
    }

    public void setAr_area_name(String ar_area_name) {
        Ar_area_name = ar_area_name;
    }
}
