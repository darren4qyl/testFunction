package b.com.myapplication.animation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
    ArrayList<Map<String,ImageView>> allAnimaObject = new ArrayList<>();
    Button mSwitchBtn = null;

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
        for (int i = 0; i< 40 ;i++){
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

        start();
    }
    public void start(){
        new Thread(()->{
            while(true) {
                for(int i=0; i< allAnimaObject.size();i++) {
                    ImageView view = allAnimaObject.get(i).get("image");
                    if(view.getAnimation()==null) {
                        RotateAnimation animation = new RotateAnimation(0f, 360f,
                                Animation.RELATIVE_TO_SELF, 0.59f, Animation.RELATIVE_TO_SELF, 0.59f);
                        animation.setFillAfter(true); // 设置保持动画最后的状态
                        animation.setDuration(800); // 设置动画时间
                        //animation.setRepeatCount(10);
                        animation.setInterpolator(new AccelerateInterpolator()); // 设置插入器
                        view.setAnimation(animation);
                    }else
                        view.startAnimation(view.getAnimation());
                    try {
                        Thread.sleep(100);
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
