package b.com.myapplication.animation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import b.com.myapplication.R;

import static b.com.myapplication.R.drawable.shape;

public class VrilleButton extends FrameLayout {
    final static String TAG = "vrillebutton";
    ArrayList<Map<String,ImageView>> allAnimaObject = new ArrayList<>();
    Button mSwitchBtn = null;
    final private int ADD_SWITCH_CONTROL_ANIMATION = 0;
    final private int CONTINUE_RUNNING_ANIMATION = 1;
    private Handler mainHandler = null;

    public VrilleButton(@NonNull Context context) {
        super(context);
        InitializationView(context);
    }

    public VrilleButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitializationView(context);
    }

    public VrilleButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitializationView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VrilleButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        InitializationView(context);
    }

    private void InitializationView(Context context) {
        mSwitchBtn = new Button(context);
        mSwitchBtn.setText("1111");
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        mSwitchBtn.setLayoutParams(params);
        // mSwitchBtn.setAlpha(0);
        this.addView(mSwitchBtn);
        for (int i = 0; i< 10 ;i++){
            ImageView view = new ImageView(context);
            FrameLayout.LayoutParams l = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            l.gravity = Gravity.CENTER;
            view.setLayoutParams(l);
            view.setImageResource(shape);
            view.setBackgroundColor(0xFF00FF);
            Map<String,ImageView> src = new HashMap<>();
            src.put("image",view);
                allAnimaObject.add(src);
            this.addView(view);
        }

        mainHandler = new Handler(context.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                    ImageView view = allAnimaObject.get(msg.arg1).get("image");
                    switch (msg.what) {
                        case ADD_SWITCH_CONTROL_ANIMATION:
                            RotateAnimation animation = new RotateAnimation(0f, 360f,
                                    Animation.RELATIVE_TO_SELF, 0.48f, Animation.RELATIVE_TO_SELF, 0.48f);
                            animation.setFillAfter(true); // 设置保持动画最后的状态
                            animation.setDuration(5000); // 设置动画时间
                            //animation.setRepeatCount(10);
                            animation.setInterpolator(new AccelerateInterpolator()); // 设置插入器
                            view.startAnimation(animation);
                            break;
                        case CONTINUE_RUNNING_ANIMATION:
                            view.startAnimation(view.getAnimation());
                            break;

                }
                super.handleMessage(msg);
            }
        };
        start();
    }
    public void start(){
        new Thread(()->{
            while(true) {
                    for (int i = 0; i < allAnimaObject.size(); i++) {
                        ImageView view = allAnimaObject.get(i).get("image");
                            if (view.getAnimation() == null) {
                                mainHandler.obtainMessage(ADD_SWITCH_CONTROL_ANIMATION, i, 0).sendToTarget();


                            } else
                                mainHandler.obtainMessage(CONTINUE_RUNNING_ANIMATION, i, 0).sendToTarget();
                        try {
                            Thread.sleep(700);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }).start();
    }
}
