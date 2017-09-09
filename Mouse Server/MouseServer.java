import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
public class  MouseServer
{

	//REQUESTS to be handled
	public static final int LEFT_CLICK=1;
    public static final int RIGHT_CLICK=2;
    public static final int SCROLL_UP=4;
    public static final int SCROLL_DOWN=8;
    public static final int MOVE=16;
    public static final int MOVE_RIGHT=32;
	public static final int LEFT=InputEvent.BUTTON1_DOWN_MASK;
	public static final int RIGHT=InputEvent.BUTTON3_DOWN_MASK;;
	ServerSocket ss;
	Socket s;
	public static final int port=8888;
	
	Robot robot;

	MouseServer() throws IOException,AWTException
	{
		ss=new ServerSocket(port);
		robot=new Robot();
	}
	public void startListenning() throws IOException,Exception
	{
		System.out.println("started listenning");
		s=ss.accept();
		System.out.println("accepted");
		BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		while(true)
		{
			int what=Integer.parseInt(br.readLine());
			int x=Integer.parseInt(br.readLine());
			int y=Integer.parseInt(br.readLine());
			//String what=br.readLine();
			System.out.println("what= "+what);
			System.out.println("x= "+x);
			System.out.println("y= "+y);
			switch(what)
			{
				case LEFT_CLICK:
					robot.mousePress(LEFT);
					//Thread.sleep(1000);
					robot.mouseRelease(LEFT);
					break;
				case RIGHT_CLICK:
					robot.mousePress(RIGHT);
					//Thread.sleep(1000);
					robot.mouseRelease(RIGHT);
					break;
				case MOVE:
					PointerInfo info=MouseInfo.getPointerInfo();
					Point point=info.getLocation();
					System.out.println("mouse x= "+point.x);
					System.out.println("mouse y= "+point.y);
					robot.mouseMove(point.x+x,point.y+y);
					break;
			}
		}
	}
	public static void main(String[] args) throws IOException,AWTException,Exception
	{
		MouseServer ms=new MouseServer();
		ms.startListenning();
	}
}
