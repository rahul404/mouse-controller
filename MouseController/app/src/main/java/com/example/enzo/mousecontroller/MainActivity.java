package com.example.enzo.mousecontroller;

import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private Button mLeftButton,mRightButton;
    private RequestHandler mRequestHandler;
    private static final String DEBUG_TAG="HIIII";
    private Point mCurrent,mPast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCurrent=new Point(0,0);
        mPast=new Point(0,0);
        mRequestHandler=new RequestHandler();
        mRequestHandler.start();
        mRequestHandler.onLooperPrepared();
        mTextView=(TextView)findViewById(R.id.movement_indicator);
        mLeftButton=(Button)findViewById(R.id.left_button);
        mRightButton=(Button)findViewById(R.id.right_button);
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestHandler.queueTask(RequestHandler.LEFT_CLICK,0,0);
                Toast.makeText(MainActivity.this,"LEFT",Toast.LENGTH_SHORT).show();
            }
        });
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestHandler.queueTask(RequestHandler.RIGHT_CLICK,0,0);
                Toast.makeText(MainActivity.this,"RIGHT",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);
        int size=event.getPointerCount();
        String msg;
        if (size>1){
            msg="Size="+size+" More than one pointer:"+event.getX()+","+event.getY();
            mTextView.setText(msg);
            return true;
        }

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                msg="Size="+size+"ACTION_DOWN at "+event.getX()+","+event.getY();
                mTextView.setText(msg);
                return true;
            case (MotionEvent.ACTION_MOVE) :
                if(event.getHistorySize()>0){
                    mPast.x=(int)event.getHistoricalAxisValue(MotionEvent.AXIS_X,0);
                    mPast.y=(int)event.getHistoricalAxisValue(MotionEvent.AXIS_Y,0);
                }
                else
                    return true;
                msg="Size="+size+"ACTION_MOVE at "+(mCurrent.x-mPast.x)+","+(mCurrent.y-mPast.y);
                mCurrent.x=(int)event.getX();
                mCurrent.y=(int)event.getY();
                mTextView.setText(msg);
                mRequestHandler.queueTask(RequestHandler.MOVE,mCurrent.x-mPast.x,
                        mCurrent.y-mPast.y);
                return true;
            case (MotionEvent.ACTION_UP) :
                msg="Size="+size+"ACTION_MOVE at "+event.getX()+","+event.getY();
                mTextView.setText(msg);
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                msg="Size="+size+"ACTION_CANCEL at "+event.getX()+","+event.getY();
                mTextView.setText(msg);
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                msg="Size="+size+"ACTION_OUTSIDE at "+event.getX()+","+event.getY();
                mTextView.setText(msg);
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mRequestHandler.stopHandler();
    }

}
