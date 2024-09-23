package com.push.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 * Create DateTime: 2020-04-21 15:12
 *
 * 

 */
@Getter
public enum ShopTypeEnum {

    YiCheng("翼程", "YiCheng", "yiCheng", "yc"),

    NewYiCheng("新翼程", "NewYiCheng", "newYiCheng", "nyc"),

    Hf("Hf", "Hf", "hf", "hf"),

    Sy("Sy", "Sy", "sy", "sy"),

    Test("测试", "Test", "test", "test"),

    Sj("Sj", "Sj", "sj", "sj"),

    KxDistribute("KxDistribute", "KxDistribute", "kxDistribute", "kxDistribute"),

    Happy("Happy","Happy","happy","happy")
    ;


    String name;

    String enName;

    String classPre;

    //路由type
    String type;

    ShopTypeEnum(String name, String enName, String classPre, String type) {
        this.name = name;
        this.enName = enName;
        this.classPre = classPre;
        this.type = type;
    }

    public static ShopTypeEnum getByEnName(String name) {
        if (StringUtils.isNotBlank(name)) {
            for (ShopTypeEnum enu : values()) {
                if (enu.enName.equals(name)) {
                    return enu;
                }
            }
        }
        return null;
    }

    public static ShopTypeEnum getByType(String type) {
        if (StringUtils.isNotBlank(type)) {
            for (ShopTypeEnum enu : values()) {
                if (enu.type.equals(type)) {
                    return enu;
                }
            }
        }
        return null;
    }
}
