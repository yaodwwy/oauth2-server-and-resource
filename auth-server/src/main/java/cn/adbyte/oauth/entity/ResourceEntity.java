package cn.adbyte.oauth.entity;

import cn.adbyte.oauth.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

@Entity
@Cacheable
@Table(name = "t_resource")
public class ResourceEntity extends BaseEntity {
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", updatable = false, insertable = false)
    private ResourceEntity parent;
    @Column(name = "parent_id")
    private Integer pid;
    private ResourceTypeEnum type;
    private String name;
    private String url;
    private String iconCls;
    private Integer moduleSort;
    private Integer menuSort;
    private Integer tabSort;
    private Integer funcSort;
    private Boolean enabled;
    private Boolean belongPm;
    private Boolean belongVendor;
    private Boolean belongCustomer;
    @Transient//用于输出是否授权的资源
    private Boolean granted;
    @Transient//用于输出是否授权的资源
    @JsonProperty("LAY_CHECKED")
    private Boolean LAY_CHECKED;
    @JsonIgnore
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private List<ResourceEntity> children = new ArrayList<>();
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_role_resource",
            joinColumns = {@JoinColumn(name = "resource_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @OrderBy(value = "id DESC")
    @Where(clause = "del = false")
    private Set<RoleEntity> role = new HashSet<>();

    public ResourceEntity() {
    }

    public ResourceEntity(Integer pid, ResourceTypeEnum type, String name, String url, String iconCls,
                          Integer moduleSort, Boolean belongPm, Boolean belongVendor, Boolean belongCustomer) {
        this.pid = pid;
        this.type = type;
        this.name = name;
        this.url = url;
        this.iconCls = iconCls;
        this.moduleSort = moduleSort;
        this.belongPm = belongPm;
        this.belongVendor = belongVendor;
        this.belongCustomer = belongCustomer;
    }

    public ResourceEntity(Integer pid, ResourceTypeEnum type, String name, String url, String iconCls,
                          Integer moduleSort, Integer menuSort, Integer tabSort, Integer funcSort, Boolean enabled,
                          Boolean belongPm, Boolean belongVendor, Boolean belongCustomer) {
        this.pid = pid;
        this.type = type;
        this.name = name;
        this.url = url;
        this.iconCls = iconCls;
        this.moduleSort = moduleSort;
        this.menuSort = menuSort;
        this.tabSort = tabSort;
        this.funcSort = funcSort;
        this.enabled = enabled;
        this.belongPm = belongPm;
        this.belongVendor = belongVendor;
        this.belongCustomer = belongCustomer;
    }

    public Boolean getLAY_CHECKED() {
        LAY_CHECKED = granted;
        return LAY_CHECKED;
    }

    public ResourceEntity(Boolean del, Date last, Date time, Integer operator, Integer pid,
                          ResourceTypeEnum type, String name, String url, String iconCls, Integer moduleSort, Integer menuSort,
                          Integer tabSort, Integer funcSort, Boolean enabled, Boolean granted,
                          List<ResourceEntity> children) {
        super(del, last, time, operator);
        this.pid = pid;
        this.type = type;
        this.name = name;
        this.url = url;
        this.iconCls = iconCls;
        this.moduleSort = moduleSort;
        this.menuSort = menuSort;
        this.tabSort = tabSort;
        this.funcSort = funcSort;
        this.enabled = enabled;
        this.granted = granted;
        this.children = children;
    }

    public Boolean getBelongPm() {
        return belongPm;
    }

    public void setBelongPm(Boolean belongPm) {
        this.belongPm = belongPm;
    }

    public Boolean getBelongVendor() {
        return belongVendor;
    }

    public void setBelongVendor(Boolean belongVendor) {
        this.belongVendor = belongVendor;
    }

    public Boolean getBelongCustomer() {
        return belongCustomer;
    }

    public void setBelongCustomer(Boolean belongCustomer) {
        this.belongCustomer = belongCustomer;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public ResourceEntity getParent() {
        return parent;
    }

    public void setParent(ResourceEntity parent) {
        this.parent = parent;
    }

    public ResourceTypeEnum getType() {
        return type;
    }

    public void setType(ResourceTypeEnum type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Integer getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(Integer menuSort) {
        this.menuSort = menuSort;
    }

    public Integer getTabSort() {
        return tabSort;
    }

    public void setTabSort(Integer tabSort) {
        this.tabSort = tabSort;
    }

    public Integer getFuncSort() {
        return funcSort;
    }

    public void setFuncSort(Integer funcSort) {
        this.funcSort = funcSort;
    }

    public Integer getModuleSort() {
        return moduleSort;
    }

    public void setModuleSort(Integer moduleSort) {
        this.moduleSort = moduleSort;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getGranted() {
        return granted;
    }

    public void setGranted(Boolean granted) {
        this.granted = granted;
    }

    public List<ResourceEntity> getChildren() {
        return children;
    }

    public void setChildren(List<ResourceEntity> children) {
        this.children = children;
    }

    public Set<RoleEntity> getRole() {
        return role;
    }

    public void setRole(Set<RoleEntity> role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "ResourceEntity{" +
                "pid=" + pid +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", iconCls='" + iconCls + '\'' +
                ", moduleSort=" + moduleSort +
                ", menuSort=" + menuSort +
                ", tabSort=" + tabSort +
                ", funcSort=" + funcSort +
                ", enabled=" + enabled +
                ", granted=" + granted +
                ", id=" + id +
                ", del=" + del +
                ", last=" + last +
                ", time=" + time +
                ", operator=" + operator +
                '}';
    }
}
