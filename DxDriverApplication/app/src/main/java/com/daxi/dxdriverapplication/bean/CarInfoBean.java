package com.daxi.dxdriverapplication.bean;

import java.util.List;

public class CarInfoBean extends BaseBean {


    /**
     * code : 200
     * data : {"bindNum":"2","par":[{"bindingDevice":"","carId":1572246808578,"carNumber":"1","isBinding":2},{"bindingDevice":"imei352538101165176","carId":1572246821877,"carNumber":"2","isBinding":1},{"bindingDevice":"","carId":1572246837700,"carNumber":"3","isBinding":2},{"bindingDevice":"","carId":1572246856885,"carNumber":"4","isBinding":2}]}
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
         * bindNum : 2
         * par : [{"bindingDevice":"","carId":1572246808578,"carNumber":"1","isBinding":2},{"bindingDevice":"imei352538101165176","carId":1572246821877,"carNumber":"2","isBinding":1},{"bindingDevice":"","carId":1572246837700,"carNumber":"3","isBinding":2},{"bindingDevice":"","carId":1572246856885,"carNumber":"4","isBinding":2}]
         */

        private String bindNum;
        private List<ParBean> par;

        public String getBindNum() {
            return bindNum;
        }

        public void setBindNum(String bindNum) {
            this.bindNum = bindNum;
        }

        public List<ParBean> getPar() {
            return par;
        }

        public void setPar(List<ParBean> par) {
            this.par = par;
        }

        public static class ParBean {
            /**
             * bindingDevice :
             * carId : 1572246808578
             * carNumber : 1
             * isBinding : 2
             */

            private String bindingDevice;
            private long carId;
            private String carNumber;
            private int isBinding;

            public String getBindingDevice() {
                return bindingDevice;
            }

            public void setBindingDevice(String bindingDevice) {
                this.bindingDevice = bindingDevice;
            }

            public long getCarId() {
                return carId;
            }

            public void setCarId(long carId) {
                this.carId = carId;
            }

            public String getCarNumber() {
                return carNumber;
            }

            public void setCarNumber(String carNumber) {
                this.carNumber = carNumber;
            }

            public int getIsBinding() {
                return isBinding;
            }

            public void setIsBinding(int isBinding) {
                this.isBinding = isBinding;
            }
        }
    }
}
