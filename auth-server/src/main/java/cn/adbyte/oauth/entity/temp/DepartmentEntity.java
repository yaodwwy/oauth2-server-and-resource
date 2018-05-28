package cn.adbyte.oauth.entity.temp;

import cn.adbyte.oauth.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;

@Entity
@Cacheable
@Table(name = "t_department")
public class DepartmentEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private DepartmentEntity parent;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private OrganizationEntity organization;
    @Transient
    private String orgName;

    private String number;
    private String name;
    private String fullname;
    private String contacts;
    private String telephone;
    private String description;

    public DepartmentEntity getParent() {
        return parent;
    }

    public void setParent(DepartmentEntity parent) {
        this.parent = parent;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Fields {
        public static final String id = "id";
        public static final String name = "name";
        public static final String organization = "organization";
    }

}
