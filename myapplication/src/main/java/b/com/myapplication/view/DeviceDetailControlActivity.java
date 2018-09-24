package b.com.myapplication.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Date;

import b.com.myapplication.MyApplication;
import b.com.myapplication.R;
import b.com.myapplication.manager.GreenDaoManager;
import b.com.myapplication.mode.DeviceInfo;
import b.com.myapplication.mode.DeviceInfoDao;
import b.com.myapplication.utils.SharedPreferencesHelper;

public class DeviceDetailControlActivity extends BaseActivity {
    final static private String TAG = "DeviceDetailControlActivity";
    private DeviceInfoDao mDeviceInfoDao;
    private SharedPreferencesHelper mSharePreferencesHelper;
    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.device_main_layout_toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.device_main_layout_fab);

        mDeviceInfoDao = ((MyApplication)getApplication()).getmGreenDaoManager().getmDaoSession().getDeviceInfoDao();
        mSharePreferencesHelper = ((MyApplication)getApplication()).getmSharePreferenceDevice();
        fab.setOnClickListener((View v)->{
            new Thread(()->{
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setId(4l);
                deviceInfo.setDate(new Date());
                deviceInfo.setName("客厅");
                mDeviceInfoDao.insertOrReplace(deviceInfo);
                Log.e(TAG, "onCreate: "+mDeviceInfoDao.count());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DeviceInfo deviceInfo1 = new DeviceInfo();
                deviceInfo1.setId(5l);
                deviceInfo1.setDate(new Date());
                deviceInfo1.setName("厨房");
                mDeviceInfoDao.insertOrReplace(deviceInfo1);

                Log.e(TAG, "onCreate: "+mDeviceInfoDao.count());
                mSharePreferencesHelper.put("device_count",mDeviceInfoDao.count());
                mSharePreferencesHelper.put("current_device",1);
                mSharePreferencesHelper.put("device_status","off");
            }).start();
        });
    }

    @Override
    public String getToolbarName() {
        return getResources().getStringArray(R.array.view_tile_name)[0];
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
