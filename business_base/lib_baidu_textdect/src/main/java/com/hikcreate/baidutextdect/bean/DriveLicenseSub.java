package com.hikcreate.baidutextdect.bean;

import java.io.Serializable;

/**
 * 驾驶证副页图形识别结果
 *
 * @author yslei
 * @data 2019/4/8
 * @email leiyongsheng@hikcreate.com
 */
public class DriveLicenseSub implements Serializable {
    private String licenseNo; //驾驶证证号
    private String licenseName; //驾驶证姓名
    private String licenseId; //驾驶证档案编号
    private String licenseRecord; //驾驶证记录

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getLicenseName() {
        return licenseName;
    }

    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        String stri4 = licenseId.replaceAll("[\\p{P}\\p{Punct}]", "");
        this.licenseId = stri4;
    }

    public String getLicenseRecord() {
        return licenseRecord;
    }

    public void setLicenseRecord(String licenseRecord) {
        this.licenseRecord = licenseRecord;
    }

    @Override
    public String toString() {
        return "DriveLicenseSub{" +
                "licenseNo='" + licenseNo + '\'' +
                ", licenseName='" + licenseName + '\'' +
                ", licenseId='" + licenseId + '\'' +
                ", licenseRecord='" + licenseRecord + '\'' +
                '}';
    }
}
