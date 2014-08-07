import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;




public class Reply implements Runnable
{
	Socket sinthread;
	Client[] clientlist;
	private static int LAMBDA = 4;
	
	public Reply(Socket sinthread, Client[] clientlist)
	{
		this.sinthread = sinthread;
		this.clientlist = clientlist;
	}
	
	@Override
	public void run() 
	{  
        
        if(clientlist[0] == null || clientlist[1] == null)
        {
        	if(clientlist[0] != null)
        	{
        		sendflag(clientlist[0], 0);
        		return;
        	}
        	
        	if(clientlist[1] != null)
        	{
        		sendflag(clientlist[1], 0);
        		return;
        	}
        }
        
        if(clientlist[0].sendFlagOrNot() && !clientlist[1].sendFlagOrNot())
        {	
        	if(clientlist[0].getIPaddress().equals(clientlist[1].getIPaddress()))
        	{
        		
        		sendflag(clientlist[1], 0);
        		
        		clientlist[1].havesentflag();
        		return;
        	}
        	else
        	{
        		checkCollision();
        		return;
        	}
        	
        }
        
        if(!clientlist[0].sendFlagOrNot() && clientlist[1].sendFlagOrNot())
        {	
        	if(clientlist[0].getIPaddress().equals(clientlist[1].getIPaddress()))
        	{
        		
        		sendflag(clientlist[0], 0);
        		return;
        		
        	}
        	else
        	{
        		checkCollision();
        		return;
        	}
        	
        }
        
        if(!clientlist[0].sendFlagOrNot() &&!clientlist[1].sendFlagOrNot())
        { 	
        	if(clientlist[0].getReceiveTime() == 0 && clientlist[1].getReceiveTime() == 0)
			{
				return;
			}
			else if(clientlist[0].getReceiveTime() == 0  && clientlist[1].getReceiveTime() != -1 || clientlist[0].getReceiveTime() != 0 && clientlist[1].getReceiveTime() == 0)
			{
				return;
			}
			else
			{
				if(clientlist[0].getIPaddress().equals(clientlist[1].getIPaddress()))
				{
					sendflag(clientlist[0], 0);
					sendflag(clientlist[1], 0);
					return;
				}
				else
				{
					checkCollision();
					return;
				}

			}
        }

	}
	
	private void checkCollision()
	{
	
			long lowerbound = clientlist[0].getReceiveTime() - LAMBDA*800000;
			long upperbound = clientlist[1].getReceiveTime() + LAMBDA*800000;
			
			if(clientlist[1].getReceiveTime()>lowerbound && clientlist[1].getReceiveTime()<upperbound)
			{
				if(!clientlist[0].sendFlagOrNot())
				{
					sendflag(clientlist[0],1);
				}
				if(!clientlist[1].sendFlagOrNot())
				{
					sendflag(clientlist[1],1);
				}
				System.out.println("collision!!!!!");
			}
			else											//no collision
			{
				if(!clientlist[0].sendFlagOrNot())
				{
					sendflag(clientlist[0],0);
				}
				if(!clientlist[1].sendFlagOrNot())
				{
					sendflag(clientlist[1],0);
				}
				
				System.out.println("no collision!");
			}

	}

	private void sendflag(Client client, int collisionflag) 
	{
		client.havesentflag();
		
		try {
			
			OutputStream out = sinthread.getOutputStream();
			Byte byteflag = (byte)collisionflag;
			out.write(byteflag);
			out.close();
			sinthread.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
