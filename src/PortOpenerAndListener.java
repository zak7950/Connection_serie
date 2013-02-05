import gnu.io.*;
import java.util.Enumeration; 

public class PortOpenerAndListener extends Thread implements CommPortOwnershipListener 
{
	private CommPortIdentifier portID;
	private CommPort port;
	private boolean releasePortOnRequest;
	private boolean ownPort;
	public PortOpenerAndListener(String threadName, String portName, boolean strong) throws NoSuchPortException    
	{
		super(threadName);
		this.releasePortOnRequest = strong;
		this.portID = CommPortIdentifier.getPortIdentifier(portName);
		this.port = null;
		this.ownPort = false;
	}
	
	 public void run()    
	 {
		 System.out.print("["+this.getName()+"] starts and try to open port "                           + this.portID.getName()+"\n");
		 this.portID.addPortOwnershipListener(this);
		 try {Thread.sleep(1000);
		 } catch (InterruptedException e) {}       try       {
			 this.port = this.portID.open(this.getName(), 2000);
			 this.ownPort = true;
			 }       
		 catch (PortInUseException e)       
		 {          System.err.print("["+this.getName()+"] stated that "                              + this.portID.getName()+" is currently in use\n");
		 this.portID.removePortOwnershipListener(this);
		 System.out.print("["+this.getName()+"] ends\n");
		 return;       }       System.out.print("["+this.getName()+"] owns port " + this.portID.getName()+"\n");
		 try {
			 Thread.sleep(3000);
			 }
		 catch (InterruptedException e) {}       
		 System.out.print("["+this.getName()+"] closes port " + this.portID.getName()+"\n");
		 this.port.close();
		 this.ownPort = false;
		 this.portID.removePortOwnershipListener(this);
		 System.out.print("["+this.getName()+"] ends\n");
	}
	 
	 
	   public void ownershipChange(int state)     
	   {
		   switch (state)         
		   {
			   case CommPortOwnershipListener.PORT_OWNED :               System.out.print("["+this.getName()+"] notified that port "                                + this.portID.getName()+" is now owned by "                                + this.portID.getCurrentOwner()+"\n");
			   return;
			   case CommPortOwnershipListener.PORT_UNOWNED :              System.out.print("["+this.getName()+"] notified that port "                               + this.portID.getName()+" has been released\n");    
			   return;
			   case CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED :              System.out.print("["+this.getName()+"] notified that port "                               + this.portID.getName()+" is requested\n"); 
			   if (this.ownPort)              {                 if (this.releasePortOnRequest)                 {                   System.out.print("["+this.getName()+"] releases port "                                    + this.portID.getName()+"\n");   
			   this.port.close(); this.ownPort = false; return;                 }                 System.out.print("["+this.getName()+"] does not release port "                                   + this.portID.getName()+"\n");     
			   
			   }              
		   return; 
		   
		   }   
		}
	   
	   
	   public static void main(String[] args)    
	   {       
		   for (int i=0;i<5;i++)       {
			   try 
			   {
				   System.out.print("test");
				   	new PortOpenerAndListener("POL"+i,args[0],(i%2==0)).start();
	   } 
			  catch (NoSuchPortException e)          
			   {
				   System.err.print("No such port "+args[0]+", exiting...\n");
				   System.exit(1); 
		   }   
	   try 
	   {
		   Thread.sleep(1000);
	   } 
	   catch (InterruptedException e) {}      
	   
		   }   
	  
	   }
	  
}
	 
	 
	 
