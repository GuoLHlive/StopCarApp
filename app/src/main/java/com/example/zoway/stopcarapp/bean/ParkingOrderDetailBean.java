package com.example.zoway.stopcarapp.bean;

/**
 * Created by Administrator on 2017/1/18.
 */
public class ParkingOrderDetailBean {


    /**
     * success : true
     * date : 2017-02-16 16:51:05
     * type : data
     * code : 0
     * data : {"parkingOrderId":66,"recordNo":"PAA1000120170216162913","parkId":0,"parkSectionId":0,"parkSeatId":-3,"vehicleNo":"粤X12345","vehicleType":"02","isParking":"no","isSpecial":"no","parkingTime":1487233753000,"leaveTime":1487234338000,"payStatus":"no_pay","dueFare":0,"realFare":0,"mobileUrl":"http://www.baidu.com"}
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
         * parkingOrderId : 66
         * recordNo : PAA1000120170216162913
         * parkId : 0
         * parkSectionId : 0
         * parkSeatId : -3
         * vehicleNo : 粤X12345
         * vehicleType : 02
         * isParking : no
         * isSpecial : no
         * parkingTime : 1487233753000
         * leaveTime : 1487234338000
         * payStatus : no_pay
         * dueFare : 0
         * realFare : 0
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
        private long leaveTime;
        private String payStatus;
        private int dueFare;
        private int realFare;
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

        public long getLeaveTime() {
            return leaveTime;
        }

        public void setLeaveTime(long leaveTime) {
            this.leaveTime = leaveTime;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        public int getDueFare() {
            return dueFare;
        }

        public void setDueFare(int dueFare) {
            this.dueFare = dueFare;
        }

        public int getRealFare() {
            return realFare;
        }

        public void setRealFare(int realFare) {
            this.realFare = realFare;
        }

        public String getMobileUrl() {
            return mobileUrl;
        }

        public void setMobileUrl(String mobileUrl) {
            this.mobileUrl = mobileUrl;
        }
    }
}
