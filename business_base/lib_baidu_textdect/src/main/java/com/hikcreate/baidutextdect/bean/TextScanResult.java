package com.hikcreate.baidutextdect.bean;

import com.hikcreate.baidutextdect.TextDectEnum;
import com.hikcreate.baidutextdect.util.TextDecFileUtil;

import java.io.Serializable;

/**
 * 图片文本扫描结果
 *
 * @author yslei
 * @data 2019/4/12
 * @email leiyongsheng@hikcreate.com
 */
public class TextScanResult implements Serializable {

    private TextDectEnum mType;
    private String mImagePath;
    private DriveLicenseMain mDriveLicenseMain;
    private DriveLicenseSub mDriveLicenseSub;
    private IDCardEntity mIDCardEntity;
    private VehicleLicenseMain mVehicleLicenseMain;

    public TextScanResult(TextDectEnum type) {
        mImagePath = TextDecFileUtil.getImageCacheFilePath();
        mType = type;
    }

    public TextScanResult(TextDectEnum type, DriveLicenseMain driveLicenseMain) {
        this(type);
        mDriveLicenseMain = driveLicenseMain;
    }

    public TextScanResult(TextDectEnum type, DriveLicenseSub driveLicenseSub) {
        this(type);
        mDriveLicenseSub = driveLicenseSub;
    }

    public TextScanResult(TextDectEnum type, IDCardEntity idCardEntity) {
        this(type);
        mIDCardEntity = idCardEntity;
    }

    public TextScanResult(TextDectEnum type, VehicleLicenseMain vehicleLicenseMain) {
        this(type);
        mVehicleLicenseMain = vehicleLicenseMain;
    }

    public TextDectEnum getType() {
        return mType;
    }

    public void setType(TextDectEnum type) {
        mType = type;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public DriveLicenseMain getDriveLicenseMain() {
        return mDriveLicenseMain;
    }

    public void setDriveLicenseMain(DriveLicenseMain driveLicenseMain) {
        mDriveLicenseMain = driveLicenseMain;
    }

    public DriveLicenseSub getDriveLicenseSub() {
        return mDriveLicenseSub;
    }

    public void setDriveLicenseSub(DriveLicenseSub driveLicenseSub) {
        mDriveLicenseSub = driveLicenseSub;
    }

    public IDCardEntity getIDCardEntity() {
        return mIDCardEntity;
    }

    public void setIDCardEntity(IDCardEntity IDCardEntity) {
        mIDCardEntity = IDCardEntity;
    }

    public VehicleLicenseMain getVehicleLicenseMain() {
        return mVehicleLicenseMain;
    }

    public void setVehicleLicenseMain(VehicleLicenseMain vehicleLicenseMain) {
        mVehicleLicenseMain = vehicleLicenseMain;
    }

    @Override
    public String toString() {
        return "TextScanResult{" +
                "mType=" + mType +
                ", mDriveLicenseMain=" + mDriveLicenseMain +
                ", mDriveLicenseSub=" + mDriveLicenseSub +
                ", mIDCardEntity=" + mIDCardEntity +
                ", mVehicleLicenseMain=" + mVehicleLicenseMain +
                '}';
    }
}
