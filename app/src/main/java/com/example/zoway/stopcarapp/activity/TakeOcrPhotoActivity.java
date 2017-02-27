package com.example.zoway.stopcarapp.activity;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.bean.PayUIBean;
import com.example.zoway.stopcarapp.bean.UIsBean;
import com.example.zoway.stopcarapp.databinding.ActivityTakeOcrPhotoBinding;
import com.example.zoway.stopcarapp.file.SystemService;
import com.example.zoway.stopcarapp.ocr.BitmapHandle;
import com.example.zoway.stopcarapp.view.CameraPreview;
import com.example.zoway.stopcarapp.view.DrawRectView;
import com.wintone.plateid.PlateCfgParameter;
import com.wintone.plateid.PlateRecognitionParameter;
import com.wintone.plateid.RecogService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TakeOcrPhotoActivity extends BaseActivity {
    private static final String TAG = "TakeOcrPhotoActivity";

    ActivityTakeOcrPhotoBinding binding;
    private Camera mCamera = null;
    private CameraPreview mPreview;
    private FrameLayout preview;
    PitcCallback mPicture;
    private ProgressDialog pDia;
    private BaseActivity activity;

    public RecogService.MyBinder recogBinder;
    public int iInitPlateIDSDK;
    public int nRet;
    private boolean usepara;
    private String lpFileName;
    private String pic;
    private String devcode;
    private String datefile;
    private int imageformat = 1;
    int bVertFlip = 0;
    int bDwordAligned = 1;
    boolean bGetVersion = false;
    private int ReturnAuthority = -1;
    private int nPlateLocate_Th;// 识别阈值(取值范围0-9,5:默认阈值0:最宽松的阈值9:最严格的阈值)
    private int nOCR_Th;
    private int bIsAutoSlope;// 是否要倾斜校正
    private int nSlopeDetectRange;// 倾斜校正的范围(取值范围0-16)
    private int nContrast;// 清晰度指数(取值范围0-9,最模糊时设为1;最清晰时设为9)
    private int bIsNight;// 是否夜间模式：1是；0不是
    private String szProvince;// 省份顺序
    private int individual;// 是否开启个性化车牌:0是；1不是
    private int tworowyellow;// 双层黄色车牌是否开启:2是；3不是
    private int armpolice;// 单层武警车牌是否开启:4是；5不是
    private int tworowarmy;// 双层军队车牌是否开启:6是；7不是
    private int tractor; // 农用车车牌是否开启:8是；9不是
    private int onlytworowyellow;// 只识别双层黄牌是否开启:10是；11不是
    private int embassy;// 使馆车牌是否开启:12是；13不是
    private int onlylocation;// 只定位车牌是否开启:14是；15不是
    private int armpolice2;// 双层武警车牌是否开启:16是；17不是
    String[] fieldvalue = new String[14];
    private String compressFilePath = null;//压缩后图片路径

    private int parkingOrderId;
    private String seatNo;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                     pDia = ProgressDialog.show(TakeOcrPhotoActivity.this, "识别车牌图片","正在识别中", true, false);
                    break;
                case 2:
                    //保存结果并回调函数
                    pDia.dismiss();
                    Log.d(TAG, msg.obj.toString());
                    //回调
                    Log.i("TakeOcrPhotoActivity","TakeOcrPhotoActivity:"+msg.obj.toString());
                    Log.i("TakeOcrPhotoActivity","TakeOcrPhotoActivity:"+compressFilePath);
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String result = jsonObject.getString("result");
                        if ("?".equals(result)){
                            File file = new File(compressFilePath);
                            if (file.exists()){
                                file.delete();
                                Toast.makeText(getApplicationContext(),"无法识别照片，请将镜头内红框对准车牌",Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            Intent intent = new Intent(activity,PayActivity.class);
                            intent.putExtra("PayUI",new PayUIBean(true,result,parkingOrderId,seatNo));
                            activity.startActivity(intent);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //关闭当前activity
                    BaseActivity baseActivity = activitys.get(activitys.size() - 1);
                    baseActivity.finish();
                    activitys.remove(activitys.size()-1);

                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_take_ocr_photo;
    }

    @Override
    protected void initData(Intent intent) {
        binding = (ActivityTakeOcrPhotoBinding) view;
        activity = this;
        activitys.add(activity);
        binding.setLightBtnTxt("开灯");
        mPicture = new PitcCallback();
        //订单号
        Bundle extras = intent.getExtras();
        seatNo = extras.getString("seatNo");
        parkingOrderId = extras.getInt("parkingOrderId");


    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mCamera = Camera.open(0); // attempt to get a Camera instance
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            Toast.makeText(this, "照相机异常", Toast.LENGTH_LONG).show();
            return;
        }

        Camera.Parameters parameters = mCamera.getParameters();
//        parameters.setPictureSize(10, 10);
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setJpegQuality(80);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(parameters);

        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        LinearLayout tkephotoly2 = (LinearLayout) findViewById(R.id.tkephotoly2);

        preview.addView(mPreview);
        DrawRectView drv = new DrawRectView(this);
        preview.addView(drv);
        preview.bringChildToFront(tkephotoly2);

        //开灯
        mPreview.openFlahsLight();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPreviewAndFlashLight();
        mPreview.closeFlashLigth();
    }

    private void stopPreviewAndFlashLight(){
        //关灯

        // TODO Auto-generated method stub
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void takePhote(View view){
        mCamera.takePicture(null, null, mPicture);
    }
    public void zoomIn(View view){
        mPreview.setZoomView(8);
    }
    public void zoomOut(View view){
        mPreview.setZoomView(8);
    }
    public void lightCtrl(View view){
        if (binding.getLightBtnTxt().equals("开灯")) {
            mPreview.openFlahsLight();
            binding.setLightBtnTxt("关灯");
        } else {
            mPreview.closeFlashLigth();
            binding.setLightBtnTxt("开灯");
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_1 || keyCode == KeyEvent.KEYCODE_ENTER){
            takePhote(null);
            //Toast.makeText(this, keyCode + "", Toast.LENGTH_LONG).show();
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_BACK){
            //返回
            finish();
        }
        return super.onKeyUp(keyCode, event);
    }

    public ServiceConnection recogConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            recogConn = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            recogBinder = (RecogService.MyBinder) service;
            int inRet = recogBinder.getInitPlateIDSDK();
            // nRet = recogBinder.getInitPlateIDSDK();
            inRet = 0;
            if (inRet != 0) {
                 Toast.makeText(getApplicationContext(), "验证授权或初始化失败:" + nRet,
                 Toast.LENGTH_SHORT).show();

            } else {
                if (usepara == false) {
                    recogBinder.setRecogArgu(lpFileName, imageformat, bGetVersion, bVertFlip, bDwordAligned);
                } else {
                    System.out.println("usepara");
                    // 设置识别用到的参数，采用直接设置参数对象的方式设置识别参数，所有参数定义请查看文档
                    PlateCfgParameter cfgparameter = new PlateCfgParameter();
                    cfgparameter.armpolice = armpolice;
                    cfgparameter.armpolice2 = armpolice2;
                    cfgparameter.bIsAutoSlope = bIsAutoSlope;
                    cfgparameter.bIsNight = bIsNight;
                    cfgparameter.embassy = embassy;
                    cfgparameter.individual = individual;
                    cfgparameter.nContrast = nContrast;
                    cfgparameter.nOCR_Th = nOCR_Th;
                    cfgparameter.nPlateLocate_Th = nPlateLocate_Th;
                    cfgparameter.nSlopeDetectRange = nSlopeDetectRange;
                    cfgparameter.onlylocation = onlylocation;
                    cfgparameter.tworowyellow = tworowyellow;
                    cfgparameter.tworowarmy = tworowarmy;
                    if (szProvince == null)
                        szProvince = "";
                    cfgparameter.szProvince = szProvince;
                    cfgparameter.onlytworowyellow = onlytworowyellow;
                    cfgparameter.tractor = tractor;
                    recogBinder.setRecogArgu(cfgparameter, imageformat,
                            bVertFlip, bDwordAligned);
                }
                nRet = recogBinder.getnRet();
                // fieldvalue = recogBinder.doRecog(pic, width, height);
                PlateRecognitionParameter prp = new PlateRecognitionParameter();
                prp.dataFile = datefile;
                prp.devCode = devcode;
                prp.pic = lpFileName;
                fieldvalue = recogBinder.doRecogDetail(prp);
                nRet = recogBinder.getnRet();
            }
            // 解绑识别服务。
            if (recogBinder != null) {
                unbindService(recogConn);
            }
            // 用户不指定lpFileName时删除所拍图片
            if (null != pic && !pic.equals("")) {
                // System.out.println("null != lpFileName && !lpFileName.equals");
            } else {
                // System.out.println("lpFileName="+lpFileName);
                File picFile = new File(lpFileName);
                if (picFile.exists()) {
                    picFile.delete();
                }
            }
            // 返回识别结果
            // Log.i(TAG, "pic="+pic);
            // Intent intentReturn = new Intent();
            // intentReturn.putExtra("ReturnAuthority", ReturnAuthority);
            // intentReturn.putExtra("nRet", nRet);
            // intentReturn.putExtra("ReturnLPFileName", lpFileName);
            // intentReturn.putExtra("fieldvalue", fieldvalue);
            // setResult(Activity.RESULT_OK, intentReturn);
            // finish();
            try {
                Message msg1 = new Message();
                msg1.what = 2;
                JSONObject json = new JSONObject();
                json.put("filePath", compressFilePath.substring(compressFilePath.indexOf("Zparking") + 9));
                if (fieldvalue != null && fieldvalue[0] != null) {
                    int len = fieldvalue[0].length() > 7 ? 7 : fieldvalue[0].length();
                    json.put("result", fieldvalue[0]);
                    json.put("color", fieldvalue[1]);
                } else {
                    json.put("result", "?");
                }
                msg1.obj = json;
                handler.sendMessage(msg1);
            }catch(Exception ex){
                Log.e(TAG, ex.getMessage(), ex);
            }
        }
    };

    private class PitcCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
//            byte[] jdata = null;
//            if (camera.getParameters().getPreviewFormat() == ImageFormat.NV21) {// NV21
//                // Convert to JPG
//                Camera.Size previewSize = camera.getParameters().getPreviewSize();
//                YuvImage yuvimage = new YuvImage(_data, ImageFormat.NV21, previewSize.width, previewSize.height, null);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 50, baos);
//                jdata = baos.toByteArray();
//                //Use the jdata as normal.
//
//                try {
//                    String s = SystemService.tempFile + "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()) + ".jpg";
//                    File f1 = new File(s + ".jpg");
//                    f1.createNewFile();
//                    BufferedOutputStream bos = new BufferedOutputStream(
//                            new FileOutputStream(f1));
//                    yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 50, bos);
//                    bos.close();
//                }catch(Exception ex){
//
//                }
//            }
//            final byte[] data = jdata;
            int ii = data.length;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inInputShareable = true;
            opts.inPurgeable = true;

            final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
//            final Bitmap bitmap1 = BitmapFactory.decodeByteArray(_data, 0, data.length, opts);

            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String in = sdf.format(Calendar.getInstance().getTime());
            final String fname = SystemService.tempFile + "/" + in + ".jpg";
            // String fname = imgBasePath + "20150824104730.jpg";
            Log.i(TAG, fname + "	" + ii);


            Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(Subscriber<? super String> subscriber) {
                            //保存
                            if (TakeOcrPhotoActivity.this.mCamera != null) {
                                Camera.Size ss = TakeOcrPhotoActivity.this.mCamera.getParameters().getPictureSize();
                                BitmapHandle.writeOcrJpgFromBytes(fname, ss.height, ss.width, data,100, bitmap);
                            }
                            //压缩图片
                            Bitmap srcBitmap = BitmapFactory.decodeFile(fname);//此时返回bm为空

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
                            int options = 100;
                            while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                                baos.reset();//重置baos即清空baos
                                srcBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                                options -= 15;//每次都减少10
                            }

                            compressFilePath = SystemService.photoFile + fname.substring(fname.lastIndexOf("/"));
                            try {
                                File compressFile = new File(compressFilePath);
                                if(!compressFile.exists()) compressFile.createNewFile();
                                FileOutputStream fos = new FileOutputStream(compressFile);
                                fos.write(baos.toByteArray());
                                srcBitmap.recycle();
                                srcBitmap = null;
                                baos.close();
                                fos.close();
                            }catch(Exception ex){

                            }

                            subscriber.onNext(fname);
                        }
                    })
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            Intent recogIntent = new Intent(TakeOcrPhotoActivity.this, RecogService.class);
                            recogIntent.putExtra("imgFileName", s);
                            lpFileName = s;
                            bindService(recogIntent, recogConn, Service.BIND_AUTO_CREATE);
                        }
                    });
        }
    }
}
