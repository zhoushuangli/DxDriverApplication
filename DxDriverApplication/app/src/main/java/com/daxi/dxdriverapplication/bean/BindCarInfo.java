package com.daxi.dxdriverapplication.bean;

import java.util.List;

public class BindCarInfo {
    /**
     * code : 200
     * data : {"carNumber":"2","carOrbit":{"mixName":"1#拌和站","mixXy":"34.231854,108.877761","pourXy":"34.222038,108.917123","xy":[{"time":"16:48","x":"34.231854","y":"108.877761"},{"time":"17:15","x":"108.879233","y":"34.232039"},{"time":"17:15","x":"108.879233","y":"34.232039"},{"time":"17:15","x":"108.879233","y":"34.232039"}]},"isBingding":"1","maxLevel":"2","mixXy":"34.231854,108.877761","notice":{"info":{"canCube":"10","carId":1588042464497,"carNumber":"1","unit":2}},"orderCar":{"carNumber":"2","did":1577064770949,"dutyPerson":"周晨曦","dutyPhone":"12340000031","id":1577064889000,"isRead":1,"laborForceName":"","laborForcePhone":"","laborForceStr":"","mixCube":"0","orderCreateTime":"2019-12-23 09:32","orderNumber":"T170949968","partName":"对方水电费水电费/场地清理/清理与挖掘","status":14,"unit":1},"par":[{"carNumber":"2","carid":1572246821877,"id":1577064889000,"isSelect":"1","level":1,"partName":"对方水电费水电费/场地清理/清理与挖掘"},{"carNumber":"1","carid":1572246808578,"id":1577067049548,"isSelect":"2","level":2,"partName":"对方水电费水电费/特殊地区路基处理/盐渍土路基处理"}],"pourSiteXy":"34.222038,108.917123"}
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
         * carNumber : 2
         * carOrbit : {"mixName":"1#拌和站","mixXy":"34.231854,108.877761","pourXy":"34.222038,108.917123","xy":[{"time":"16:48","x":"34.231854","y":"108.877761"},{"time":"17:15","x":"108.879233","y":"34.232039"},{"time":"17:15","x":"108.879233","y":"34.232039"},{"time":"17:15","x":"108.879233","y":"34.232039"}]}
         * isBingding : 1
         * maxLevel : 2
         * mixXy : 34.231854,108.877761
         * notice : {"info":{"canCube":"10","carId":1588042464497,"carNumber":"1","unit":2}}
         * orderCar : {"carNumber":"2","did":1577064770949,"dutyPerson":"周晨曦","dutyPhone":"12340000031","id":1577064889000,"isRead":1,"laborForceName":"","laborForcePhone":"","laborForceStr":"","mixCube":"0","orderCreateTime":"2019-12-23 09:32","orderNumber":"T170949968","partName":"对方水电费水电费/场地清理/清理与挖掘","status":14,"unit":1}
         * par : [{"carNumber":"2","carid":1572246821877,"id":1577064889000,"isSelect":"1","level":1,"partName":"对方水电费水电费/场地清理/清理与挖掘"},{"carNumber":"1","carid":1572246808578,"id":1577067049548,"isSelect":"2","level":2,"partName":"对方水电费水电费/特殊地区路基处理/盐渍土路基处理"}]
         * pourSiteXy : 34.222038,108.917123
         */

        private String carNumber;
        private CarOrbitBean carOrbit;
        private String isBingding;
        private String maxLevel;
        private String mixXy;
        private NoticeBean notice;
        private OrderCarBean orderCar;
        private String pourSiteXy;
        private List<ParBean> par;

        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }

        public CarOrbitBean getCarOrbit() {
            return carOrbit;
        }

        public void setCarOrbit(CarOrbitBean carOrbit) {
            this.carOrbit = carOrbit;
        }

        public String getIsBingding() {
            return isBingding;
        }

        public void setIsBingding(String isBingding) {
            this.isBingding = isBingding;
        }

        public String getMaxLevel() {
            return maxLevel;
        }

        public void setMaxLevel(String maxLevel) {
            this.maxLevel = maxLevel;
        }

        public String getMixXy() {
            return mixXy;
        }

        public void setMixXy(String mixXy) {
            this.mixXy = mixXy;
        }

        public NoticeBean getNotice() {
            return notice;
        }

        public void setNotice(NoticeBean notice) {
            this.notice = notice;
        }

        public OrderCarBean getOrderCar() {
            return orderCar;
        }

        public void setOrderCar(OrderCarBean orderCar) {
            this.orderCar = orderCar;
        }

        public String getPourSiteXy() {
            return pourSiteXy;
        }

        public void setPourSiteXy(String pourSiteXy) {
            this.pourSiteXy = pourSiteXy;
        }

        public List<ParBean> getPar() {
            return par;
        }

        public void setPar(List<ParBean> par) {
            this.par = par;
        }

        public static class CarOrbitBean {
            /**
             * mixName : 1#拌和站
             * mixXy : 34.231854,108.877761
             * pourXy : 34.222038,108.917123
             * xy : [{"time":"16:48","x":"34.231854","y":"108.877761"},{"time":"17:15","x":"108.879233","y":"34.232039"},{"time":"17:15","x":"108.879233","y":"34.232039"},{"time":"17:15","x":"108.879233","y":"34.232039"}]
             */

            private String mixName;
            private String mixXy;
            private String pourXy;
            private List<XyBean> xy;

            public String getMixName() {
                return mixName;
            }

            public void setMixName(String mixName) {
                this.mixName = mixName;
            }

            public String getMixXy() {
                return mixXy;
            }

            public void setMixXy(String mixXy) {
                this.mixXy = mixXy;
            }

            public String getPourXy() {
                return pourXy;
            }

            public void setPourXy(String pourXy) {
                this.pourXy = pourXy;
            }

            public List<XyBean> getXy() {
                return xy;
            }

            public void setXy(List<XyBean> xy) {
                this.xy = xy;
            }

            public static class XyBean {
                /**
                 * time : 16:48
                 * x : 34.231854
                 * y : 108.877761
                 */

                private String time;
                private String x;
                private String y;

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getX() {
                    return x;
                }

                public void setX(String x) {
                    this.x = x;
                }

                public String getY() {
                    return y;
                }

                public void setY(String y) {
                    this.y = y;
                }
            }
        }

        public static class NoticeBean {
            /**
             * info : {"canCube":"10","carId":1588042464497,"carNumber":"1","unit":2}
             */

            private InfoBean info;

            public InfoBean getInfo() {
                return info;
            }

            public void setInfo(InfoBean info) {
                this.info = info;
            }

            public static class InfoBean {
                /**
                 * canCube : 10
                 * carId : 1588042464497
                 * carNumber : 1
                 * unit : 2
                 */

                private String canCube;
                private long carId;
                private String carNumber;
                private int unit;

                public String getCanCube() {
                    return canCube;
                }

                public void setCanCube(String canCube) {
                    this.canCube = canCube;
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

                public int getUnit() {
                    return unit;
                }

                public void setUnit(int unit) {
                    this.unit = unit;
                }
            }
        }

        public static class OrderCarBean {
            /**
             * carNumber : 2
             * did : 1577064770949
             * dutyPerson : 周晨曦
             * dutyPhone : 12340000031
             * id : 1577064889000
             * isRead : 1
             * laborForceName :
             * laborForcePhone :
             * laborForceStr :
             * mixCube : 0
             * orderCreateTime : 2019-12-23 09:32
             * orderNumber : T170949968
             * partName : 对方水电费水电费/场地清理/清理与挖掘
             * status : 14
             * unit : 1
             */

            private String carNumber;
            private long did;
            private String dutyPerson;
            private String dutyPhone;
            private long id;
            private int isRead;
            private String laborForceName;
            private String laborForcePhone;
            private String laborForceStr;
            private String mixCube;
            private String orderCreateTime;
            private String orderNumber;
            private String partName;
            private int status;
            private int unit;

            public String getCarNumber() {
                return carNumber;
            }

            public void setCarNumber(String carNumber) {
                this.carNumber = carNumber;
            }

            public long getDid() {
                return did;
            }

            public void setDid(long did) {
                this.did = did;
            }

            public String getDutyPerson() {
                return dutyPerson;
            }

            public void setDutyPerson(String dutyPerson) {
                this.dutyPerson = dutyPerson;
            }

            public String getDutyPhone() {
                return dutyPhone;
            }

            public void setDutyPhone(String dutyPhone) {
                this.dutyPhone = dutyPhone;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public int getIsRead() {
                return isRead;
            }

            public void setIsRead(int isRead) {
                this.isRead = isRead;
            }

            public String getLaborForceName() {
                return laborForceName;
            }

            public void setLaborForceName(String laborForceName) {
                this.laborForceName = laborForceName;
            }

            public String getLaborForcePhone() {
                return laborForcePhone;
            }

            public void setLaborForcePhone(String laborForcePhone) {
                this.laborForcePhone = laborForcePhone;
            }

            public String getLaborForceStr() {
                return laborForceStr;
            }

            public void setLaborForceStr(String laborForceStr) {
                this.laborForceStr = laborForceStr;
            }

            public String getMixCube() {
                return mixCube;
            }

            public void setMixCube(String mixCube) {
                this.mixCube = mixCube;
            }

            public String getOrderCreateTime() {
                return orderCreateTime;
            }

            public void setOrderCreateTime(String orderCreateTime) {
                this.orderCreateTime = orderCreateTime;
            }

            public String getOrderNumber() {
                return orderNumber;
            }

            public void setOrderNumber(String orderNumber) {
                this.orderNumber = orderNumber;
            }

            public String getPartName() {
                return partName;
            }

            public void setPartName(String partName) {
                this.partName = partName;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getUnit() {
                return unit;
            }

            public void setUnit(int unit) {
                this.unit = unit;
            }
        }

        public static class ParBean {
            /**
             * carNumber : 2
             * carid : 1572246821877
             * id : 1577064889000
             * isSelect : 1
             * level : 1
             * partName : 对方水电费水电费/场地清理/清理与挖掘
             */

            private String carNumber;
            private long carid;
            private long id;
            private String isSelect;
            private int level;
            private String partName;

            public String getCarNumber() {
                return carNumber;
            }

            public void setCarNumber(String carNumber) {
                this.carNumber = carNumber;
            }

            public long getCarid() {
                return carid;
            }

            public void setCarid(long carid) {
                this.carid = carid;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getIsSelect() {
                return isSelect;
            }

            public void setIsSelect(String isSelect) {
                this.isSelect = isSelect;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public String getPartName() {
                return partName;
            }

            public void setPartName(String partName) {
                this.partName = partName;
            }
        }
    }

//
//    /**
//     * code : 200
//     * data : {"carNumber":"2","carOrbit":{"mixName":"1#拌和站","mixXy":"34.231854,108.877761","pourXy":"34.222038,108.917123","xy":[{"time":"16:48","x":"34.231854","y":"108.877761"},{"time":"17:15","x":"108.879233","y":"34.232039"},{"time":"17:15","x":"108.879233","y":"34.232039"},{"time":"17:15","x":"108.879233","y":"34.232039"}]},"isBingding":"1","maxLevel":"2","mixXy":"34.231854,108.877761","orderCar":{"carNumber":"2","did":1577064770949,"dutyPerson":"周晨曦","dutyPhone":"12340000031","id":1577064889000,"isRead":1,"laborForceName":"","laborForcePhone":"","laborForceStr":"","mixCube":"0","orderCreateTime":"2019-12-23 09:32","orderNumber":"T170949968","partName":"对方水电费水电费/场地清理/清理与挖掘","status":14,"unit":1},"par":[{"carNumber":"2","carid":1572246821877,"id":1577064889000,"isSelect":"1","level":1,"partName":"对方水电费水电费/场地清理/清理与挖掘"},{"carNumber":"1","carid":1572246808578,"id":1577067049548,"isSelect":"2","level":2,"partName":"对方水电费水电费/特殊地区路基处理/盐渍土路基处理"}],"pourSiteXy":"34.222038,108.917123"}
//     * msg : SUCCESS
//     */
//
//    private int code;
//    private DataBean data;
//    private String msg;
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public static class DataBean {
//        public Notice getNotice() {
//            return notice;
//        }
//
//        public void setNotice(Notice notice) {
//            this.notice = notice;
//        }
//
//        /**
//         * carNumber : 2
//         * carOrbit : {"mixName":"1#拌和站","mixXy":"34.231854,108.877761","pourXy":"34.222038,108.917123","xy":[{"time":"16:48","x":"34.231854","y":"108.877761"},{"time":"17:15","x":"108.879233","y":"34.232039"},{"time":"17:15","x":"108.879233","y":"34.232039"},{"time":"17:15","x":"108.879233","y":"34.232039"}]}
//         * isBingding : 1
//         * maxLevel : 2
//         * mixXy : 34.231854,108.877761
//         * orderCar : {"carNumber":"2","did":1577064770949,"dutyPerson":"周晨曦","dutyPhone":"12340000031","id":1577064889000,"isRead":1,"laborForceName":"","laborForcePhone":"","laborForceStr":"","mixCube":"0","orderCreateTime":"2019-12-23 09:32","orderNumber":"T170949968","partName":"对方水电费水电费/场地清理/清理与挖掘","status":14,"unit":1}
//         * par : [{"carNumber":"2","carid":1572246821877,"id":1577064889000,"isSelect":"1","level":1,"partName":"对方水电费水电费/场地清理/清理与挖掘"},{"carNumber":"1","carid":1572246808578,"id":1577067049548,"isSelect":"2","level":2,"partName":"对方水电费水电费/特殊地区路基处理/盐渍土路基处理"}]
//         * pourSiteXy : 34.222038,108.917123
//         */
//        private Notice notice;
//        private String carNumber;
//        private CarOrbitBean carOrbit;
//        private String isBingding;
//        private String maxLevel;
//        private String mixXy;
//        private OrderCarBean orderCar;
//        private String pourSiteXy;
//        private List<ParBean> par;
//
//        public String getCarNumber() {
//            return carNumber;
//        }
//
//        public void setCarNumber(String carNumber) {
//            this.carNumber = carNumber;
//        }
//
//        public CarOrbitBean getCarOrbit() {
//            return carOrbit;
//        }
//
//        public void setCarOrbit(CarOrbitBean carOrbit) {
//            this.carOrbit = carOrbit;
//        }
//
//        public String getIsBingding() {
//            return isBingding;
//        }
//
//        public void setIsBingding(String isBingding) {
//            this.isBingding = isBingding;
//        }
//
//        public String getMaxLevel() {
//            return maxLevel;
//        }
//
//        public void setMaxLevel(String maxLevel) {
//            this.maxLevel = maxLevel;
//        }
//
//        public String getMixXy() {
//            return mixXy;
//        }
//
//        public void setMixXy(String mixXy) {
//            this.mixXy = mixXy;
//        }
//
//        public OrderCarBean getOrderCar() {
//            return orderCar;
//        }
//
//        public void setOrderCar(OrderCarBean orderCar) {
//            this.orderCar = orderCar;
//        }
//
//        public String getPourSiteXy() {
//            return pourSiteXy;
//        }
//
//        public void setPourSiteXy(String pourSiteXy) {
//            this.pourSiteXy = pourSiteXy;
//        }
//
//        public List<ParBean> getPar() {
//            return par;
//        }
//
//        public void setPar(List<ParBean> par) {
//            this.par = par;
//        }
//
//        public static class CarOrbitBean {
//            /**
//             * mixName : 1#拌和站
//             * mixXy : 34.231854,108.877761
//             * pourXy : 34.222038,108.917123
//             * xy : [{"time":"16:48","x":"34.231854","y":"108.877761"},{"time":"17:15","x":"108.879233","y":"34.232039"},{"time":"17:15","x":"108.879233","y":"34.232039"},{"time":"17:15","x":"108.879233","y":"34.232039"}]
//             */
//
//            private String mixName;
//            private String mixXy;
//            private String pourXy;
//            private List<XyBean> xy;
//            private String runingStatu;
//
//            public String getRuningStatu() {
//                return runingStatu;
//            }
//
//            public void setRuningStatu(String runingStatu) {
//                this.runingStatu = runingStatu;
//            }
//
//            public String getMixName() {
//                return mixName;
//            }
//
//            public void setMixName(String mixName) {
//                this.mixName = mixName;
//            }
//
//            public String getMixXy() {
//                return mixXy;
//            }
//
//            public void setMixXy(String mixXy) {
//                this.mixXy = mixXy;
//            }
//
//            public String getPourXy() {
//                return pourXy;
//            }
//
//            public void setPourXy(String pourXy) {
//                this.pourXy = pourXy;
//            }
//
//            public List<XyBean> getXy() {
//                return xy;
//            }
//
//            public void setXy(List<XyBean> xy) {
//                this.xy = xy;
//            }
//
//            public static class XyBean {
//                /**
//                 * time : 16:48
//                 * x : 34.231854
//                 * y : 108.877761
//                 */
//
//                private String time;
//                private String x;
//                private String y;
//
//                public String getTime() {
//                    return time;
//                }
//
//                public void setTime(String time) {
//                    this.time = time;
//                }
//
//                public String getX() {
//                    return x;
//                }
//
//                public void setX(String x) {
//                    this.x = x;
//                }
//
//                public String getY() {
//                    return y;
//                }
//
//                public void setY(String y) {
//                    this.y = y;
//                }
//            }
//        }
//
//        public static class Notice {
//            private Info info;
//            public Info getInfo() {
//                return info;
//            }
//            public void setInfo(Info info) {
//                this.info = info;
//            }
//            public static class Info {
//                public int getCarNumber() {
//                    return carNumber;
//                }
//
//                public void setCarNumber(int carNumber) {
//                    this.carNumber = carNumber;
//                }
//
//                public int getUnit() {
//                    return unit;
//                }
//
//                public void setUnit(int unit) {
//                    this.unit = unit;
//                }
//
//                public int getCarId() {
//                    return carId;
//                }
//
//                public void setCarId(int carId) {
//                    this.carId = carId;
//                }
//
//                public int getCanCube() {
//                    return canCube;
//                }
//
//                public void setCanCube(int canCube) {
//                    this.canCube = canCube;
//                }
//
//                private int carNumber;
//                private int unit;
//                private int carId;
//                private int canCube;
//            }
//        }
//
//        public static class OrderCarBean {
//            /**
//             * carNumber : 2
//             * did : 1577064770949
//             * dutyPerson : 周晨曦
//             * dutyPhone : 12340000031
//             * id : 1577064889000
//             * isRead : 1
//             * laborForceName :
//             * laborForcePhone :
//             * laborForceStr :
//             * mixCube : 0
//             * orderCreateTime : 2019-12-23 09:32
//             * orderNumber : T170949968
//             * partName : 对方水电费水电费/场地清理/清理与挖掘
//             * status : 14
//             * unit : 1
//             */
//
//            private String carNumber;
//            private long did;
//            private String dutyPerson;
//            private String dutyPhone;
//            private long id;
//            private int isRead;
//            private String laborForceName;
//            private String laborForcePhone;
//            private String laborForceStr;
//            private String mixCube;
//            private String orderCreateTime;
//            private String orderNumber;
//            private String partName;
//            private int status;
//            private int unit;
//
//            public String getCarNumber() {
//                return carNumber;
//            }
//
//            public void setCarNumber(String carNumber) {
//                this.carNumber = carNumber;
//            }
//
//            public long getDid() {
//                return did;
//            }
//
//            public void setDid(long did) {
//                this.did = did;
//            }
//
//            public String getDutyPerson() {
//                return dutyPerson;
//            }
//
//            public void setDutyPerson(String dutyPerson) {
//                this.dutyPerson = dutyPerson;
//            }
//
//            public String getDutyPhone() {
//                return dutyPhone;
//            }
//
//            public void setDutyPhone(String dutyPhone) {
//                this.dutyPhone = dutyPhone;
//            }
//
//            public long getId() {
//                return id;
//            }
//
//            public void setId(long id) {
//                this.id = id;
//            }
//
//            public int getIsRead() {
//                return isRead;
//            }
//
//            public void setIsRead(int isRead) {
//                this.isRead = isRead;
//            }
//
//            public String getLaborForceName() {
//                return laborForceName;
//            }
//
//            public void setLaborForceName(String laborForceName) {
//                this.laborForceName = laborForceName;
//            }
//
//            public String getLaborForcePhone() {
//                return laborForcePhone;
//            }
//
//            public void setLaborForcePhone(String laborForcePhone) {
//                this.laborForcePhone = laborForcePhone;
//            }
//
//            public String getLaborForceStr() {
//                return laborForceStr;
//            }
//
//            public void setLaborForceStr(String laborForceStr) {
//                this.laborForceStr = laborForceStr;
//            }
//
//            public String getMixCube() {
//                return mixCube;
//            }
//
//            public void setMixCube(String mixCube) {
//                this.mixCube = mixCube;
//            }
//
//            public String getOrderCreateTime() {
//                return orderCreateTime;
//            }
//
//            public void setOrderCreateTime(String orderCreateTime) {
//                this.orderCreateTime = orderCreateTime;
//            }
//
//            public String getOrderNumber() {
//                return orderNumber;
//            }
//
//            public void setOrderNumber(String orderNumber) {
//                this.orderNumber = orderNumber;
//            }
//
//            public String getPartName() {
//                return partName;
//            }
//
//            public void setPartName(String partName) {
//                this.partName = partName;
//            }
//
//            public int getStatus() {
//                return status;
//            }
//
//            public void setStatus(int status) {
//                this.status = status;
//            }
//
//            public int getUnit() {
//                return unit;
//            }
//
//            public void setUnit(int unit) {
//                this.unit = unit;
//            }
//        }
//
//        public static class ParBean {
//            /**
//             * carNumber : 2
//             * carid : 1572246821877
//             * id : 1577064889000
//             * isSelect : 1
//             * level : 1
//             * partName : 对方水电费水电费/场地清理/清理与挖掘
//             */
//
//            private String carNumber;
//            private long carid;
//            private long id;
//            private String isSelect;
//            private int level;
//            private String partName;
//
//            public String getCarNumber() {
//                return carNumber;
//            }
//
//            public void setCarNumber(String carNumber) {
//                this.carNumber = carNumber;
//            }
//
//            public long getCarid() {
//                return carid;
//            }
//
//            public void setCarid(long carid) {
//                this.carid = carid;
//            }
//
//            public long getId() {
//                return id;
//            }
//
//            public void setId(long id) {
//                this.id = id;
//            }
//
//            public String getIsSelect() {
//                return isSelect;
//            }
//
//            public void setIsSelect(String isSelect) {
//                this.isSelect = isSelect;
//            }
//
//            public int getLevel() {
//                return level;
//            }
//
//            public void setLevel(int level) {
//                this.level = level;
//            }
//
//            public String getPartName() {
//                return partName;
//            }
//
//            public void setPartName(String partName) {
//                this.partName = partName;
//            }
//        }
//    }
}
