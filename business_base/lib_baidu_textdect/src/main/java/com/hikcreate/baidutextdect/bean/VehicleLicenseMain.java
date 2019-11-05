package com.hikcreate.baidutextdect.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 行驶证正页图形识别结果
 *
 * @author yslei
 * @data 2019/4/8
 * @email leiyongsheng@hikcreate.com
 */
public class VehicleLicenseMain implements Serializable {

    /**
     * 对象不为空时，以下属性均可能为空
     */
    private String licenseAddress; // "行驶证地址"
    private String licenseModel; // "品牌型号";
    private String licenseIssueDate; // "发证日期";
    private String licenseUseCharacter; // "使用性质";
    private String licenseEngineNo; // "发动机号码";
    private String licensePlateNo; // "号牌号码";
    private String licenseOwner; // "所有人";
    private String licenseRegisterDate; // "注册日期";
    private String licenseVIN; // "车辆识别代号";
    private String licenseVehicleType; // "车辆类型";

    public VehicleLicenseMain(){
    }

    public VehicleLicenseMain(String licenseAddress, String licenseModel, String licenseIssueDate,
                              String licenseUseCharacter, String licenseEngineNo, String licensePlateNo,
                              String licenseOwner, String licenseRegisterDate, String licenseVIN, String licenseVehicleType) {
        this.licenseAddress = licenseAddress;
        this.licenseModel = licenseModel;
        this.licenseIssueDate = licenseIssueDate;
        this.licenseUseCharacter = licenseUseCharacter;
        this.licenseEngineNo = licenseEngineNo;
        this.licensePlateNo = licensePlateNo;
        this.licenseOwner = licenseOwner;
        this.licenseRegisterDate = licenseRegisterDate;
        this.licenseVIN = licenseVIN;
        this.licenseVehicleType = licenseVehicleType;
    }

    public String getLicenseAddress() {
        return licenseAddress;
    }

    public void setLicenseAddress(String licenseAddress) {
        this.licenseAddress = licenseAddress;
    }

    public String getLicenseModel() {
        return licenseModel;
    }

    public void setLicenseModel(String licenseModel) {
        this.licenseModel = licenseModel;
    }

    public String getLicenseIssueDate() {
        return licenseIssueDate;
    }

    public void setLicenseIssueDate(String licenseIssueDate) {
        this.licenseIssueDate = licenseIssueDate;
    }

    public String getLicenseUseCharacter() {
        return licenseUseCharacter;
    }

    public void setLicenseUseCharacter(String licenseUseCharacter) {
        this.licenseUseCharacter = licenseUseCharacter;
    }

    public String getLicenseEngineNo() {
        return licenseEngineNo;
    }

    public void setLicenseEngineNo(String licenseEngineNo) {
        this.licenseEngineNo = licenseEngineNo;
    }

    public String getLicensePlateNo() {
        return licensePlateNo;
    }

    public void setLicensePlateNo(String licensePlateNo) {
        this.licensePlateNo = licensePlateNo;
    }

    public String getLicenseOwner() {
        return licenseOwner;
    }

    public void setLicenseOwner(String licenseOwner) {
        this.licenseOwner = licenseOwner;
    }

    public String getLicenseRegisterDate() {
        return licenseRegisterDate;
    }

    public void setLicenseRegisterDate(String licenseRegisterDate) {
        this.licenseRegisterDate = licenseRegisterDate;
    }

    public String getLicenseVIN() {
        return licenseVIN;
    }

    public void setLicenseVIN(String licenseVIN) {
        this.licenseVIN = licenseVIN;
    }

    public String getLicenseVehicleType() {
        return licenseVehicleType;
    }

    public void setLicenseVehicleType(String licenseVehicleType) {
        this.licenseVehicleType = licenseVehicleType;
    }

    @Override
    public String toString() {
        return "VehicleLicenseMain{" +
                "licenseAddress='" + licenseAddress + '\'' +
                ", licenseModel='" + licenseModel + '\'' +
                ", licenseIssueDate='" + licenseIssueDate + '\'' +
                ", licenseUseCharacter='" + licenseUseCharacter + '\'' +
                ", licenseEngineNo='" + licenseEngineNo + '\'' +
                ", licensePlateNo='" + licensePlateNo + '\'' +
                ", licenseOwner='" + licenseOwner + '\'' +
                ", licenseRegisterDate='" + licenseRegisterDate + '\'' +
                ", licenseVIN='" + licenseVIN + '\'' +
                ", licenseVehicleType='" + licenseVehicleType + '\'' +
                '}';
    }
}
