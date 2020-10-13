package com.daxi.dxdriverapplication.bean;



import java.util.List;

public class ModeBaseUrlBean extends BaseBean {


    /**
     * code : 200
     * data : {"apiHost":{"device":"http://192.168.88.26:9007","eva":"http://192.168.88.26:9004","finance":"http://192.168.88.26:9005","hrs":"http://192.168.88.26:9008","materials":"http://192.168.88.26:9003","mix":"http://192.168.88.26:9002","proplans":"http://192.168.88.26:9006","user":"http://192.168.88.26:9001"},"createTime":"2019-09-04","device":1,"downloadUrl":"https://dx185.com/app/1.0.13/shuzitumu.apk","fixInfo":["优化拌和站"],"force":false,"id":14,"isTest":true,"name":"数字土木","version":"1.0.13"}
     * msg : SUCCESS
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {

        /**
         * apiHost : {"device":"http://192.168.88.26:9007","eva":"http://192.168.88.26:9004","finance":"http://192.168.88.26:9005","hrs":"http://192.168.88.26:9008","materials":"http://192.168.88.26:9003","mix":"http://192.168.88.26:9002","proplans":"http://192.168.88.26:9006","user":"http://192.168.88.26:9001"}
         * createTime : 2019-09-04
         * device : 1
         * downloadUrl : https://dx185.com/app/1.0.13/shuzitumu.apk
         * fixInfo : ["优化拌和站"]
         * force : false
         * id : 14
         * isTest : true
         * name : 数字土木
         * version : 1.0.13
         */

        private ApiHostBean apiHost;
        private String createTime;
        private int device;
        private String downloadUrl;
        private boolean force;
        private int id;
        private boolean isTest;
        private String name;
        private String version;
        private List<String> fixInfo;

        public ApiHostBean getApiHost() {
            return apiHost;
        }

        public void setApiHost(ApiHostBean apiHost) {
            this.apiHost = apiHost;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getDevice() {
            return device;
        }

        public void setDevice(int device) {
            this.device = device;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public boolean isForce() {
            return force;
        }

        public void setForce(boolean force) {
            this.force = force;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIsTest() {
            return isTest;
        }

        public void setIsTest(boolean isTest) {
            this.isTest = isTest;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public List<String> getFixInfo() {
            return fixInfo;
        }

        public void setFixInfo(List<String> fixInfo) {
            this.fixInfo = fixInfo;
        }

        public static class ApiHostBean {
            /**
             * device : http://192.168.88.26:9007
             * eva : http://192.168.88.26:9004
             * finance : http://192.168.88.26:9005
             * hrs : http://192.168.88.26:9008
             * materials : http://192.168.88.26:9003
             * mix : http://192.168.88.26:9002
             * proplans : http://192.168.88.26:9006
             * user : http://192.168.88.26:9001
             */

            private String device;
            private String eva;
            private String finance;
            private String hrs;
            private String materials;
            private String mix;
            private String proplans;
            private String user;

            public String getDevice() {
                return device;
            }

            public void setDevice(String device) {
                this.device = device;
            }

            public String getEva() {
                return eva;
            }

            public void setEva(String eva) {
                this.eva = eva;
            }

            public String getFinance() {
                return finance;
            }

            public void setFinance(String finance) {
                this.finance = finance;
            }

            public String getHrs() {
                return hrs;
            }

            public void setHrs(String hrs) {
                this.hrs = hrs;
            }

            public String getMaterials() {
                return materials;
            }

            public void setMaterials(String materials) {
                this.materials = materials;
            }

            public String getMix() {
                return mix;
            }

            public void setMix(String mix) {
                this.mix = mix;
            }

            public String getProplans() {
                return proplans;
            }

            public void setProplans(String proplans) {
                this.proplans = proplans;
            }

            public String getUser() {
                return user;
            }

            public void setUser(String user) {
                this.user = user;
            }
        }
    }
}
