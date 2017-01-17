package com.example.zoway.stopcarapp.bean;

/**
 * Created by Administrator on 2017/1/16.
 */
public class PartBaseInfoBean {


    /**
     * success : true
     * date : 2017-01-16 16:08:35
     * type : data
     * code : 0
     * data : {"parkId":0,"name":"顺德区北滘镇路边停车场","town":"顺德区北滘镇","parkType":"curb","parkNo":"AA","totalSeatCount":1,"parkingSeatCount":0,"longitude":113.21549,"latitude":22.929501,"address":"顺德区北滘镇","contactor":"张三","tel":"13800138000","descr":"描述"}
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
         * parkId : 0
         * name : 顺德区北滘镇路边停车场
         * town : 顺德区北滘镇
         * parkType : curb
         * parkNo : AA
         * totalSeatCount : 1
         * parkingSeatCount : 0
         * longitude : 113.21549
         * latitude : 22.929501
         * address : 顺德区北滘镇
         * contactor : 张三
         * tel : 13800138000
         * descr : 描述
         */

        private int parkId;
        private String name;
        private String town;
        private String parkType;
        private String parkNo;
        private int totalSeatCount;
        private int parkingSeatCount;
        private double longitude;
        private double latitude;
        private String address;
        private String contactor;
        private String tel;
        private String descr;

        public int getParkId() {
            return parkId;
        }

        public void setParkId(int parkId) {
            this.parkId = parkId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getParkType() {
            return parkType;
        }

        public void setParkType(String parkType) {
            this.parkType = parkType;
        }

        public String getParkNo() {
            return parkNo;
        }

        public void setParkNo(String parkNo) {
            this.parkNo = parkNo;
        }

        public int getTotalSeatCount() {
            return totalSeatCount;
        }

        public void setTotalSeatCount(int totalSeatCount) {
            this.totalSeatCount = totalSeatCount;
        }

        public int getParkingSeatCount() {
            return parkingSeatCount;
        }

        public void setParkingSeatCount(int parkingSeatCount) {
            this.parkingSeatCount = parkingSeatCount;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContactor() {
            return contactor;
        }

        public void setContactor(String contactor) {
            this.contactor = contactor;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getDescr() {
            return descr;
        }

        public void setDescr(String descr) {
            this.descr = descr;
        }
    }

    @Override
    public String toString() {
        return "PartBaseInfoBean{" +
                "success=" + success +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}
