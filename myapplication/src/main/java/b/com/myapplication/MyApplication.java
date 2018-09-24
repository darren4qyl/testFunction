package b.com.myapplication;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import b.com.myapplication.manager.GreenDaoManager;
import b.com.myapplication.utils.SharedPreferencesHelper;

public class MyApplication extends TinkerApplication {
    private SharedPreferencesHelper mSharePreferenceDevice = null;
    private GreenDaoManager mGreenDaoManager = null;

    public SharedPreferencesHelper getmSharePreferenceDevice() {
        return mSharePreferenceDevice;
    }

    public GreenDaoManager getmGreenDaoManager() {
        return mGreenDaoManager;
    }

    public MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "b.com.myapplication.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGreenDaoManager = GreenDaoManager.getInstance(this);
        mSharePreferenceDevice = new SharedPreferencesHelper(this,"devices_status");
        CrashReport.initCrashReport(getApplicationContext(), "ee9fc06fbb", false);
    }
}
