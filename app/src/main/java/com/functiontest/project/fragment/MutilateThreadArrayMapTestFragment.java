package com.functiontest.project.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.functiontest.project.R;

import java.util.HashMap;

public class MutilateThreadArrayMapTestFragment extends BaseFragment implements View.OnClickListener {
    private final String TAG = "MutilateThreadArrayMapTestFragment";
    private View view;
    private Button mBtnNormalStart = null;
    private ArrayMap<String,Integer> mLockArray = new ArrayMap<>();
    @Override
    public View InitilizedView() {
        if (view == null)
            view = View.inflate(mMainActivity, R.layout.fragment_mutilate_thread_arraymap_test, null);
        mBtnNormalStart = view.findViewById(R.id.mutilate_thread_arraymap_btn_normal_start);
        mBtnNormalStart.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.mutilate_thread_arraymap_btn_normal_start:
                String str=Settings.System.getString(mMainActivity.getContentResolver(),"prop.lastupdate");
                Log.d(TAG, "onClick: "+str);
                ActivityManager am = (ActivityManager) mMainActivity.getSystemService(Context.ACTIVITY_SERVICE);
                am.isUserAMonkey();
                //ErrorTestFunc();
                break;
        }
    }
    private void NormalTestFunc(){
        new Thread(()->{
            for (int i = 0; i < 100; i++) {
                mLockArray.put("key"+i,i);
            }
        }).start();
        new Thread(()->{
            for (int i = 0; i < 100; i++) {
                mLockArray.put("key"+i,i);
            }
        }).start();
    }
    private void ErrorTestFunc(){
        Bundle scanParams = new Bundle();
        new Thread(()->{
            int i = 0;
            while (true){
                i++;

                scanParams.putParcelable("ScanSettings"+i, new ScanSettings());
            }
        }).start();
        new Thread(()->{
            int i = 0;
            while (true){
                i++;
           //     Bundle scanParams = new Bundle();
                scanParams.putParcelable("ScanSetting"+i, new ScanSettings());
            }
        }).start();
    }
    public static class ScanSettings implements Parcelable {
        /**
         * Hidden network to be scanned for.
         * {@hide}
         */
        public static class HiddenNetwork {
            /** SSID of the network */
            public String ssid;

            /**
             * Default constructor for HiddenNetwork.
             */
            public HiddenNetwork(String ssid) {
                this.ssid = ssid;
            }
        }

        /** one of the WIFI_BAND values */
        public int band;
        /** list of channels; used when band is set to WIFI_BAND_UNSPECIFIED */
        /**
         * list of hidden networks to scan for. Explicit probe requests are sent out for such
         * networks during scan. Only valid for single scan requests.
         * {@hide}
         * */
        public HiddenNetwork[] hiddenNetworks;
        /** period of background scan; in millisecond, 0 => single shot scan */
        public int periodInMs;
        /** must have a valid REPORT_EVENT value */
        public int reportEvents;
        /** defines number of bssids to cache from each scan */
        public int numBssidsPerScan;
        /**
         * defines number of scans to cache; use it with REPORT_EVENT_AFTER_BUFFER_FULL
         * to wake up at fixed interval
         */
        public int maxScansToCache;
        /**
         * if maxPeriodInMs is non zero or different than period, then this bucket is
         * a truncated binary exponential backoff bucket and the scan period will grow
         * exponentially as per formula: actual_period(N) = period * (2 ^ (N/stepCount))
         * to maxPeriodInMs
         */
        public int maxPeriodInMs;
        /**
         * for truncated binary exponential back off bucket, number of scans to perform
         * for a given period
         */
        public int stepCount;
        /**
         * Flag to indicate if the scan settings are targeted for PNO scan.
         * {@hide}
         */
        public boolean isPnoScan;

        /** Implement the Parcelable interface {@hide} */
        public int describeContents() {
            return 0;
        }

        /** Implement the Parcelable interface {@hide} */
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(band);
            dest.writeInt(periodInMs);
            dest.writeInt(reportEvents);
            dest.writeInt(numBssidsPerScan);
            dest.writeInt(maxScansToCache);
            dest.writeInt(maxPeriodInMs);
            dest.writeInt(stepCount);
            dest.writeInt(isPnoScan ? 1 : 0);

            if (hiddenNetworks != null) {
                dest.writeInt(hiddenNetworks.length);
                for (int i = 0; i < hiddenNetworks.length; i++) {
                    dest.writeString(hiddenNetworks[i].ssid);
                }
            } else {
                dest.writeInt(0);
            }
        }

        /** Implement the Parcelable interface {@hide} */
        public static final Creator<ScanSettings> CREATOR =
                new Creator<ScanSettings>() {
                    public ScanSettings createFromParcel(Parcel in) {
                        ScanSettings settings = new ScanSettings();
                        settings.band = in.readInt();
                        settings.periodInMs = in.readInt();
                        settings.reportEvents = in.readInt();
                        settings.numBssidsPerScan = in.readInt();
                        settings.maxScansToCache = in.readInt();
                        settings.maxPeriodInMs = in.readInt();
                        settings.stepCount = in.readInt();
                        settings.isPnoScan = in.readInt() == 1;
                        int num_channels = in.readInt();

                        int numNetworks = in.readInt();
                        settings.hiddenNetworks = new HiddenNetwork[numNetworks];
                        for (int i = 0; i < numNetworks; i++) {
                            String ssid = in.readString();
                            settings.hiddenNetworks[i] = new HiddenNetwork(ssid);;
                        }
                        return settings;
                    }

                    public ScanSettings[] newArray(int size) {
                        return new ScanSettings[size];
                    }
                };

    }
}
