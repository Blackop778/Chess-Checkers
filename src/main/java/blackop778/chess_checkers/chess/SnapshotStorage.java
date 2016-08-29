package blackop778.chess_checkers.chess;

import java.util.ArrayList;

public abstract class SnapshotStorage 
{
	private static ArrayList<Snapshot> snapshots;
	private static ArrayList<Byte> count;
	
	public static void initialize(boolean chess)
	{
		if(chess)
		{
			snapshots = new ArrayList<Snapshot>();
			count = new ArrayList<Byte>();
		}
		else
		{
			snapshots = null;
			count = null;
		}
	}
	
	public static boolean addSnapshot(Snapshot shot)
	{
		int index = snapshots.indexOf(shot);
		if(index == -1)
		{
			snapshots.add(shot);
			count.add(new Byte((byte) 0));
		}
		else
		{
			count.get(index)
		}
	}
}
