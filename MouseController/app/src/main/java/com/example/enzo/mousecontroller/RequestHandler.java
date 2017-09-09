package com.example.enzo.mousecontroller;

import android.graphics.Point;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by enzo on 3/29/2017.
 */

public class RequestHandler  extends HandlerThread{
    //REQUESTS to be handled
    public static final int LEFT_CLICK=1;
    public static final int RIGHT_CLICK=2;
    public static final int SCROLL_UP=4;
    public static final int SCROLL_DOWN=8;
    public static final int MOVE=16;
    public static final int MOVE_RIGHT=32;

    //HANDLER name
    private static final String NAME="RequestHandler";


    //reference to the handler
    private Handler mRequestHandler;

    //reference to the communicating class
    Socket mSocket;
    PrintWriter mPrintWriter;

    //private class to send the messages to the server
    private class Transmitter{
        Socket s;
        String mIp;
        int mPort;
        PrintWriter pw;
        Transmitter(String ip, int port){
            mIp=ip;
            mPort=port;
        }

        //this method is to be invoked before any nio takes place
        //it established the connection
        public void establishConnection(){
            try{
                Log.e("ERROR",mIp+" "+mPort);
                s=new Socket(mIp,mPort);
                pw=new PrintWriter(s.getOutputStream(),true);
            }
            catch(IOException ioe){
                Log.e("ERROR","establish connection error");
            }
        }
        public void sendMessage(Message msg){
            int what=msg.what;
            Point point=(Point)msg.obj;
            pw.println(""+what);
            //pw.println(""+point.x);
            //pw.println(""+point.y);
        }
    }

    public RequestHandler() {
        super(NAME);
    }
    public void establishConnection(String ip,int port){
        try{
            mSocket=new Socket(ip,port);
            mPrintWriter=new PrintWriter(mSocket.getOutputStream(),true);
        }
        catch(IOException ioe){
            Log.e("ERROR","establish conection error");
        }
    }

    //this method wil queue the task for us
    public void queueTask(int what,int dx,int dy){
        Message message=mRequestHandler.obtainMessage();
        message.what=what;
        Point point=new Point(dx,dy);
        message.obj=point;
        message.sendToTarget();
    }
    @Override
    protected void  onLooperPrepared(){
        mRequestHandler=new Handler(){
          @Override
            public void handleMessage(Message msg){
                if(mSocket==null){
                    //establishConnection("10.0.2.2",8888);
                    establishConnection("192.168.0.103",8888);
                }
                //code to send message
                mPrintWriter.println(""+msg.what);
                Point point=(Point)msg.obj;
                mPrintWriter.println(""+point.x);
                mPrintWriter.println(""+point.y);
                Log.i("ERROR","Message sent");
          }
        };
    }
    public void stopHandler(){
        quit();
    }

}
