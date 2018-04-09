package com.example.sunhq.a2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Sunhq on 2018/4/9.
 */
public class Card extends FrameLayout {
    public Card(Context context) {
        super(context);

        label = new TextView(getContext());
        label.setTextSize(32);
        label.setTextColor(0xff00CCFF); //数字的背景
        label.setBackgroundColor(0x3366CCFF);  //文字的背景
        label.setGravity(Gravity.CENTER);
        LayoutParams layoutParams = new LayoutParams(-1,-1);
        layoutParams.setMargins(10,10,0,0);
        addView(label,layoutParams);

        setNum(0);
    }

    private int num = 0;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if (num <= 0){
            label.setText("");
        } else {
            label.setText(num + "");
        }

    }

    public boolean equals(Card o) {
        return getNum() == o.getNum(); //判断数字是否相等
    }

    public TextView label;


}
