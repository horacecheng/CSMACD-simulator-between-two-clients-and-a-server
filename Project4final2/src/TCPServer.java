
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPServer {

    ServerSocket ss = null;
    Socket incoming = null;

    public void go() {

        try {

            ss = new ServerSocket(5555);

            while (true) {

                
            	incoming = ss.accept();
                System.out.println("Incoming: " + incoming.getInetAddress());
                new Thread(new Hello(incoming)).start();

            }

        } catch (IOException e) {

            e.printStackTrace();
        } finally {

            try {
                ss.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    
    public static void main(String[] args)
    {
    	new TCPServer().go();
    }
    
}
