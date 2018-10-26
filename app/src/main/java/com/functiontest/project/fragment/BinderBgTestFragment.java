package com.functiontest.project.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.functiontest.project.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BinderBgTestFragment extends BaseFragment implements View.OnClickListener{
    private final String TAG = "BinderBgTestFragment";
    private View view;
    private EditText mEditBinderCallFrequency = null;
    private Button mBtnBinderCode = null;
    private EditText mEditBinderCall2Frequency = null;
    private Button mBtnBinderCode2 = null;
    private Button mBtnBinderCode3 = null;
    private Button mBtnBinderCode4 = null;
    private Button mBtnBinderCode5 = null;
    private Button mBtnBinderCode6 = null;
    private Button mBtnBinderCallSpecify = null;
    private TextView mTvWaitNotifyCount = null;
    private EditText mEditBinderNotifyNum = null;
    private Button mBtnBinderNotify = null;
    private int waitResponseNotifyCount = 0;
    private IBinder mActivity = null;
    @Override
    public View InitilizedView() {
        if (view == null) {
            view = View.inflate(mMainActivity, R.layout.fragment_binder_bg_test, null);
        }
        mEditBinderCallFrequency = view.findViewById(R.id.binder_bg_edit_call_frequency);
        mBtnBinderCode = view.findViewById(R.id.binder_bg_btn_binder_call);
        mEditBinderCall2Frequency = view.findViewById(R.id.binder_bg_edit_call2_frequency);
        mBtnBinderCode2 = view.findViewById(R.id.binder_bg_btn_binder_call2);
        mBtnBinderCode3 = view.findViewById(R.id.binder_bg_btn_binder_call3);
        mBtnBinderCode4 = view.findViewById(R.id.binder_bg_btn_binder_call4);
        mBtnBinderCode5 = view.findViewById(R.id.binder_bg_btn_binder_call5);
        mBtnBinderCode6 = view.findViewById(R.id.binder_bg_btn_binder_call6);
        mBtnBinderCallSpecify = view.findViewById(R.id.binder_bg_btn_binder_call_specify);
        mTvWaitNotifyCount = view.findViewById(R.id.binder_bg_tv_show_call_count);
        mEditBinderNotifyNum = view.findViewById(R.id.binder_bg_edit_notify_number);
        mBtnBinderNotify = view.findViewById(R.id.binder_bg_btn_notify);

        mBtnBinderCode.setOnClickListener(this);
        mBtnBinderCode2.setOnClickListener(this);
        mBtnBinderCode3.setOnClickListener(this);
        mBtnBinderCode4.setOnClickListener(this);
        mBtnBinderCode5.setOnClickListener(this);
        mBtnBinderCode6.setOnClickListener(this);
        mBtnBinderCallSpecify.setOnClickListener(this);
        mBtnBinderNotify.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        Log.d("darren", "onDestroy: -------------");
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        String edit;
        switch (view.getId()){
            case R.id.binder_bg_btn_binder_call:
                edit = mEditBinderCallFrequency.getText().toString().length()>0?mEditBinderCallFrequency.getText().toString():"0";
                waitResponseNotifyCount+=Integer.valueOf(edit);
                mTvWaitNotifyCount.setText(mTvWaitNotifyCount.getText().toString().replaceFirst(":(.*)",":"+String.valueOf(waitResponseNotifyCount)));
                for (int i=0;i<Integer.valueOf(edit);i++){
                    new Thread(()->{
                        CallActivityTestCode(9010,false);
                    }).start();
                }

                break;
            case R.id.binder_bg_btn_binder_call2:
                edit = mEditBinderCall2Frequency.getText().toString().length()>0?mEditBinderCall2Frequency.getText().toString():"0";
                waitResponseNotifyCount+=Integer.valueOf(edit);
                mTvWaitNotifyCount.setText(mTvWaitNotifyCount.getText().toString().replaceFirst(":(.*)",":"+String.valueOf(waitResponseNotifyCount)));
                for (int i=0;i<Integer.valueOf(edit);i++){
                    new Thread(()->{
                        CallActivityTestCode(9012,false);
                    }).start();
                }
                break;
            case R.id.binder_bg_btn_binder_call3:
                for (int i=0;i<10;i++){
                    new Thread(()->{
                        Log.d(TAG, "onClick: ------------");
                        CallActivityTestCode(9021,false);
                    }).start();
                }
                break;
            case R.id.binder_bg_btn_binder_call4:
                for (int i=0;i<10;i++){
                    new Thread(()->{
                        CallActivityTestCode(9022,false);
                    }).start();
                }
                break;
            case R.id.binder_bg_btn_binder_call5:
                for (int i=0;i<10;i++){
                    new Thread(()->{
                        CallActivityTestCode(9023,false);
                    }).start();
                }
                break;
            case R.id.binder_bg_btn_binder_call6:
                for (int i=0;i<10;i++){
                    new Thread(()->{
                        CallActivityTestCode(9024,false);
                    }).start();
                }
                break;
            case R.id.binder_bg_btn_binder_call_specify:
                new Thread(()->{
                    CallActivityTestCode(9015,false);
                }).start();
                break;
            case R.id.binder_bg_btn_notify:
                edit = mEditBinderNotifyNum.getText().toString().length()>0?mEditBinderNotifyNum.getText().toString():"0";
                waitResponseNotifyCount-=Integer.valueOf(edit);
                mTvWaitNotifyCount.setText(mTvWaitNotifyCount.getText().toString().replaceFirst(":(.*)",":"+String.valueOf(waitResponseNotifyCount)));
                for (int i = 0;i<Integer.valueOf(edit);i++){
                    new Thread(()->{
                        CallActivityTestCode(9011,false,false);
                    }).start();
                }
                break;
        }

    }
    private void CallActivityTestCode(int code,boolean isReply){
        CallActivityTestCode(code,isReply,true);
    }

    @SuppressLint("ServiceCast")
    private void CallActivityTestCode(int code,boolean isReply,boolean isQueue){
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        if(mActivity == null){
            try {
                Method method = null;
                Class clazz = Class.forName(
                        "android.os.ServiceManager");
                method = clazz.getMethod("getService", String.class);
                mActivity = (IBinder) method.invoke(null, Context.ACTIVITY_SERVICE);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            mActivity.transact(code,data,reply,isQueue?2:0);
            reply.readException();
            if(isReply)
                Log.d(TAG, "CallActivityTestCode: "+reply.readInt());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
/**
 diff --git a/core/java/com/android/internal/app/IBatteryStats.aidl b/core/java/com/android/internal/app/IBatteryStats.aidl
 index 4275e0b..e26d0c2 100644
 --- a/core/java/com/android/internal/app/IBatteryStats.aidl
 +++ b/core/java/com/android/internal/app/IBatteryStats.aidl
 @@ -122,7 +122,7 @@ interface IBatteryStats {
 void noteWifiMulticastDisabledFromSource(in WorkSource ws);
 void noteWifiRadioPowerState(int powerState, long timestampNs, int uid);
 void noteNetworkInterfaceType(String iface, int type);
 -    void noteNetworkStatsEnabled();
 +    potentialthreat void noteNetworkStatsEnabled();
 void noteDeviceIdleMode(int mode, String activeReason, int activeUid);
 void setBatteryState(int status, int health, int plugType, int level, int temp, int volt,
 int chargeUAh, int chargeFullUAh);
 diff --git a/services/core/java/com/android/server/Watchdog.java b/services/core/java/com/android/server/Watchdog.java
 index 583430a..c411f73 100644
 --- a/services/core/java/com/android/server/Watchdog.java
 +++ b/services/core/java/com/android/server/Watchdog.java
 @@ -504,7 +504,7 @@ public class Watchdog extends Thread {
 blockedCheckers = Collections.emptyList();
 subject = "Open FD high water mark reached";
 }
 -                allowRestart = mAllowRestart;
 +                allowRestart = false;
 }

 // If we got here, that means that the system is most likely hung.
 diff --git a/services/core/java/com/android/server/am/ActivityManagerService.java b/services/core/java/com/android/server/am/ActivityManagerService.java
 index a424243..90a5ab7 100644
 --- a/services/core/java/com/android/server/am/ActivityManagerService.java
 +++ b/services/core/java/com/android/server/am/ActivityManagerService.java
 @@ -533,6 +533,8 @@ import smartisanos.util.SettingsUtil;
 import smartisanos.os.PeroptWhiteListParser;
 import android.util.RingBuffer;

 +import android.view.SurfaceControl;
 +
 public class ActivityManagerService extends IActivityManager.Stub
 implements Watchdog.Monitor, BatteryStatsImpl.BatteryCallback {

 @@ -3629,6 +3631,109 @@ public class ActivityManagerService extends IActivityManager.Stub
 removeTasksByPackageNameSmartLocked(packageName);
 }
 }
 +        if (code == 9010) {
 +            synchronized(obj) {
 +                try {
 +                    obj.wait();
 +                }catch(InterruptedException ex){
 +                    Slog.e(TAG_AM,"owner object wait interrupt",ex);
 +                }
 +                Slog.e(TAG_AM,"qyl thread getId="+Thread.currentThread().getId());
 +                Slog.e(TAG_AM,"qyl thread pid="+Process.myTid());
 +            }
 +            if(Process.myTid()==sBgBinderThreadPid){
 +                synchronized(willfullyBlock) {
 +                    try {
 +                        willfullyBlock.wait();
 +                    }catch(InterruptedException ex){
 +                        Slog.e(TAG_AM,"qyl willfullyBlock ",ex);
 +                    }
 +                }
 +            }
 +
 +        }
 +        if (code == 9012) {
 +            synchronized(obj) {
 +                try{
 +                obj.wait();
 +                }catch(InterruptedException ex){
 +                    Slog.e(TAG_AM,"owner object wait interrupt",ex);
 +                }
 +            }
 +        }
 +        if (code == 9013) {
 +            synchronized(obj) {
 +                try{
 +                obj.wait();
 +                }catch(InterruptedException ex){
 +                    Slog.e(TAG_AM,"owner object wait interrupt",ex);
 +                }
 +            }
 +        }
 +        if (code == 9015) {
 +            IBinder mPerfHandler = ServiceManager.getService("performance.adj");
 +            if(mPerfHandler==null){
 +                reply.writeException(new NullPointerException("system Service death"));
 +                return true;
 +            }
 +            mPerfHandler.transact(70, data, reply, 0);
 +        }
 +        if (code == 9014) {
 +            synchronized(obj) {
 +                obj.notifyAll();
 +            }
 +        }
 +        if (code == 9011) {
 +            synchronized(obj) {
 +                obj.notify();
 +            }
 +        }
 +        if (code == 9016) {
 +            Binder.setBinderCtlMask(1);
 +        }
 +        if (code == 9017) {
 +            synchronized(willfullyBlock) {
 +                willfullyBlock.notify();
 +            }
 +        }
 +        if (code == 9018) {
 +            Slog.e(TAG_AM,"qyl ----  " + data.readInt());
 +            Slog.e(TAG_AM,"qyl ------ "+data.readString());
 +            sBgBinderThreadPid = data.readInt();
 +            Slog.e(TAG_AM,"qyl ---------- willfullyBlock bg thread pid="+sBgBinderThreadPid);
 +        }
 +        if (code == 9019) {
 +            new Thread(()->{
 +                Slog.e(TAG_AM,"qyl test thread start-------");
 +                while(true) {
 +                    try {
 +                    SurfaceControl.openTransaction();
 +                    Thread.sleep(1);
 +                    SurfaceControl.openTransaction();
 +                    Thread.sleep(1);
 +                    SurfaceControl.setAnimationTransaction();
 +                    SurfaceControl.closeTransactionSync();
 +                    Thread.sleep(1);
 +                    SurfaceControl.closeTransactionSync();
 +                    }catch (Exception e){
 +                        Slog.e("qyl","open close exception",e);
 +                    }
 +
 +                    try{
 +                                               Thread.sleep(200);
 +                                       }catch (Exception e){
 +                                               Slog.e("qyl","thread interpret excetion",e);
 +                                       }
 +                                       if(endTransactionSync == 10)
 +                        break;
 +                }
 +            }).start();
 +        }
 +        if (code == 9020) {
 +            Slog.e(TAG_AM,"qyl ----  " + data.readInt());
 +            Slog.e(TAG_AM,"qyl ------ "+data.readString());
 +            endTransactionSync = data.readInt();
 +        }
 try {
 return super.onTransact(code, data, reply, flags);
 } catch (RuntimeException e) {
 @@ -3643,7 +3748,10 @@ public class ActivityManagerService extends IActivityManager.Stub
 throw e;
 }
 }
 -
 +    private final Object obj = new Object();
 +    private final Object willfullyBlock = new Object();
 +    private int sBgBinderThreadPid = 0;
 +    private int endTransactionSync = 0;
 void updateCpuStats() {
 final long now = SystemClock.uptimeMillis();
 if (mLastCpuTime.get() >= now - MONITOR_CPU_MIN_TIME) {
 diff --git a/smarti-services/PerformanceService.cpp b/smarti-services/PerformanceService.cpp
 index 6f65740..f574f0f 100644
 --- a/smarti-services/PerformanceService.cpp
 +++ b/smarti-services/PerformanceService.cpp
 @@ -43,8 +43,14 @@
 #include <dlfcn.h>
 #include <string>

 +#include <condition_variable>
 +#include <mutex>
 +#include <thread>
 typedef std::pair<std::string, int> PAIR;

 +std::condition_variable cv;
 +std::mutex mtx;
 +
 struct CmpByValue {
 bool operator()(const PAIR& lhs, const PAIR& rhs) {
 return lhs.second > rhs.second;
 @@ -354,9 +360,15 @@ void KGSL_contentError() {
 }
 }
 }
 +void foo(){
 +  sp<IServiceManager> sm  = defaultServiceManager();
 +  sp<IBinder> binder = sm->getService(String16("activity"));
 +  Parcel data,reply;
 +  binder->transact(9014,data,&reply,0);
 +}
 status_t PerformanceService::onTransact(uint32_t code, const Parcel& data,
 Parcel* reply, uint32_t flags) {
 -    Mutex::Autolock _l(mLock);
 +   // Mutex::Autolock _l(mLock);

 IPCThreadState* ipc = IPCThreadState::self();
 const int uid = ipc->getCallingUid();
 @@ -899,6 +911,23 @@ status_t PerformanceService::onTransact(uint32_t code, const Parcel& data,
 reply->writeNoException();
 }
 break;
 +        case 70:{
 +            ALOGE("qyl wait current thread,until other thread send notify.....");
 +            std::unique_lock<std::mutex> lck(mtx);
 +            cv.wait(lck);
 +            ALOGE("qyl wait current thread is been roused");
 +            std::thread notifyThread(foo);
 +            ALOGE("qyl notifyThread done");
 +            notifyThread.join();
 +                }
 +            break;
 +        case 71:{
 +            ALOGE("qyl execution wake up operation");
 +            std::unique_lock<std::mutex> lck(mtx);
 +            cv.notify_one();
 +            ALOGE("qyl wake up done");
 +                }
 +            break;
 default:
 {
 return BBinder::onTransact(code, data, reply, flags);



 *
 *
 */