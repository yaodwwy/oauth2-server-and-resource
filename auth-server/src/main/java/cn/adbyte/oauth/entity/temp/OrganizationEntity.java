package cn.adbyte.oauth.entity.temp;

import cn.adbyte.oauth.common.BaseEntity;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Cacheable
@Table(name = "t_organization")
public class OrganizationEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private OrganizationEntity parent;
    @Column(name = "transaction_organization_id")
    private Integer transactionOrganization;
    @Column(name = "architecture_type")
    private ArchitectureCateEnum architectureType;
    private String name;
    private String companyName;
    private String description;
    private String businessScope;
    private String licenseCode;
    private String creditIdentifier;
    private String legalRepresentative;
    private String registeredCapital;
    private String memo;
    private Boolean enabled;
    private Boolean authentication;
    private String purchaseQuota;
    private String annexAddress;
    private Boolean platform;

//    @JsonIgnore
//    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
//    private Set<DepartmentEntity> department = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<OrganizationEntity> children = new HashSet<>();



    public OrganizationEntity() {
    }

    public Set<OrganizationEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<OrganizationEntity> children) {
        this.children = children;
    }

    public OrganizationEntity getParent() {
        return parent;
    }

    public void setParent(OrganizationEntity parent) {
        this.parent = parent;
    }


    public Integer getTransactionOrganization() {
        return transactionOrganization;
    }

    public void setTransactionOrganization(Integer transactionOrganization) {
        this.transactionOrganization = transactionOrganization;
    }

    public ArchitectureCateEnum getArchitectureType() {
        return architectureType;
    }

    public void setArchitectureType(ArchitectureCateEnum architectureType) {
        this.architectureType = architectureType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public String getCreditIdentifier() {
        return creditIdentifier;
    }

    public void setCreditIdentifier(String creditIdentifier) {
        this.creditIdentifier = creditIdentifier;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enable) {
        this.enabled = enable;
    }

    public Boolean getAuthentication() {
        return authentication;
    }

    public String getPurchaseQuota() {
        return purchaseQuota;
    }

    public String getAnnexAddress() {
        return annexAddress;
    }

    public void setAnnexAddress(String annexAddress) {
        this.annexAddress = annexAddress;
    }

    public void setPurchaseQuota(String purchaseQuota) {
        this.purchaseQuota = purchaseQuota;
    }

    public void setAuthentication(Boolean authentication) {
        this.authentication = authentication;
    }

    public Boolean getPlatform() { return platform; }

    public void setPlatform(Boolean platform) { this.platform = platform; }

    @Override
    public String toString() {
        return "OrganizationEntity{" +
                ", transactionOrganization=" + transactionOrganization +
                ", architectureType=" + architectureType +
                ", name='" + name + '\'' +
                ", companyName='" + companyName + '\'' +
                ", description='" + description + '\'' +
                ", businessScope='" + businessScope + '\'' +
                ", licenseCode='" + licenseCode + '\'' +
                ", creditIdentifier='" + creditIdentifier + '\'' +
                ", legalRepresentative='" + legalRepresentative + '\'' +
                ", registeredCapital='" + registeredCapital + '\'' +
                ", memo='" + memo + '\'' +
                ", enabled=" + enabled +
                ", authentication=" + authentication +
                ", purchaseQuota='" + purchaseQuota + '\'' +
                ", annexAddress='" + annexAddress + '\'' +
                ", platform=" + platform +
                ", id=" + id +
                ", del=" + del +
                ", last=" + last +
                ", time=" + time +
                ", operator=" + operator +
                '}';
    }

    public static class Fields {
        public static final String id = "id";
        public static final String companyName = "companyName";
    }
}
