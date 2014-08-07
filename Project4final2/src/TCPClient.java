import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;


public class TCPClient 
{
	final static int LAMBDA = 20;
	private static final String serverip = "192.168.1.105";
	static int port = 5555;
	final static double slot = LAMBDA*0.8;				//ms
    long  delay;
    final static long simulationtimeslot =(long) (0.8 *5000 * 1000000);
    static int totalpacketswillsend = (int) (simulationtimeslot/(LAMBDA*0.8*1000000));		//= total timeslot that require for the simulation
    static int  totalslot;
    
    static int counter  = 0;
    int packetid = 0;
    
    public static void main(String[] args) throws Exception
	{
		System.out.println("test lambda = " + LAMBDA);
		
		LinkedList<Packet> packetlist = new LinkedList<Packet>();

		for(int i = 0 ; i<totalpacketswillsend ; i++)
		{
			Packet packet = new Packet(i);
			packetlist.add(packet);
		}
		int j = packetlist.size();
		
		for(int i = 0 ; i<j ; i++)		//assume one loop spent LAMBDA slots
		{
			Packet packet = packetlist.get(i);
				
			try
			{	
				Socket s = new Socket(serverip, port);
				
				DataInputStream in = new DataInputStream(s.getInputStream());
	    		DataOutputStream out = new DataOutputStream(s.getOutputStream());
	    		out.write(new byte[1024], 0, 1024);
	    		int flag = in.read();

	        	if(flag == 0)
	        	{
	        		counter++;
	        		totalslot = totalslot +packet.getSlot();
	        		System.out.println(packet.getID() + "packet has no collision");
	        		
	        	}
	        	else
	        	{
	        		System.out.println(packet.getID() + "packet has a collision");
	        		packet.addOneCollision();
	        		int slot = backoff(packet);
	        		if(slot ==-1 )
	        		{
	        			System.out.println("Too many collision that we cannot transmit" + packet.getID() + " packet again..");
	        			continue;
	        		}
	        		if(slot+i <j)
	        		{
	        			packetlist.add(slot+i, packet);
	        			packet.addDelay(slot);
	        		}
	        		else
	        		{
	        			System.out.println("Over Time!" + i + " packet cannot transmit");
	        		}
	        		continue;
	        	}

		    	s.close();
			}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
			
		}
		

	    	double throughput = (double)counter/totalpacketswillsend ;
	    	double averagedelay = totalslot/counter;
	    	System.out.println("Throughtput: " +throughput + "packet/timeslot");
	    	System.out.println("Average delay:" + averagedelay + "timeslot");

				
	}
    
	public static int backoff(Packet packet)			//expotential binary backoff algorithm
	{
		Random generator = new Random();
		if(packet.getCollision()>16)
		{
			return -1;
		}
		int k = Math.min(packet.getCollision(), 10);
		int power =(int) Math.pow(2, k);
		int delayslot = generator.nextInt(power);
		return delayslot;
	}
}
