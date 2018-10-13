package b.com.myapplication.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.ArrayList;

import b.com.myapplication.R;
import b.com.myapplication.mode.DeviceInfo;

public class DeviceDetailWidget extends LinearLayout {
    final static private String TAG = "DeviceDetailWidget";
    private ArrayList<DeviceInfo> mDevicesInfo = new ArrayList<>();
    private LinearLayout bottom;


    public DeviceDetailWidget(Context context) {
        super(context);
        Initialization(context);
    }

    public DeviceDetailWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Initialization(context);
    }

    public DeviceDetailWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Initialization(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DeviceDetailWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Initialization(context);
    }

    private void Initialization(Context context){
        setBackgroundResource(R.drawable.device_info_background);
        setOrientation(VERTICAL);
        LinearLayout detail = initDeviceDetailView(context);
        LinearLayout.LayoutParams detailParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        detailParams.weight = 1;
        addView(detail,detailParams);
        bottom = initDeviceBottomView(context);
        LayoutParams bottomParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        bottomParams.weight = 8;

        addView(bottom,bottomParams);

    }
    private LinearLayout initDeviceDetailView(Context context) {
        LinearLayout view = (LinearLayout) ((Activity)context).getLayoutInflater().inflate(R.layout.chaild_device_detail_view,null);

        return view;
    }
    private LinearLayout initDeviceBottomView(Context context) {
        LinearLayout view = new LinearLayout(context);
        return view;
    }
    public void setVisibility(int isVisible){
        bottom.setVisibility(isVisible);
    }
}
