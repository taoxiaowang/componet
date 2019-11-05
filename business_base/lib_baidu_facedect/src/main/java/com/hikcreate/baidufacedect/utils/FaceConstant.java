package com.hikcreate.baidufacedect.utils;

public class FaceConstant {

    public static class ImageType {
        // 图片的base64值，base64编码后的图片数据，编码后的图片大小不超过2M；
        public static final String BASE64 = "BASE64";
        // 图片的 URL地址( 可能由于网络等原因导致下载图片时间过长)；
        public static final String URL = "URL";
        // 人脸图片的唯一标识，调用人脸检测接口时，会为每个人脸图片赋予一个唯一的FACE_TOKEN，同一张图片多次检测得到的FACE_TOKEN是同一个。
        public static final String FACE_TOKEN = "FACE_TOKEN";
    }

    public static class FaceType { //默认LIVE
        // 表示生活照：通常为手机、相机拍摄的人像图片、或从网络获取的人像图片等
        public static final String LIVE = "LIVE";
        // 表示身份证芯片照：二代身份证内置芯片中的人像照片
        public static final String IDCARD = "IDCARD";
        // 表示带水印证件照：一般为带水印的小图，如公安网小图
        public static final String WATERMARK = "WATERMARK";
        // 表示证件照片：如拍摄的身份证、工卡、护照、学生证等证件图片
        public static final String CERT = "CERT";
    }

    public static class QualityControl { // 默认 NONE
        // 不进行控制
        public static final String NONE = "NONE";
        // 较低的质量要求
        public static final String LOW = "LOW";
        // 一般的质量要求
        public static final String NORMAL = "NORMAL";
        // 较高的质量要求
        public static final String HIGH = "HIGH";
    }

    public static class LivenessControl { // 默认 NONE
        // 不进行控制
        public static final String NONE = "NONE";
        // 较低的活体要求(高通过率 低攻击拒绝率)
        public static final String LOW = "LOW";
        // 一般的活体要求(平衡的攻击拒绝率, 通过率)
        public static final String NORMAL = "NORMAL";
        // 较高的活体要求(高攻击拒绝率 低通过率)
        public static final String HIGH = "HIGH";
    }
}
