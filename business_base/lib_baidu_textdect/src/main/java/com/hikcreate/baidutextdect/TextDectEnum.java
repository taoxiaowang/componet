package com.hikcreate.baidutextdect;

/**
 * 图片识别基本枚举值
 *
 * @author yslei
 * @data 2019/4/2
 * @email leiyongsheng@hikcreate.com
 */
public enum TextDectEnum {
    BASIC_TEXT_DETECT("基本图片"), // 基本图片识别
    ID_CARD_DETECT_FRONT("身份证"), // 身份证正面识别 -- IDCardEntity
    ID_CARD_DETECT_BACK("身份证背面"), // 身份证背面识别 -- IDCardEntity
    VEHICLE_LICENSE_DETECT_MAIN("行驶证"), // 行驶证正页识别 --VehicleLicenseMain
    VEHICLE_LICENSE_DETECT_SUB("行驶证副页"), // 行驶证背副页识别
    DRIVING_LICENSE_DETECT_MAIN("驾驶证"), // 驾驶证正页识别 --DriveLicenseMain
    DRIVING_LICENSE_DETECT_SUN("驾驶证副页"),// 驾驶证副页识别 -- DriveLicenseSub
    BANK_CARD_DETECT("银行卡"),
    PASSPORT_CARD_DETECT("护照");

    private String mName;

    TextDectEnum(String name) {
        this.mName = name;
    }

    public String getName() {
        return mName;
    }
}
