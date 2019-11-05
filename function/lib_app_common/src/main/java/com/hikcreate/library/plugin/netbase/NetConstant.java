package com.hikcreate.library.plugin.netbase;

/**
 * to do
 *
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public class NetConstant {
    public static final String TAG = "hik_network";

    public static class ResponseCode {
        public static final int CODE_SUCCESS = 0;
        public static final int CODE_ERROR = 1;
        public static final int CODE_KICK_OUT = 3;
        public static final int CODE_RESPONSE_SUCCESS = 100000;
        public static final int CODE_SUCCESS_LOCAL = 999;
    }

    public static class RequestTimeOut {
        public static final String CONNECT_TIMEOUT = "CONNECT_TIMEOUT";
        public static final String READ_TIMEOUT = "READ_TIMEOUT";
        public static final String WRITE_TIMEOUT = "WRITE_TIMEOUT";
    }

    public static class MultiDomain {
        public static final String BASE_URL = "base_url";
    }

    public static class MiniType {
        //MIME Type类型被定义在Content-Type header中。每个MIME类型由3部分组成：Content-Type: [type]/[subtype]。前者是数据大类别，后者定义具体的种类。
        public static final String TEXT = "text";//用于标准化地表示的文本信息，文本消息可以是多种字符集和或者多种格式的；
        public static final String MULTIPART = "multipart";//用于连接消息体的多个部分构成一个消息，这些部分可以是不同类型的数据；
        public static final String APPLICATION = "application";//用于传输应用程序数据或者二进制数据；
        public static final String MESSAGE = "message";//用于包装一个E-mail消息；
        public static final String IMAGE = "image";//用于传输静态图片数据；
        public static final String AUDIO = "audio";//用于传输音频或者音声数据；
        public static final String VIDEO = "video";//用于传输动态影像数据，可以是与音频编辑在一起的视频数据格式。
    }
}
