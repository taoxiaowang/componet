package com.hikcreate.baidutextdect.bean;

import java.io.Serializable;

/**
 * 驾驶证正页图形识别结果
 *
 * @author yslei
 * @data 2019/4/8
 * @email leiyongsheng@hikcreate.com
 */
public class DriveLicenseMain implements Serializable {
    private String licenseNo; //驾驶证证号
    private String licenseName; //驾驶证姓名
    private int licenseGender; //驾驶证性别
    private String licenseAddress; //驾驶证地址
    private String licenseNation; //驾驶证国籍
    private String licenseBirth; //驾驶证出生日期
    private String licenseFirstIssue; //驾驶证领证日期
    private String licenseClass; //驾驶证准驾车型
    private String licenseValidStart; //驾驶证有效起始期
    private String licenseValidEnd; //驾驶证有效结束期

    public DriveLicenseMain() {
    }

    public DriveLicenseMain(String licenseNo, String licenseName, int licenseGender,
                            String licenseAddress, String licenseNation, String licenseBirth,
                            String licenseFirstIssue, String licenseClass,
                            String licenseValidStart, String licenseValidEnd) {
        this.licenseNo = licenseNo;
        this.licenseName = licenseName;
        this.licenseGender = licenseGender;
        this.licenseAddress = licenseAddress;
        this.licenseNation = licenseNation;
        this.licenseBirth = licenseBirth;
        this.licenseFirstIssue = licenseFirstIssue;
        this.licenseClass = licenseClass;
        this.licenseValidStart = licenseValidStart;
        this.licenseValidEnd = licenseValidEnd;
    }

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

    public int getLicenseGender() {
        return licenseGender;
    }

    public void setLicenseGender(int licenseGender) {
        this.licenseGender = licenseGender;
    }

    public String getLicenseAddress() {
        return licenseAddress;
    }

    public void setLicenseAddress(String licenseAddress) {
        this.licenseAddress = licenseAddress;
    }

    public String getLicenseNation() {
        return licenseNation;
    }

    public void setLicenseNation(String licenseNation) {
        this.licenseNation = licenseNation;
    }

    public String getLicenseBirth() {
        return licenseBirth;
    }

    public void setLicenseBirth(String licenseBirth) {
        this.licenseBirth = licenseBirth;
    }

    public String getLicenseFirstIssue() {
        return licenseFirstIssue;
    }

    public void setLicenseFirstIssue(String licenseFirstIssue) {
        this.licenseFirstIssue = licenseFirstIssue;
    }

    public String getLicenseClass() {
        return licenseClass;
    }

    public void setLicenseClass(String licenseClass) {
        this.licenseClass = licenseClass;
    }

    public String getLicenseValidStart() {
        return licenseValidStart;
    }

    public void setLicenseValidStart(String licenseValidStart) {
        this.licenseValidStart = licenseValidStart;
    }

    public String getLicenseValidEnd() {
        return licenseValidEnd;
    }

    public void setLicenseValidEnd(String licenseValidEnd) {
        this.licenseValidEnd = licenseValidEnd;
    }

    @Override
    public String toString() {
        return "DriveLicenseMain{" +
                "licenseNo='" + licenseNo + '\'' +
                ", licenseName='" + licenseName + '\'' +
                ", licenseGender='" + licenseGender + '\'' +
                ", licenseAddress='" + licenseAddress + '\'' +
                ", licenseNation='" + licenseNation + '\'' +
                ", licenseBirth='" + licenseBirth + '\'' +
                ", licenseFirstIssue='" + licenseFirstIssue + '\'' +
                ", licenseClass='" + licenseClass + '\'' +
                ", licenseValidStart='" + licenseValidStart + '\'' +
                ", licenseValidEnd='" + licenseValidEnd + '\'' +
                '}';
    }
}
