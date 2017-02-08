package com.example.zoway.stopcarapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */
public class ParkingOrderListBean {

    /**
     * success : true
     * date : 2017-01-18 10:25:49
     * type : data
     * code : 0
     * datas : [{"parkingOrderId":-1,"recordNo":"PAA1000120170117162800","parkSeatId":-3,"vehicleNo":null,"vehicleType":null,"isParking":"yes","parkingTime":1484641970000,"payStatus":"no_pay","mobileUrl":"http://www.baidu.com"}]
     */

    private boolean success;
    private String date;
    private String type;
    private String code;
    private List<DatasBean> datas;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * parkingOrderId : -1
         * recordNo : PAA1000120170117162800
         * parkSeatId : -3
         * vehicleNo : null
         * vehicleType : null
         * isParking : yes
         * parkingTime : 1484641970000
         * payStatus : no_pay
         * mobileUrl : http://www.baidu.com
         */

        private int parkingOrderId;
        private String recordNo;
        private int parkSeatId;
        private Object vehicleNo;
        private Object vehicleType;
        private String isParking;
        private long parkingTime;
        private String payStatus;
        private String mobileUrl;

        public int getParkingOrderId() {
            return parkingOrderId;
        }

        public void setParkingOrderId(int parkingOrderId) {
            this.parkingOrderId = parkingOrderId;
        }

        public String getRecordNo() {
            return recordNo;
        }

        public void setRecordNo(String recordNo) {
            this.recordNo = recordNo;
        }

        public int getParkSeatId() {
            return parkSeatId;
        }

        public void setParkSeatId(int parkSeatId) {
            this.parkSeatId = parkSeatId;
        }

        public Object getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(Object vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

        public Object getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(Object vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getIsParking() {
            return isParking;
        }

        public void setIsParking(String isParking) {
            this.isParking = isParking;
        }

        public long getParkingTime() {
            return parkingTime;
        }

        public void setParkingTime(long parkingTime) {
            this.parkingTime = parkingTime;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        public String getMobileUrl() {
            return mobileUrl;
        }

        public void setMobileUrl(String mobileUrl) {
            this.mobileUrl = mobileUrl;
        }

        @Override
        public String toString() {
            return "DatasBean{" +
                    "parkingOrderId=" + parkingOrderId +
                    ", recordNo='" + recordNo + '\'' +
                    ", parkSeatId=" + parkSeatId +
                    ", vehicleNo=" + vehicleNo +
                    ", vehicleType=" + vehicleType +
                    ", isParking='" + isParking + '\'' +
                    ", parkingTime=" + parkingTime +
                    ", payStatus='" + payStatus + '\'' +
                    ", mobileUrl='" + mobileUrl + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ParkingOrderListBean{" +
                "success=" + success +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", datas=" + datas +
                '}';
    }
}
