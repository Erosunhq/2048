package com.example.sunhq.a2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunhq on 2018/4/9.
 */
public class GridViewGame extends GridLayout {
    boolean merge = false;




    public GridViewGame(Context context) {
        super(context);
        initGameView();
    }

    public GridViewGame(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GridViewGame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    protected void initGameView(){
        setColumnCount(4); //指明4列
        //setBackgroundColor(0xffADFF2F);  // 主窗口的背景
        //setBackground(getResources().getDrawable(R.mipmap.background));
        // 从用户手势滑动方向判断用户的意图
        setOnTouchListener(new OnTouchListener() {

            private float startX,startY,offsetX,offsetY; //记录用户手指按下和离开,以及偏移的位置

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                // 往左
                                swipeLeft();
                            } else if (offsetX > 5) {
                                //
                                swipeRight();
                            }
                        }else {
                                if (offsetY < -5) {
                                    // 往上
                                    swipeUp();
                                }else if (offsetY > 5) {
                                    // 往下
                                    swipeDown();
                                }
                        }
                        break;
                    default:
                        break;
                }


                return true; //如果是false只能监听到touch down事件
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) { //考虑到不同手机的宽高
        super.onSizeChanged(w, h, oldw, oldh);

        int cardWidth = (Math.min(w,h) - 10) / 4;
        addCards(cardWidth,cardWidth);
        startGame();
    }

    private void addCards(int cardWidth,int cardHeight) {
        Card card;
        for (int y = 0;y < 4;y++){
            for (int x = 0; x < 4;x++){
                card = new Card(getContext());
                card.setNum(0); //开始全部置为0
                addView(card,cardWidth,cardHeight);

                cardMap[x][y] = card; //记录添加的卡片
            }
        }
    }

    public void startGame(){
        MainActivity.getMainActivity().clearScore(); //开始的时候清零
        for (int y = 0; y < 4;y++){
            for (int x = 0;x <4;x++){
                cardMap[x][y].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();// 添加两个随机数
    }

    // 添加随机数
    private void addRandomNum(){
        emptyPoints.clear();
        for (int y = 0;y < 4;y++){
            for (int x = 0;x < 4;x++){
                if (cardMap[x][y].getNum() <= 0){ //未添加数字的才可以再添加
                    emptyPoints.add(new Point(x,y));
                }
            }
        }
        Point p = new Point();
        if(emptyPoints!=null && emptyPoints.size()>0){
            p = emptyPoints.remove((int)(Math.random() * emptyPoints.size()));
        }
        cardMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4); // 按1:9的比例随机添加2和4
    }

    //方法类
    private void swipeLeft(){

        boolean merge = false;

        for (int y = 0; y < 4;y++){
            for (int x = 0;x <4;x++){
                for (int x1 = x+1;x1 < 4;x1++){
                    if (cardMap[x1][y].getNum() > 0){
                        if (cardMap[x][y].getNum() <= 0){ //为空
                            cardMap[x][y].setNum(cardMap[x1][y].getNum());
                            cardMap[x1][y].setNum(0);
                            x--; //
                            merge = true;
                        }else if (cardMap[x][y].equals(cardMap[x1][y])){ //不为空,并且两张卡片的值是相同的
                            cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
                            cardMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
            if (merge){
                addRandomNum();
                checkComplete();
            }
        }
    }



    private void swipeRight(){
        boolean merge = false;
        for (int y = 0; y < 4;y++){
            for (int x = 3;x >=0;x--){
                for (int x1 = x-1;x1>=0;x1--){
                    if (cardMap[x1][y].getNum() > 0){
                        if (cardMap[x][y].getNum() <= 0){ //为空
                            cardMap[x][y].setNum(cardMap[x1][y].getNum());
                            cardMap[x1][y].setNum(0);

                            x++; //

                            merge = true;
                        }else if (cardMap[x][y].equals(cardMap[x1][y])){ //不为空,并且两张卡片的值是相同的
                            cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
                            cardMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge){
            addRandomNum();
            checkComplete();
        }

    }
    private void swipeUp(){
        boolean merge = false;
        for (int x = 0; x < 4;x++){
            for (int y = 0;y <4;y++){

                for (int y1 = y+1;y1 < 4;y1++){
                    if (cardMap[x][y1].getNum() > 0){

                        if (cardMap[x][y].getNum() <= 0){ //为空
                            cardMap[x][y].setNum(cardMap[x][y1].getNum());
                            cardMap[x][y1].setNum(0);

                            y--; //
                            merge = true;
                        }else if (cardMap[x][y].equals(cardMap[x][y1])){ //不为空,并且两张卡片的值是相同的
                            cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
                            cardMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge){
            addRandomNum();
            checkComplete();
        }

    }
    private void swipeDown(){

        for (int x = 0; x < 4;x++){
            for (int y = 3;y >= 0;y--){

                for (int y1 = y-1;y1 >= 0;y1--){
                    if (cardMap[x][y1].getNum() > 0){

                        if (cardMap[x][y].getNum() <= 0){ //为空
                            cardMap[x][y].setNum(cardMap[x][y1].getNum());
                            cardMap[x][y1].setNum(0);

                            y++; //
                            merge = true;
                        }else if (cardMap[x][y].equals(cardMap[x][y1])){ //不为空,并且两张卡片的值是相同的
                            cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
                            cardMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge){
            addRandomNum();
            checkComplete();
        }

    }
/*
    //滑动动画, 音效等
    private void invokeAnimate() {
        if (merge) {
            new WaveThread("merge.wav").start();
            moveAnimate();
            mergeAnimate();
        } else if (isMove) {
            new WaveThread("move.wav").start();
            moveAnimate();
        }
        if (isMerge || isMove) {
            createTile();
            isMerge = false;
            isMove = false;
        }
    }*/

    // 判断游戏结束
    private void checkComplete(){
        boolean complete = true;
        ALL:
        for (int y = 0; y < 4;y++) {
            for (int x = 0; x < 4; x++)
                if (cardMap[x][y].getNum() == 0 ||
                        (x > 0 && cardMap[x][y].equals(cardMap[x - 1][y])) ||
                        (x < 3 && cardMap[x][y].equals(cardMap[x + 1][y])) ||
                        (y > 0 && cardMap[x][y].equals(cardMap[x][y - 1])) ||
                        (y < 3 && cardMap[x][y].equals(cardMap[x][y + 1]))) { //四个方向上有相同数字
                    complete = false;
                    break ALL;  //为了能跳出内层循环和外层循环

                }
        }
        if (complete){
            new AlertDialog.Builder(getContext()).setTitle("您好")
                    .setIcon(R.mipmap.icon)
                    .setMessage("游戏结束,您的最终得分是: "+MainActivity.getMainActivity().score)

                    .setPositiveButton("不服重来", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                            MainActivity.getMainActivity().showScore(); //清零后显示
                        }
                    })
                    .show();
        }

    }

    public Card[][] cardMap  = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();
}
