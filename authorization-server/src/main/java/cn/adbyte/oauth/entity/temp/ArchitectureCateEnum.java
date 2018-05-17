package cn.adbyte.oauth.entity.temp;

/**
 * Created by Adam Yao on 2017/12/12.
 * 系统架构类别
 */
public enum ArchitectureCateEnum {

    PM(0, "云平台"),
    VENDOR(1, "供应商"),
    CUSTOMER(2, "客户");

    public Number code;
    public String des;

    ArchitectureCateEnum(Number code, String des) {
        this.code = code;
        this.des = des;
    }

    public static ArchitectureCateEnum get(Integer code) {

        for (ArchitectureCateEnum c : ArchitectureCateEnum.values()) {
            if (c.code.toString().equals(code.toString())) {
                return c;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "code:" + code + ", des:" + des;
    }


}
