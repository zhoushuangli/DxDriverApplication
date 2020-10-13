package com.daxi.dxdriverapplication.bean;

public class UserInfoBean extends BaseBean {


    /**
     * code : 200
     * msg : SUCCESS
     * data : {"jobName":"运输车司机","simpleName":"大瑞二","phone":"12345000023","isUnit":false,"proid":3,"mixAttribute":2,"name":"中铁十二局集团有限公司大瑞铁路工程项目经理二分部","id":"c983acc1cd294d8ca270b003d6ab7560","userName":"运输车四号司机","departName":"拌和站"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * jobName : 运输车司机
         * simpleName : 大瑞二
         * phone : 12345000023
         * isUnit : false
         * proid : 3
         * mixAttribute : 2
         * name : 中铁十二局集团有限公司大瑞铁路工程项目经理二分部
         * id : c983acc1cd294d8ca270b003d6ab7560
         * userName : 运输车四号司机
         * departName : 拌和站
         */

        private String jobName;
        private String simpleName;
        private String phone;
        private boolean isUnit;
        private int proid;
        private int mixAttribute;
        private String name;
        private String id;
        private String userName;
        private String departName;

        public String getJobName() {
            return jobName;
        }

        public void setJobName(String jobName) {
            this.jobName = jobName;
        }

        public String getSimpleName() {
            return simpleName;
        }

        public void setSimpleName(String simpleName) {
            this.simpleName = simpleName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public boolean isIsUnit() {
            return isUnit;
        }

        public void setIsUnit(boolean isUnit) {
            this.isUnit = isUnit;
        }

        public int getProid() {
            return proid;
        }

        public void setProid(int proid) {
            this.proid = proid;
        }

        public int getMixAttribute() {
            return mixAttribute;
        }

        public void setMixAttribute(int mixAttribute) {
            this.mixAttribute = mixAttribute;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getDepartName() {
            return departName;
        }

        public void setDepartName(String departName) {
            this.departName = departName;
        }
    }
}
