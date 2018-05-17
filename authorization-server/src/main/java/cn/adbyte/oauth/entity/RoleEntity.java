package cn.adbyte.oauth.entity;

import cn.adbyte.oauth.common.BaseEntity;
import cn.adbyte.oauth.entity.temp.ArchitectureCateEnum;
import cn.adbyte.oauth.entity.temp.OrganizationEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Where;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Cacheable
@Table(name = "t_role")
public class RoleEntity extends BaseEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organization_id", updatable = false, insertable = false)
    private OrganizationEntity organization;
    @Transient
    private String orgName;
    @Transient
    private ArchitectureCateEnum architectureType;
    @Column(name = "organization_id")
    private Integer orgID;
    private String name;
    private String description;
    @Transient//用于输出是否授权的资源
    private Boolean granted;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_role_resource",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "resource_id", referencedColumnName = "id")})
    @OrderBy(value = "id DESC")
    @Where(clause = "del = false")
    private Set<ResourceEntity> resource = new HashSet<>();

    public RoleEntity() {
    }

    public RoleEntity(Integer orgID, String name, String description) {
        this.orgID = orgID;
        this.name = name;
        this.description = description;
    }

    public RoleEntity(Boolean del, Date last, Date time, Integer operator, Integer orgID, String name, String description, Boolean granted) {
        super(del, last, time, operator);
        this.orgID = orgID;
        this.name = name;
        this.description = description;
        this.granted = granted;
    }

    public String getOrgName() {
        return organization != null ? organization.getCompanyName() : null;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public ArchitectureCateEnum getArchitectureType() {
        return organization != null ? organization.getArchitectureType() : null;
    }

    public void setArchitectureType(ArchitectureCateEnum architectureType) {
        this.architectureType = architectureType;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getGranted() {
        return granted;
    }

    public void setGranted(Boolean granted) {
        this.granted = granted;
    }

    public Set<ResourceEntity> getResource() {
        return resource;
    }

    public void setResource(Set<ResourceEntity> resource) {
        this.resource = resource;
    }

    public Integer getOrgID() {
        return orgID;
    }

    public void setOrgID(Integer orgID) {
        this.orgID = orgID;
    }

    @Override
    public String toString() {
        return "RoleEntity{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", del=" + del +
                ", last=" + last +
                ", time=" + time +
                ", operator=" + operator +
                '}';
    }


}
