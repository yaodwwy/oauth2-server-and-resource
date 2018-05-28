package cn.adbyte.oauth.entity;

public enum ResourceTypeEnum {

    MENU(0, "菜单"),
    TABS(1, "选项卡"),
    FUNC(2, "功能"),
    ;

    public Integer code;
    public String des;

    ResourceTypeEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public static ResourceTypeEnum get(Integer code) {
        for (ResourceTypeEnum c : ResourceTypeEnum.values()) {
            if (c.code.toString().equals(code.toString())) {
                return c;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public String toString() {
        return "code:" + code + ", des:" + des;
    }

}
