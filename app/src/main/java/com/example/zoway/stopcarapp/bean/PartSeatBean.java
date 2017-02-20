package com.example.zoway.stopcarapp.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 * 车位信息
 */
public class PartSeatBean {


    /**
     * success : true
     * date : 2017-01-16 16:56:45
     * type : data
     * code : 0
     * datas : [{"parkSeatId":0,"seatNo":"10000","sectionId":0,"detectorId":0,"status":"normal","isParking":"no"}]
     */

    private boolean success;
    private String date;
    private String type;
    private String code;
    private ArrayList<DatasBean> datas;

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

    public ArrayList<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean{
        /**
         * parkSeatId : 0
         * seatNo : 10000
         * sectionId : 0
         * detectorId : 0
         * status : normal
         * isParking : no
         */

        private int parkSeatId;
        private String seatNo;
        private int sectionId;
        private int detectorId;
        private String status;
        private String isParking;

        public int getParkSeatId() {
            return parkSeatId;
        }

        public void setParkSeatId(int parkSeatId) {
            this.parkSeatId = parkSeatId;
        }
         public String getSeatNo() {
            return seatNo;
        }

        public void setSeatNo(String seatNo) {
            this.seatNo = seatNo;


        }

        public int getSectionId() {
            return sectionId;
        }

        public void setSectionId(int sectionId) {
            this.sectionId = sectionId;
        }

        public int getDetectorId() {
            return detectorId;
        }

        public void setDetectorId(int detectorId) {
            this.detectorId = detectorId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


        public String getIsParking() {
            return isParking;
        }

        public void setIsParking(String isParking) {
            this.isParking = isParking;

        }
    }
}
