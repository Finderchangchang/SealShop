package dw.seal.shop.http;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询印章信息on 2015/7/9.
 */
public class ReadSoapObject {
    public static List<SignetModel> listSignet = new ArrayList<SignetModel>();

    /**
     * 解析SoapObject对象
     *
     * @param result
     * @return
     */
    public static List<SignetModel> parseSoapObject(SoapObject result, String method) {

        List<SignetModel> list = new ArrayList<SignetModel>();
        SoapObject provinceSoapObject = (SoapObject) result.getProperty(method + "Result");

        if (provinceSoapObject.getProperty("Sucess").toString().equals("true")) {
            SoapObject soaplist = (SoapObject) provinceSoapObject.getProperty("Obj");
            if (method.equals("GetSignet")) {
                SignetModel signet = new SignetModel();
                signet.setCorpName(soaplist.getProperty("CorpName").toString());
                signet.setCorpTypeName(soaplist.getProperty("CorpTypeName").toString());
                signet.setSignetContent(soaplist.getProperty("SignetContent").toString());
                signet.setSignetId(soaplist.getProperty("SignetId").toString());
                signet.setSignetTypeName(soaplist.getProperty("SignetTypeName").toString());
                String a[] = soaplist.getProperty("CorpCreateDate").toString().split("T");
                signet.setCorpCreateDate(a[0]);
                signet.setRegApprover(soaplist.getProperty("RegApprover").toString());
                list.add(signet);
            } else if (method.equals("GetSignetAll")) {
                SignetModel signet = new SignetModel();
                signet.setCorpId(soaplist.getProperty("CorpId").toString());
                signet.setCorpName(soaplist.getProperty("CorpName").toString());
                signet.setCorpLinker(soaplist.getProperty("CorpLinker").toString());
                signet.setCorpLinkWay(soaplist.getProperty("CorpLinkWay").toString());
                signet.setCorpTypeName(soaplist.getProperty("CorpTypeName").toString());
                signet.setCorpAddress(soaplist.getProperty("CorpAddress").toString());
                signet.setCorpTaxNo(soaplist.getProperty("CorpTaxNo").toString());
                signet.setSignetContent(soaplist.getProperty("SignetContent").toString());
                signet.setSignetId(soaplist.getProperty("SignetId").toString());
                signet.setSignetTypeName(soaplist.getProperty("SignetTypeName").toString());
                String a[] = soaplist.getProperty("CorpCreateDate").toString().split("T");
                signet.setCorpCreateDate(a[0]);
                signet.setRegApprover(soaplist.getProperty("RegApprover").toString());
                signet.setSignetSpecification(soaplist.getProperty("SignetSpecification").toString());
                signet.setSignetMaterialName(soaplist.getProperty("SignetMaterialName").toString());
                list.add(signet);
            } else {
                for (int i = 0; i < soaplist.getPropertyCount(); i++) {
                    SignetModel signet = new SignetModel();
                    SoapObject soaparea = (SoapObject) soaplist.getProperty(i);
                    if (method.equals("SearchSignet")) {
                        signet.setCorpId(soaparea.getProperty("CorpId").toString());
                        signet.setCorpName(soaparea.getProperty("CorpName").toString());
                        String a[] = soaparea.getProperty("CorpCreateDate").toString().split("T");
                        signet.setCorpCreateDate(a[0]);
                    } else if (method.equals("GetNotice")) {
                        signet.setTitle(soaparea.getProperty("Title").toString());//标题
                        signet.setUrl(soaparea.getProperty("NoticeUrl").toString());//链接
                    } else if (method.equals("SearchCorpSignet")) {
                        signet.setSignetId(soaparea.getProperty("SignetId").toString());
                        signet.setSignetTypeName(soaparea.getProperty("SignetTypeName").toString());
                        signet.setSignetSpecification(soaparea.getProperty("SignetSpecification").toString());
                        signet.setSignetContent(soaparea.getProperty("SignetContent").toString());

                        String a[] = soaparea.getProperty("CorpCreateDate").toString().split("T");
                        if (a != null) {
                            signet.setCorpCreateDate(a[0] + " " + a[1]);
                        } else {

                        }
                    } else if (method.equals("PasswordLogin")) {

                        signet.setCorpId(soaparea.getProperty("CorpId").toString());
                        signet.setCorpName(soaparea.getProperty("CorpName").toString());
                        signet.setCorpLinker(soaparea.getProperty("CorpLinker").toString());
                        signet.setCorpLinkWay(soaparea.getProperty("CorpLinkWay").toString());
                        signet.setCorpTypeName(soaparea.getProperty("CorpTypeName").toString());
                        signet.setCorpAddress(soaparea.getProperty("CorpAddress").toString());
                        signet.setCorpTaxNo(soaparea.getProperty("CorpTaxNo").toString());
                        signet.setSignetContent(soaparea.getProperty("SignetContent").toString());
                        signet.setSignetId(soaparea.getProperty("SignetId").toString());
                        signet.setSignetTypeName(soaparea.getProperty("SignetTypeName").toString());
                        String a[] = soaparea.getProperty("CorpCreateDate").toString().split("T");
                        signet.setCorpCreateDate(a[0]);
                        signet.setRegApprover(soaparea.getProperty("RegApprover").toString());
                        signet.setSignetSpecification(soaparea.getProperty("SignetSpecification").toString());
                        signet.setSignetMaterialName(soaparea.getProperty("SignetMaterialName").toString());
                    }
                    list.add(signet);
                    System.out.println("lsit:::" + list.size());
                }
            }
        } else {
            return null;
        }
        return list;
    }

    public static MobileVersionModel getVersion(SoapObject result, String method) {
        SoapObject provinceSoapObject = (SoapObject) result.getProperty(method + "Result");
        MobileVersionModel model = new MobileVersionModel();
        if (provinceSoapObject.getProperty("Sucess").toString().equals("true")) {
            SoapObject rm = (SoapObject) provinceSoapObject.getProperty("Obj");
            model.setVersion(rm.getProperty("Version").toString());
            model.setLink(rm.getProperty("Link").toString());
        } else {
            return null;
        }
        return model;
    }

    public static String getNewPassWord(SoapObject result, String method) {

        SoapObject provinceSoapObject = (SoapObject) result.getProperty(method + "Result");

        if (provinceSoapObject.getProperty("Sucess").toString().equals("true")) {
            return provinceSoapObject.getProperty("Obj").toString();
        } else {
            return provinceSoapObject.getProperty("Message").toString();
        }
    }


    /**
     * 解析SoapObject对象
     *
     * @param result
     * @return
     */
    public static boolean isPassWordTrue(SoapObject result, String method) {

        List<SignetModel> list = new ArrayList<SignetModel>();
        SoapObject provinceSoapObject = (SoapObject) result.getProperty(method + "Result");

        if (provinceSoapObject.getProperty("Sucess").toString().equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    public static String isPassWorDMessage(SoapObject result, String method) {

        List<SignetModel> list = new ArrayList<SignetModel>();
        SoapObject provinceSoapObject = (SoapObject) result.getProperty(method + "Result");

        return provinceSoapObject.getProperty("Message").toString();

    }

    /**
     * 解析SoapObject对象
     *
     * @param result
     * @return
     */
    public static List<AreaModel> parseSoapObjectArea(SoapObject result) {
        System.out.println("size::::");
        List<AreaModel> list = new ArrayList<AreaModel>();
        SoapObject provinceSoapObject = (SoapObject) result.getProperty("GetAreaResult");
        System.out.println("success" + (provinceSoapObject.getProperty("Sucess").toString()));
        if (provinceSoapObject.getProperty("Sucess").toString().equals("true")) {
            SoapObject soaplist = (SoapObject) provinceSoapObject.getProperty("Obj");
            System.out.println("soap:size" + soaplist.getNestedSoapCount());
            for (int i = 0; i < soaplist.getPropertyCount(); i++) {
                AreaModel area = new AreaModel();
                SoapObject soaparea = (SoapObject) soaplist.getProperty(i);
                area.setAr_area_id(soaparea.getProperty("Ar_area_id").toString());
                area.setAr_area_name(soaparea.getProperty("Ar_area_name").toString().replaceAll("\n", ""));
                //System.out.println(area.getAr_area_name().replaceAll("\n", "") + ";;;;;;;;;;;");
                list.add(area);
            }
            //System.out.println("size::::" + list.size());
            return list;
        } else {
            return null;
        }
    }
}
