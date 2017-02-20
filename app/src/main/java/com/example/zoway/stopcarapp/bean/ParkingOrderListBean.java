package com.example.zoway.stopcarapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */
public class ParkingOrderListBean {


    /**
     * success : true
     * date : 2017-02-17 09:55:40
     * type : data
     * code : 0
     * datas : [{"parkingOrderId":85,"recordNo":"PAA1000120170217095432","parkId":0,"parkSectionId":0,"parkSeatId":-3,"vehicleNo":"粤FQK883","vehicleType":"02","isParking":"yes","parkingTime":1487296472000,"leaveTime":null,"payStatus":"no_pay"}]
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
         * parkingOrderId : 85
         * recordNo : PAA1000120170217095432
         * parkId : 0
         * parkSectionId : 0
         * parkSeatId : -3
         * vehicleNo : 粤FQK883
         * vehicleType : 02
         * isParking : yes
         * parkingTime : 1487296472000
         * leaveTime : null
         * payStatus : no_pay
         */

        private int parkingOrderId;
        private String recordNo;
        private int parkId;
        private int parkSectionId;
        private int parkSeatId;
        private String vehicleNo;
        private String vehicleType;
        private String isParking;
        private long parkingTime;
        private Object leaveTime;
        private String payStatus;

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

        public long getParkingTime() {
            return parkingTime;
        }

        public void setParkingTime(long parkingTime) {
            this.parkingTime = parkingTime;
        }

        public Object getLeaveTime() {
            return leaveTime;
        }

        public void setLeaveTime(Object leaveTime) {
            this.leaveTime = leaveTime;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        @Override
        public String toString() {
            return "DatasBean{" +
                    "parkingOrderId=" + parkingOrderId +
                    ", recordNo='" + recordNo + '\'' +
                    ", parkId=" + parkId +
                    ", parkSectionId=" + parkSectionId +
                    ", parkSeatId=" + parkSeatId +
                    ", vehicleNo='" + vehicleNo + '\'' +
                    ", vehicleType='" + vehicleType + '\'' +
                    ", isParking='" + isParking + '\'' +
                    ", parkingTime=" + parkingTime +
                    ", leaveTime=" + leaveTime +
                    ", payStatus='" + payStatus + '\'' +
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
