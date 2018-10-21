/*
 * Copyright 2015 chenupt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package b.com.myapplication.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.util.concurrent.Runnables;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import b.com.myapplication.R;
import b.com.myapplication.mode.DeviceInfo;
import b.com.myapplication.mode.DeviceInfoData;
import b.com.myapplication.utils.MessageEvent;
import b.com.myapplication.widget.DeviceDetailWidget;

/**
 * Created by chenupt@gmail.com on 2015/1/31.
 * Description TODO
 */
public class GuideFragment extends Fragment implements View.OnClickListener {
    final static String TAG = "GuideFragment";
    private int bgRes;
    private ImageView imageViewSwitch;
    private TextView tvDeviceInfoName;
    private DeviceInfoData info;
    private DeviceDetailWidget mDeviceDetail;
    private ArrayList<Integer> mSwitchPicRecouseId = new ArrayList<>(40);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = (DeviceInfoData) getArguments().get("data");
        Log.d(TAG, "onCreate: "+info.getName());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_guide, container, false);
        tvDeviceInfoName = view.findViewById(R.id.device_info_name);
        mDeviceDetail = view.findViewById(R.id.fragment_device_detail);
        imageViewSwitch = view.findViewById(R.id.device_info_detail_switch);
        if(!info.isVisibility()){
            Log.d(TAG, "onCreateView: ----------- view gone");
            mDeviceDetail.setVisibility(View.GONE);
            view.requestLayout();
        }
        imageViewSwitch.setOnClickListener(this);
        new Thread(new InitializeResourceRunner(this.getContext())).start();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvDeviceInfoName.setText(info.getName());
        if(info.getName().equals("null")){
            tvDeviceInfoName.setText("none device");
        }
//        imageView = (ImageView) getView().findViewById(R.id.image);
//        imageView.setBackgroundResource(bgRes);
    }



    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.device_info_detail_switch:
                break;
        }
    }

    private void powerSwitch(boolean status) {
        int index = (status)? 0 : 40;
        for (int i = index; status? i >= 40 : i < 0; i++) {
         //   imageViewSwitch.setImageResource();
        }

    }
    private class InitializeResourceRunner implements Runnable {
        private Context mContext;
        public InitializeResourceRunner(Context context) {
            mContext = context;
        }

        @Override
        public void run() {
            for(int i=1; i<= 40 ;i++){
                String index = String.format("%02d",i);
                mSwitchPicRecouseId.add(getDrawableId(mContext,"lock_light_btn_00"+index));
                Log.d(TAG, "run: ---id:"+getDrawableId(mContext,"lock_light_btn_00"+index)+",index="+index);
                Glide.with(mContext).load(getDrawableId(mContext,"lock_light_btn_00"+index));
            }
        }
        private int getIdentifierByType(Context context, String resourceName, String defType) {
            return context.getResources().getIdentifier(resourceName,
                    defType,
                    context.getPackageName());
        }
        public int getDrawableId(Context context, String resourceName) {
            return getIdentifierByType(context, resourceName, "drawable");
        }
    }
}
