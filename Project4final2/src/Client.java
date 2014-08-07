import java.net.InetAddress;
import java.net.Socket;


public class Client
{
	long receivetime;
	boolean sendflag;
	Socket socket;
	InetAddress ipaddress;
	
	public Client(Socket socket)
	{
		this.socket = socket;
		
		ipaddress = socket.getInetAddress();
		this.receivetime = 0;
		sendflag = false;
	}
	
	public boolean sendFlagOrNot()
	{
		return sendflag;
	}
	
	public void havesentflag()
	{
		sendflag = true;
	}
	
	public void setReceiveTime(long receivetime)
	{
		this.receivetime = receivetime;
	}
	
	public long getReceiveTime()
	{
		return receivetime;
	}
	
	public InetAddress getIPaddress()
	{
		return ipaddress;
	}

}