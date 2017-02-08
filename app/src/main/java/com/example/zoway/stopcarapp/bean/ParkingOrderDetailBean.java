package com.example.zoway.stopcarapp.bean;

/**
 * Created by Administrator on 2017/1/18.
 */
public class ParkingOrderDetailBean {


    /**
     * success : true
     * date : 2017-01-18 15:12:57
     * type : data
     * code : 0
     * data : {"parkingOrderId":0,"recordNo":"PAA1000020170117162800","parkId":0,"parkSectionId":0,"parkSeatId":0,"vehicleNo":"粤X12345","vehicleType":"02","isParking":"yes","isSpecial":"no","parkingTime":1484641970000,"payStatus":"no_pay","dueFare":null,"mobileUrl":"http://www.baidu.com"}
     */

    private boolean success;
    private String date;
    private String type;
    private String code;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * parkingOrderId : 0
         * recordNo : PAA1000020170117162800
         * parkId : 0
         * parkSectionId : 0
         * parkSeatId : 0
         * vehicleNo : 粤X12345
         * vehicleType : 02
         * isParking : yes
         * isSpecial : no
         * parkingTime : 1484641970000
         * payStatus : no_pay
         * dueFare : null
         * mobileUrl : http://www.baidu.com
         */

        private int parkingOrderId;
        private String recordNo;
        private int parkId;
        private int parkSectionId;
        private int parkSeatId;
        private String vehicleNo;
        private String vehicleType;
        private String isParking;
        private String isSpecial;
        private long parkingTime;
        private String payStatus;
        private Object dueFare;
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

        public int getParkId() {
            return parkId;
        }

        public void setParkId(int parkId) {
            this.parkId = parkId;
        }

        public int getParkSectionId() {
            return parkSectionId;
        }

        public void setParkSectionId(int parkSectionId) {
            this.parkSectionId = parkSectionId;
        }

        public int getParkSeatId() {
            return parkSeatId;
        }

        public void setParkSeatId(int parkSeatId) {
            this.parkSeatId = parkSeatId;
        }

        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getIsParking() {
            return isParking;
        }

        public void setIsParking(String isParking) {
            this.isParking = isParking;
        }

        public String getIsSpecial() {
            return isSpecial;
        }

        public void setIsSpecial(String isSpecial) {
            this.isSpecial = isSpecial;
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

        public Object getDueFare() {
            return dueFare;
        }

        public void setDueFare(Object dueFare) {
            this.dueFare = dueFare;
        }

        public String getMobileUrl() {
            return mobileUrl;
        }

        public void setMobileUrl(String mobileUrl) {
            this.mobileUrl = mobileUrl;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "parkingOrderId=" + parkingOrderId +
                    ", recordNo='" + recordNo + '\'' +
                    ", parkId=" + parkId +
                    ", parkSectionId=" + parkSectionId +
                    ", parkSeatId=" + parkSeatId +
                    ", vehicleNo='" + vehicleNo + '\'' +
                    ", vehicleType='" + vehicleType + '\'' +
                    ", isParking='" + isParking + '\'' +
                    ", isSpecial='" + isSpecial + '\'' +
                    ", parkingTime=" + parkingTime +
                    ", payStatus='" + payStatus + '\'' +
                    ", dueFare=" + dueFare +
                    ", mobileUrl='" + mobileUrl + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ParkingOrderDetailBean{" +
                "success=" + success +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}
