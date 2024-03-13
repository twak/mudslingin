package mud.trunk;
import mud.supporting.IntPos;
import java.io.*;
import java.util.*;

public class aContact
{
	public aContact(int size)
	{
		//LinkedList somelist[] = new LinkedList();
		hashtab = new LinkedList[size];
		tablesize = size;
		tableInit();
	}

	public void tableInit()
	{
		int i = 0;

		for(i = 0; i < tablesize; i++)
		{
			hashtab[i] = new LinkedList();
		}
	}

	public int nMod(int ino, int mno)
	{
		int modu = 0;
		int ans = 0;

		if (mno != 0)
		{
			ans  = ino/mno;
			modu = ino - ans;
		}

		return modu;

	}

	public int hashKey(IntPos px)
	{
		int hkey = 0;
		int hx = 0;
		int hy = 0;
		double hkey1 = 0;
		hx = px.getX();
		hy = px.getY();

		hkey = ((hx + 2)* 5) + (hy * 2);
		hkey = hkey - (hx - (hy + 3));
		hkey = nMod((tablesize - 1), hkey);

		return hkey;
	}

	public void addElement(IntPos addOb)
	{
		IntPos tableOb = new IntPos();
		int hashVal = 0;

		hashVal = hashKey(addOb);
		tableOb.set(addOb.getX(), addOb.getY());
		hashtab[hashVal].addLast(tableOb);
	}

	public void removeElement(IntPos remOb)
	{
		Object tableOb;
		IntPos tableVal = new IntPos();
		ListIterator iterator;
		boolean isRemoved = false;
		int hval;

		hval = hashKey(remOb);
		iterator = hashtab[hval].listIterator();

		while (iterator.hasNext() && isRemoved == true)
		{
			tableOb = iterator.next();
			tableVal = (IntPos)tableOb;

			if (tableVal.getX() == remOb.getX() && tableVal.getY() == remOb.getY())
			{
				iterator.remove();
				isRemoved = true;
			}
		}
	}

	public boolean cont(IntPos comOb)
	{
		Object listOb;
		IntPos listIn = new IntPos();
		LinkedList iterlist;
		ListIterator iterator;
		int hVal = 0;
		boolean isfound = false;

		hVal = hashKey(comOb);
		iterator = (hashtab[hVal]).listIterator();

		while (iterator.hasNext() && isfound == false)
		{
			listOb = iterator.next();
			listIn = (IntPos)listOb;

			if (listIn.getX() == comOb.getX() && listIn.getY() == comOb.getY())
			{
				isfound = true;
			}
		}

		return isfound;

	}


	private LinkedList hashtab[];
	private int tablesize;
}
