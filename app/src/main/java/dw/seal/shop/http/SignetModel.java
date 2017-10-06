package dw.seal.shop.http;

import java.io.Serializable;

/**
 * 印章模型 on 2015/7/9.
 */
public class SignetModel implements Serializable {
    private String corpId;//企业编码
    private String corpName;//企业名称
    private String CorpTypeName;//企业类型
    private String CorpAddress;//企业地址
    private String CorpLinker;//企业联系人
    private String corpLinkWay;//联系电话
    private String corpTaxNo;//营业执照

    private String signetId;//印章编码

    private String SignetContent;//印章内容
    private String signetSpecification;//印章规格
    private String signetTypeName;//印章类型
    private String SignetMaterialName;//印章材料
    private String RegApprover;//备案单位
    private String corpCreateDate;//备案时间
    private String title;//相关知识标题
    private String url;//相关知识链接



    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpTypeName() {
        return CorpTypeName;
    }

    public void setCorpTypeName(String corpTypeName) {
        CorpTypeName = corpTypeName;
    }

    public String getCorpAddress() {
        return CorpAddress;
    }

    public void setCorpAddress(String corpAddress) {
        CorpAddress = corpAddress;
    }

    public String getCorpLinker() {
        return CorpLinker;
    }

    public void setCorpLinker(String corpLinker) {
        CorpLinker = corpLinker;
    }

    public String getCorpLinkWay() {
        return corpLinkWay;
    }

    public void setCorpLinkWay(String corpLinkWay) {
        this.corpLinkWay = corpLinkWay;
    }

    public String getCorpTaxNo() {
        return corpTaxNo;
    }

    public void setCorpTaxNo(String corpTaxNo) {
        this.corpTaxNo = corpTaxNo;
    }

    public String getSignetId() {
        return signetId;
    }

    public void setSignetId(String signetId) {
        this.signetId = signetId;
    }

    public String getSignetContent() {
        return SignetContent;
    }

    public void setSignetContent(String signetContent) {
        SignetContent = signetContent;
    }

    public String getSignetSpecification() {
        return signetSpecification;
    }

    public void setSignetSpecification(String signetSpecification) {
        this.signetSpecification = signetSpecification;
    }

    public String getSignetTypeName() {
        return signetTypeName;
    }

    public void setSignetTypeName(String signetTypeName) {
        this.signetTypeName = signetTypeName;
    }

    public String getSignetMaterialName() {
        return SignetMaterialName;
    }

    public void setSignetMaterialName(String signetMaterialName) {
        SignetMaterialName = signetMaterialName;
    }

    public String getRegApprover() {
        return RegApprover;
    }

    public void setRegApprover(String regApprover) {
        RegApprover = regApprover;
    }

    public String getCorpCreateDate() {
        return corpCreateDate;
    }

    public void setCorpCreateDate(String corpCreateDate) {
        this.corpCreateDate = corpCreateDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
