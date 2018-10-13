package b.com.myapplication.view;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import b.com.myapplication.MyApplication;
import b.com.myapplication.R;
import b.com.myapplication.mode.DeviceInfo;
import b.com.myapplication.mode.DeviceInfoDao;
import b.com.myapplication.mode.DeviceInfoData;
import b.com.myapplication.utils.MessageEvent;
import b.com.myapplication.utils.SharedPreferencesHelper;
import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

@SuppressLint("LongLogTag")
public class DeviceDetailControlActivity extends BaseActivity implements View.OnClickListener ,ViewPager.OnPageChangeListener {
    final static private String TAG = "DeviceDetailControlActivity";
    final static private int NOTIFICATIONS_STARTWORK = 1;
    final static private int WORK_THREAD_PROPERTY = 1;
    private DeviceInfoDao mDeviceInfoDao;
    private SharedPreferencesHelper mSharePreferencesHelper;
    private ScrollerViewPager viewPager;
    private SpringIndicator springIndicator;
    private Button btnBoilingWater;
    private FrameLayout mDeviceDetailView;
    final private ExecutorService priorityThreadPool = new ThreadPoolExecutor(3,
            3, 0, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.device_main_layout_toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.device_main_layout_fab);
        EventBus.getDefault().register(this);
        mDeviceInfoDao = ((MyApplication) getApplication()).getmGreenDaoManager().getmDaoSession().getDeviceInfoDao();
        mSharePreferencesHelper = ((MyApplication) getApplication()).getmSharePreferenceDevice();
        fab.setOnClickListener((View v) -> {
            new Thread(() -> {
                testFun1(mDeviceInfoDao.count()+1);
                Log.e(TAG, "onCreate: " + mDeviceInfoDao.count());
                refurbishDate();
                mSharePreferencesHelper.put("device_count", new Long(mDeviceInfoDao.count()).intValue());
                mSharePreferencesHelper.put("current_device", 1);
                mSharePreferencesHelper.put("device_status", "off");
            }).start();
        });
        Initialize();
        refurbishDate();
    }
    private void testFun1(long index){
        Long id = Long.valueOf(index);
        String name = "";
        for (int i = 0; i< 5 ; i++) {
            name = name + String.valueOf((char)(index + 65));
        }
        Date date = new Date();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setId(id);
        deviceInfo.setName(name);
        deviceInfo.setDate(date);
        mDeviceInfoDao.insertOrReplace(deviceInfo);
    }

    private void Initialize() {
        viewPager = (ScrollerViewPager) findViewById(R.id.view_pager);
        springIndicator = (SpringIndicator) findViewById(R.id.indicator);
        btnBoilingWater = findViewById(R.id.boiling_water);
        mDeviceDetailView = (FrameLayout)findViewById(R.id.device_main_layout_detail_view);

        EmptyView();

        btnBoilingWater.setOnClickListener(this);
        springIndicator.setOnPageChangeListener(this);
    }

    private void EmptyView(){
        PagerModelManager pagerModelManager = new PagerModelManager();
        pagerModelManager.addCommonFragment(GuideFragment.class, getBgRes(), getTitles());
        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), pagerModelManager);
        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();
        springIndicator.setViewPager(viewPager);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        switch (messageEvent.getWhat()) {
            case 2:
                EmptyView();
                break;
            case 1:
                Log.d(TAG, "onMoonEvent: -----------  activity");
                List<DeviceInfoData> data = Lists.newArrayList();
                List<String> dataTitle = Lists.newArrayList();
                List<DeviceInfo> list = (List<DeviceInfo>) messageEvent.getMessage();
                for (int i = 0; i < list.size(); i++ ) {
                    DeviceInfoData infoData = new DeviceInfoData(list.get(i));
                    data.add(infoData);
                    dataTitle.add(String.valueOf(i+1));
                }
                if(list.size() == 1) {
                    data.get(0).setVisibility(false);
                    springIndicator.setVisibility(View.GONE);
                }else {
                    springIndicator.setVisibility(View.VISIBLE);
                    mDeviceDetailView.getLayoutParams().height=dp2px(this,260);
                }
                PagerModelManager pagerModelManager = new PagerModelManager();
                pagerModelManager.addCommonFragment(GuideFragment.class, data, dataTitle);
                ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), pagerModelManager);
                viewPager.removeAllViews();
                viewPager.setAdapter(adapter);
                viewPager.fixScrollSpeed();
                springIndicator.removeAllViews();
                springIndicator.setViewPager(viewPager);
                int id = (int) mSharePreferencesHelper.getSharedPreference("current_device", 0);
                viewPager.setCurrentItem(id);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        int id = (int) mSharePreferencesHelper.getSharedPreference("current_device", 0);
        Log.d(TAG, "onStart: ----------id="+id);
        viewPager.setCurrentItem(id);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSharePreferencesHelper.put("current_device", viewPager.getCurrentItem());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void refurbishDate() {
        priorityThreadPool.execute(new PriorityRunnable(WORK_THREAD_PROPERTY) {

            @Override
            protected void doSomeThing() {
                List<DeviceInfo> list = mDeviceInfoDao.queryBuilder().build().list();
                for (DeviceInfo info : list) {
                    Log.d(TAG, "doSomeThing: " + info.toString());
                }
                if(list.size()>0)
                    EventBus.getDefault().post(new MessageEvent(1, list));
                else {
                    EventBus.getDefault().post(new MessageEvent(2));
                }

            }
        });
    }

    private void startWorker() {
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;//channel的重要性
        String id = "channel_1";//channel的id
        String description = "channel use notify current working status";//channel的描述信息
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(id, "Function", importance);//生成channel
            channel.setDescription(description);
            mNotifyMgr.createNotificationChannel(channel);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, DeviceDetailControlActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, id)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setContentIntent(contentIntent);

        mNotifyMgr.notify(NOTIFICATIONS_STARTWORK, mBuilder.build());
    }

    private List<String> getTitles() {
        return Lists.newArrayList("1");
    }

    private List<DeviceInfoData> getBgRes() {
        int size = (int) mSharePreferencesHelper.getSharedPreference("device_count",0);
        DeviceInfoData data;
        if (size != 0) {
            data = new DeviceInfoData(new DeviceInfo(1l, "test", new Date()));
        }else {
            data = new DeviceInfoData(new DeviceInfo(1l, "null", new Date()));
        }
        if(size <= 1) {
            data.setVisibility(false);
            springIndicator.setVisibility(View.GONE);
            mDeviceDetailView.getLayoutParams().height=dp2px(this,220);
        }
        return Lists.newArrayList(data);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.boiling_water:
                Log.d(TAG, "onClick: ---------" + viewPager.getCurrentItem());

                startWorker();
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "onPageScrolled: position="+position+",positionOffset="+positionOffset+",positionOffsetPixels="+positionOffsetPixels);
    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: "+position);
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d(TAG, "onPageScrollStateChanged: "+state);
    }

    public abstract class PriorityRunnable implements Runnable, Comparable<PriorityRunnable> {
        private int priority;

        public PriorityRunnable(int priority) {
            if (priority < 0) {
                throw new IllegalArgumentException();
            }
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public int compareTo(@NonNull PriorityRunnable another) {
            int me = this.priority;
            int anotherPri = another.getPriority();
            return me == anotherPri ? 0 : me < anotherPri ? 1 : -1;
        }

        @Override
        public void run() {
            doSomeThing();
        }

        protected abstract void doSomeThing();
    }
    private int dp2px(Context context, float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
}
