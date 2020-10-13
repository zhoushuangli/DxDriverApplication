package com.daxi.dxdriverapplication.bean;

public class AccountNumberBean extends BaseBean {


    /**
     * code : 200
     * data : {"Token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZXNvdXJjZSI6IntcInBob25lXCI6XCIxODYwMTc4MzE3MlwiLFwiZXhwaXJlXCI6MTU2OTcyMjM5NjU3Mn0iLCJleHAiOjE1Njk3MjIzOTZ9.GYKUkM9Ogn5GdcWJxZxf4tYkj5JYIroeRUKhBAgwlJ8","unitOrProject":"project"}
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
         * Token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZXNvdXJjZSI6IntcInBob25lXCI6XCIxODYwMTc4MzE3MlwiLFwiZXhwaXJlXCI6MTU2OTcyMjM5NjU3Mn0iLCJleHAiOjE1Njk3MjIzOTZ9.GYKUkM9Ogn5GdcWJxZxf4tYkj5JYIroeRUKhBAgwlJ8
         * unitOrProject : project
         */

        private String Token;
        private String unitOrProject;

        public String getToken() {
            return Token;
        }

        public void setToken(String Token) {
            this.Token = Token;
        }

        public String getUnitOrProject() {
            return unitOrProject;
        }

        public void setUnitOrProject(String unitOrProject) {
            this.unitOrProject = unitOrProject;
        }
    }
}
