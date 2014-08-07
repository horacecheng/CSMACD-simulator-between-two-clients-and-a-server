
public class Packet
{
	int id;
	int collision;
	int slot;
	
	
	public Packet(int id)
	{
		this.id = id;
		slot = 0;
		collision = 0;
	}
	
	public int getCollision()
	{
		return collision;
	}
	
	public void addOneCollision()
	{
		collision++;
	}

	
	public int getID()
	{
		return id;
	}
	
	public void addDelay(int slot)
	{
		this.slot = this.slot +slot;
	}

	public int getSlot()
	{
		return slot;
	}

}
