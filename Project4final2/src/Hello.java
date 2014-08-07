
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;

import java.net.Socket;


public class Hello implements Runnable {

    InputStream is = null;
    OutputStream os = null;
    boolean isdone = false;
    Socket sinthread = null;
    static Client[] clientlist = new Client[2];

    static int counter = 0;
	
	public Hello(Socket s)
	{
		clientlist[counter] = new Client(s);
		this.sinthread = s;
	}

	@Override
	public void run() 
	{
		if(sinthread.isConnected())
		{
			System.out.println(sinthread.getInetAddress() + ", Welcome to my server");
			
		}
		
		try
		{
			is = sinthread.getInputStream();
			//System.out.println("InputStream: " + is);
			
			int packetsize = 1024;
			byte[] rbuf = new byte[packetsize];
			
			
			if(counter == 0)
			{
				clientlist[0].setReceiveTime(System.nanoTime());
				counter++;
			}
			else
			{
				clientlist[1].setReceiveTime(System.nanoTime());
				counter--;
			}
			
			if(clientlist[0].getReceiveTime() != 0 ||clientlist[1].getReceiveTime() != 0 )
			{
				is.read(rbuf, 0, packetsize);
				System.out.println("Receive from " + sinthread.getInetAddress() + "'s 1KB package");
				
				Thread t = new Thread(new Reply(this.sinthread, clientlist) );
				t.start();
				try {
					t.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			

		}catch(IOException e)
		{
			System.out.println(sinthread.getInetAddress() + "'s connection s terminated");
		}finally{
			System.out.println(sinthread.getInetAddress() + " See you again!!!");
			try {
				sinthread.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}