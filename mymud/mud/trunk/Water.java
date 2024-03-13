package mud.trunk;

import mud.supporting.IntPos;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.FileInputStream;


public class Water implements Serializable
{

	/*
		Creates water map for given file
		and fills in "contact map"
	*/
	public Water(String filename, Stage stg, MudFloor cfloor)
	{
		gameStage = stg;
		mudFloorCopy = cfloor.mudMap;
		//waterMap.setStats(100, 100, -1, 127);
		waterMap.FillDataMap(filename, stg, new Color (100,100,255,255));
		popContactMap();
	}

	/*
		Populates the "contactMap", a list of water pixels that
		have contact with the air, if this is true of a pixel
		it is recorded where it has contact and it also noted
		if it is in contact with any other water pixels and
		where this contact occurs also
	*/
	public void popContactMap()
	{
		aContact temSto;
		IntPos cpos = new IntPos();
		IntPos dpos = new IntPos();
		airContact = new LinkedList();
		boolean hasAirContact = false;
		int gameX = 0;
		int gameY = 0;
		int x = 0;
		int y = 0;

		gameX = gameStage.getGameValue().mapSizeX - 1;
		gameY = gameStage.getGameValue().mapSizeY - 1;
		y = gameY;

		while (y > 0)
		{
			x = 0;
			while ( x < gameX)
			{
				hasAirContact = false;
				cpos = new IntPos();
				dpos = new IntPos();

				cpos.set(x, y);

				if (isWater(cpos))
				{
					if (y < gameY)
					{
						dpos.set(x, y + 1);

						if ((!isWater(dpos)) && mudFloorCopy.valueAt(dpos) == mudFloorCopy.white[0])
						{
							hasAirContact = true;
						}
					}

					if (y > 0)
					{
						dpos.set(x, y - 1);

						if ((!isWater(dpos)) && mudFloorCopy.valueAt(dpos) == mudFloorCopy.white[0])
						{
							hasAirContact = true;
						}
					}

					if (x < gameX)
					{
						dpos.set(x + 1, y);

						if ((!isWater(dpos)) && mudFloorCopy.valueAt(dpos) == mudFloorCopy.white[0])
						{
							hasAirContact = true;
						}
					}

					if (x > 0)
					{
						dpos.set(x - 1, y);

						if ((!isWater(dpos)) && mudFloorCopy.valueAt(dpos) == mudFloorCopy.white[0])
						{
							hasAirContact = true;
						}
					}

					if (x < gameX && y < gameY)
					{
						dpos.set(x + 1, y + 1);

						if ((!isWater(dpos)) && mudFloorCopy.valueAt(dpos) == mudFloorCopy.white[0])
						{
							hasAirContact = true;
						}
					}

					if (x > 0 && y < gameY)
					{
						dpos.set(x - 1, y + 1);

						if ((!isWater(dpos)) && mudFloorCopy.valueAt(dpos) == mudFloorCopy.white[0])
						{
							hasAirContact = true;
						}
					}

					if (hasAirContact == true)
					{
						airContact.addLast(cpos);
					}
				}

				x++;
			}

			y--;
		}

	}


	/*
		determines whether a pixel is in the list of
		pixels in contact with the air if the pixel is
		already in the list it modifies the list to include
		the extra information about the pixel
	*//*
	boolean isInContactList(aContact mapOb, char conpic)
	{
		aContact listOb;
		Object listObject;
		ListIterator iterator;
		boolean isFound = false;
		IntPos qpix = null;

		qpix.setX(mapOb.pxpos.getX());
		qpix.setY(mapOb.pxpos.getY());

		iterator = airContact.listIterator();

		while (iterator.hasNext())
		{
			listObject = iterator.next();
			listOb = (aContact)listObject;

			if (qpix.getX() == listOb.pxpos.getX() && qpix.getY() == listOb.pxpos.getY())
			{
				isFound = true;

				if (conpic == 'u')
				{
					//mapOb.u = 2;
					mapOb.d = listOb.d;
					mapOb.l = listOb.l;
					mapOb.r = listOb.r;
					iterator.set(mapOb);
				}

				if (conpic == 'd')
				{
					mapOb.u = listOb.u;
					//mapOb.d = 2;
					mapOb.l = listOb.l;
					mapOb.r = listOb.r;
					iterator.set(mapOb);
				}

				if (conpic == 'l')
				{
					mapOb.u = listOb.u;
					mapOb.d = listOb.d;
					//mapOb.l = 2;
					mapOb.r = listOb.r;
					iterator.set(mapOb);
				}

				if (conpic == 'r')
				{
					mapOb.u = listOb.u;
					mapOb.d = listOb.d;
					mapOb.l = listOb.l;
					//mapOb.r = 2;
					iterator.set(mapOb);
				}

				if (conpic == 't')
				{
				}
			}

		}

		return isFound;
	}*/

	/*
		Returns true or false for water at a given position
	*/
	public boolean isWater(IntPos x)
	{
		if(waterMap.valueAt(x) == 1)
		{
			return false;
		}else
		{
			return true;
		}
	}
	/*
		Returns the water
	*/
	public Image getImage()
	{
		Image mwater = waterMap.returnImage();
		return mwater;
	}

	/*
		Determines whether or not a pixel is able to move
	*/
	boolean canMove(IntPos pixelPos)
	{
		int x = 0;
		int y = 0;
		int gameX = 0;
		int gameY = 0;
		boolean cmove = false;
		IntPos tpos = new IntPos();

		gameX = gameStage.getGameValue().mapSizeX;
		gameY = gameStage.getGameValue().mapSizeY;
		x = pixelPos.getX();
		y = pixelPos.getX();

		if (x < gameX - 1)
		{
			tpos.set(x + 1, y);

			if ((!isWater(tpos)) && mudFloorCopy.valueAt(tpos) == mudFloorCopy.white[0])
			{
				cmove = true;
			}


		}

		if (x < 0)
		{
			tpos.set(x - 1, y);

			if ((!isWater(tpos)) && mudFloorCopy.valueAt(tpos) == mudFloorCopy.white[0])
			{
				cmove = true;
			}
		}

		if (y < gameY - 1)
		{
			tpos.set(x, y + 1);

			if ((!isWater(tpos)) && mudFloorCopy.valueAt(tpos) == mudFloorCopy.white[0])
			{
				cmove = true;
			}
		}

		if (y > 0)
		{
			tpos.set(x, y - 1);

			if ((!isWater(tpos)) && mudFloorCopy.valueAt(tpos) == mudFloorCopy.white[0])
			{
				//cmove = true;
			}
		}

		return true;

	}

	IntPos getNextPixel(IntPos currentPix)
	{
		ListIterator watair = airContact.listIterator();
		Object listObject;
		int x = 0;
		int y = 0;
		int gameX = 0;
		int gameY = 0;
		int eqCord = 0;
		int mEx = 0;
		IntPos listPix = new IntPos();
		IntPos potNext = new IntPos();
		IntPos tpix = new IntPos();

		gameX = gameStage.getGameValue().mapSizeX;
		gameY = gameStage.getGameValue().mapSizeY;

		eqCord = gameX - 1;
		mEx = gameX - 1;
		//mEx = 0;

		//System.err.println("Current Pixel" + currentPix);

		while (watair.hasNext())
		{
			listPix = new IntPos();
			listObject = watair.next();
			listPix = (IntPos)listObject;

			if (listPix.getY() < currentPix.getY())
			{
				if (listPix.getY() > potNext.getY())
				{
					potNext.set(listPix.getX(), listPix.getY());
				}else if (listPix.getY() == potNext.getY())
				{
					if (listPix.getX() < mEx && listPix.getX() > currentPix.getX())
					{
						if (listPix.getX() > potNext.getX())
						{
						}else
						{
							mEx = listPix.getX();
							potNext.set(listPix.getX(), listPix.getY());
						}
					}
				}
			}else if (listPix.getY() == currentPix.getY())
			{
				if (listPix.getX() > currentPix.getX())
				{
					if (listPix.getX() < eqCord)
					{
						eqCord = listPix.getX();
						potNext.set(listPix.getX(), listPix.getY());
					}
				}
			}

			//lets find out what the mother does
			//System.err.println("ListPixel " + listPix + " PotNext " + potNext + " mEx " + mEx + " eqCord " + eqCord);

			//end of prints

		}
		//System.err.println("End Of Cycle");

		//System.err.println(potNext);
		return potNext;
	}

	public boolean hasPixelMoved(int px, int py, LinkedList moved_list)
	{
		ListIterator mlist = moved_list.listIterator();
		IntPos thepix = new IntPos();
		Object listObject;
		IntPos listPixel = new IntPos();
		boolean hmo = false;
		thepix.set(px, py);

		while (mlist.hasNext())
		{
			listObject = mlist.next();
			listPixel = (IntPos)listObject;

			if (listPixel.getX() == thepix.getX() && listPixel.getY() == thepix.getY())
			{
				hmo = true;
			}
		}

		return hmo;
	}

	public IntPos getTopPixel(int heightY, int heightX)
	{
		IntPos topPix = new IntPos();
		IntPos travPix = new IntPos();
		IntPos movedPix = new IntPos();
		int gameY = 0;
		boolean topFound = false;

		gameY = gameStage.getGameValue().mapSizeY;
		travPix.set(heightX, heightY);

		while(topFound == false)
		{
			if (isWater(travPix) == true)
			{
				movedPix = new IntPos();
				movedPix.set(travPix.getX(), travPix.getY());
				movedPixels.put(movedPix, movedPix);
				moPix.addElement(movedPix);
				//System.err.println("TravPix " + travPix);

				if (heightY > 0)
				{
					heightY--;
					travPix.set(heightX, heightY);
				}else
				{
					topFound = true;
					topPix.set(travPix.getX(), travPix.getY());
				}

			}else
			{
				topFound = true;

				if (heightY < gameY - 1)
				{
					heightY++;
					travPix.set(heightX, heightY);
					topPix.set(travPix.getX(), travPix.getY());
				}
			}
		}

		return topPix;
	}


	int getMoveType(IntPos movePixel)
	{
		IntPos testPix = new IntPos();
		boolean down = false;
		boolean up = false;
		boolean left = false;
		boolean right = false;
		boolean diagRight = false;
		boolean diagLeft = false;
		int moveType = 0;
		int gameX = 0;
		int gameY = 0;
		int x = 0;
		int y = 0;

		gameX = gameStage.getGameValue().mapSizeX;
		gameY = gameStage.getGameValue().mapSizeY;
		//Thoughts
		/*
		ListIterator thought = moPix.listIterator();
		Object tob;
		Object tsob;
		boolean hm = false;
		IntPos tin = new IntPos();

		tsob = movePixel;

		while (thought.hasNext())
		{
			tob = thought.next();
			tin = (IntPos)tob;

			if (movePixel.getX() == tin.getX() && movePixel.getY() == tin.getY())
			{
				hm = true;
			}

			//System.err.println("WHATSIT " + tin);
		}*/

		//end of thoughts
		/*
		if (moPix.contains(movePixel))
		{
			System.err.print("jaxk");
		}*/

		if (moPix.cont(movePixel))
		{
			moveType = 0;
			//System.err.println("MovePixel POOP " + movePixel);
		}else
		{
			x = movePixel.getX();
			y = movePixel.getY();

			if (y < gameY - 1)
			{
				testPix = new IntPos();
				testPix.set(movePixel.getX(), movePixel.getY() + 1);

				if ((!isWater(testPix)) && mudFloorCopy.valueAt(testPix) == mudFloorCopy.white[0])
				{
					down = true;
				}
			}

			if (y > 0)
			{
				testPix = new IntPos();
				testPix.set(movePixel.getX(), movePixel.getY() - 1);

				if ((!isWater(testPix)) && mudFloorCopy.valueAt(testPix) == mudFloorCopy.white[0])
				{
					up = true;
				}
			}

			if (x < gameX - 1 && y < gameY - 1)
			{
				testPix = new IntPos();
				testPix.set(movePixel.getX() + 1, movePixel.getY() + 1);

				if ((!isWater(testPix)) && mudFloorCopy.valueAt(testPix) == mudFloorCopy.white[0])
				{
					diagRight = true;
				}

			}

			if (x < gameX - 1)
			{
				testPix.set(movePixel.getX() + 1, movePixel.getY());

				if ((!isWater(testPix)) && mudFloorCopy.valueAt(testPix) == mudFloorCopy.white[0])
				{
					right = true;
				}
			}

			if (x > 0 && y < gameY - 1)
			{
				testPix = new IntPos();
				testPix.set(movePixel.getX() - 1, movePixel.getY() + 1);

				if ((!isWater(testPix)) && mudFloorCopy.valueAt(testPix) == mudFloorCopy.white[0])
				{
					diagLeft = true;
				}
			}

			if (x > 0)
			{
				testPix = new IntPos();
				testPix.set(movePixel.getX() - 1, movePixel.getY());

				if ((!isWater(testPix)) && mudFloorCopy.valueAt(testPix) == mudFloorCopy.white[0])
				{
					left = true;
				}
			}

		}

		if (down == true)
		{
			moveType = 1;
		}else if (diagRight == true)
		{
			moveType = 2;
		}else if (diagLeft == true)
		{
			moveType = 3;
		}else if (right == true)
		{
			moveType = 4;
		}else if (left == true)
		{
			moveType = 5;
		}else if (up == true)
		{
			moveType = 0;
		}else
		{
			moveType = 0;
		}

		//System.err.println(moveType);

		return moveType;
	}

	public void updateContact()
	{
		ListIterator added_list;
		ListIterator removed_list;
		ListIterator water_air;
		Object listObject;
		Object addedObject;
		Object removeObject;
		IntPos rmpix = new IntPos();
		IntPos testpix = new IntPos();
		IntPos wtpix = new IntPos();
		IntPos lstpix = new IntPos();
		IntPos addpix = new IntPos();
		boolean ncontact = false;
		boolean found = false;
		int gameX = 0;
		int gameY = 0;
		int ax = 0;
		int ay = 0;

		gameX = gameStage.getGameValue().mapSizeX;
		gameY = gameStage.getGameValue().mapSizeY;
		removed_list = removedPixels.listIterator();

		while (removed_list.hasNext())
		{
			rmpix = new IntPos();
			testpix = new IntPos();
			wtpix = new IntPos();
			ax = 0;
			ay = 0;
			ncontact = false;
			removeObject = removed_list.next();
			rmpix = (IntPos)removeObject;
			water_air = airContact.listIterator();

			while (water_air.hasNext())
			{
				lstpix = new IntPos();
				listObject = water_air.next();
				lstpix = (IntPos)listObject;

				if (rmpix.getX() == lstpix.getX() && rmpix.getY() == lstpix.getY())
				{
					water_air.remove();
				}
			}

			if (rmpix.getX() < gameX - 1)
			{
				wtpix.set(rmpix.getX() + 1, rmpix.getY());
				ax = wtpix.getX();
				ay = wtpix.getY();

				if (isWater(wtpix))
				{
					ncontact = true;

					if (ncontact == true)
					{
						addedPixels.addLast(wtpix);
					}
				}
			}

			ncontact = false;
			testpix = new IntPos();
			wtpix = new IntPos();

			if (rmpix.getX() > 0)
			{
				wtpix.set(rmpix.getX() - 1, rmpix.getY());
				ax = wtpix.getX();
				ay = wtpix.getY();

				if(isWater(wtpix))
				{
					ncontact = true;

					if (ncontact == true)
					{
						addedPixels.addLast(wtpix);
					}
				}
			}

			ncontact = false;
			testpix = new IntPos();
			wtpix = new IntPos();

			if (rmpix.getY() < gameY - 1)
			{
				wtpix.set(rmpix.getX(), rmpix.getY() + 1);
				ax = wtpix.getX();
				ay = wtpix.getY();

				if (isWater(wtpix))
				{
					ncontact = true;

					if (ncontact == true)
					{
						addedPixels.addLast(wtpix);
					}
				}
			}

			ncontact = false;
			testpix = new IntPos();
			wtpix = new IntPos();

			if (rmpix.getY() > 0)
			{
				wtpix.set(rmpix.getX(), rmpix.getY() - 1);
				ax = wtpix.getX();
				ay = wtpix.getY();

				if (isWater(wtpix))
				{
					ncontact = true;

					if (ncontact == true)
					{
						addedPixels.addLast(wtpix);
					}
				}

			}

			ncontact = false;

		}


		added_list = addedPixels.listIterator();

		while (added_list.hasNext())
		{
			addpix = new IntPos();
			addedObject = added_list.next();
			addpix = (IntPos)addedObject;
			water_air = airContact.listIterator();
			found = false;

			while (water_air.hasNext())
			{
				listObject = water_air.next();
				lstpix = (IntPos)listObject;

				if (lstpix.getX() == addpix.getX() && lstpix.getY() == addpix.getY())
				{
					found = true;
				}
			}

			if (found == false)
			{
				airContact.addLast(addpix);
			}

		}
	}

	public void doMove(int moveType, IntPos mPix)
	{
		int x = 0;
		int y = 0;
		IntPos testPix = new IntPos();
		IntPos topPixel = new IntPos();

		if (moveType == 1)
		{
			//down
			testPix.set(mPix.getX(), mPix.getY() + 1);
			topPixel = getTopPixel(mPix.getY(), mPix.getX());
			(waterMap.rdMap).setPixel(testPix.getX(), testPix.getY(), waterMap.black);
			(waterMap.rdMap).setPixel(topPixel.getX(), topPixel.getY(), waterMap.white);
			addedPixels.addLast(testPix);
			removedPixels.addLast(topPixel);
		}else if (moveType == 2)
		{
			//diagRight
			testPix.set(mPix.getX() + 1, mPix.getY() + 1);
			topPixel = getTopPixel(mPix.getY(), mPix.getX());
			(waterMap.rdMap).setPixel(testPix.getX(), testPix.getY(), waterMap.black);
			(waterMap.rdMap).setPixel(topPixel.getX(), topPixel.getY(), waterMap.white);
			addedPixels.addLast(testPix);
			removedPixels.addLast(topPixel);
		}else if (moveType == 3)
		{
			//diagLeft
			testPix.set(mPix.getX() - 1, mPix.getY() + 1);
			topPixel = getTopPixel(mPix.getY(), mPix.getX());
			(waterMap.rdMap).setPixel(testPix.getX(), testPix.getY(), waterMap.black);
			(waterMap.rdMap).setPixel(topPixel.getX(), topPixel.getY(), waterMap.white);
			addedPixels.addLast(testPix);
			removedPixels.addLast(topPixel);
		}else if (moveType == 4)
		{
			//Right
			testPix.set(mPix.getX() + 1, mPix.getY());
			topPixel = getTopPixel(mPix.getY(), mPix.getX());
			(waterMap.rdMap).setPixel(testPix.getX(), testPix.getY(), waterMap.black);
			(waterMap.rdMap).setPixel(topPixel.getX(), topPixel.getY(), waterMap.white);
			addedPixels.addLast(testPix);
			removedPixels.addLast(topPixel);
		}else if (moveType == 5)
		{
			//Left
			testPix.set(mPix.getX() - 1, mPix.getY());
			topPixel = getTopPixel(mPix.getY(), mPix.getX());
			(waterMap.rdMap).setPixel(testPix.getX(), testPix.getY(), waterMap.black);
			(waterMap.rdMap).setPixel(topPixel.getX(), topPixel.getY(), waterMap.white);
			addedPixels.addLast(testPix);
			removedPixels.addLast(topPixel);
		}
	}

	public void doPhysics()
	{
		ListIterator waterAir;
		addedPixels = new LinkedList();
		removedPixels = new LinkedList();
		moPix = new aContact(1000);
		Object listObject;
		IntPos listPixel;
		int mType = 0;
		int tabsi = 0;

		//AltVars + Decls
		IntPos prepix = new IntPos();
		int count = 0;
		int lsize = 0;
		int px = 0;
		int py = 0;
		int gameX = 0;
		int gameY = 0;
		gameX = gameStage.getGameValue().mapSizeX;
		gameY = gameStage.getGameValue().mapSizeY;
		lsize = airContact.size();
		px = 0;
		py = gameY;

		tabsi = ((gameX/4) * (gameY/4));
		movedPixels = new Hashtable(tabsi);

		Object dummyOb;
		//end of AV + D

		waterAir = airContact.listIterator();
		//System.err.println("frame");

		while (count < lsize)
		{
			//dummyOb = waterAir.next();
			listPixel = new IntPos();
			prepix = new IntPos();
			prepix.set(px, py);
			listPixel = getNextPixel(prepix);
			//System.err.println("PP " + prepix);
			//System.err.println("NP " + listPixel);
			mType = getMoveType(listPixel);
			doMove(mType, listPixel);
			px = listPixel.getX();
			py = listPixel.getY();
			count++;
		}

		/*
		while (waterAir.hasNext())
		{
			listPixel = new IntPos();
			listObject = waterAir.next();
			listPixel = (IntPos)listObject;
			mType = getMoveType(listPixel);
			//System.err.println(mType);
			doMove(mType, listPixel);
		}*/

		updateContact();
		movedPixels.clear();
	}

	/*
		Adds a pixel of water at the given position
	*/
	public void addWater(IntPos p)
	{
		//aContact newWater = new aContact();
		//aContact conWater = new aContact();
		int x = 0;
		int y = 0;
		int gameX = 0;
		int gameY = 0;
		boolean aCon = false;
		boolean uu = false;
		boolean dd = false;
		boolean ll = false;
		boolean rr = false;
		IntPos qPos;

		gameX = gameStage.getGameValue().mapSizeX;
		gameY = gameStage.getGameValue().mapSizeY;
		x = p.getX();
		y = p.getY();
		//newWater.pxpos = p;
		//conWater.pxpos = p;

		(waterMap.rdMap).setPixel(p.getX(), p.getY(), waterMap.black);
		/*
		if (x < gameX)
		{
			qPos = new IntPos();
			qPos.set(x + 1, y);

			if (!isWater(qPos) && mudFloorCopy.valueAt(qPos) == 1)
			{
				aCon = true;
				//newWater.r = 0;
			}else if(isWater(qPos))
			{
				//pixel may need updating
				rr = true;
				//newWater.r = 2;
				//conWater.l = 2;
				if(isInContactList(conWater, 'l'))
				{
				}

			}

		}

		if (x > 0)
		{
			qPos = new IntPos();
			qPos.set(x - 1, y);

			if (!isWater(qPos) && mudFloorCopy.valueAt(qPos) == 1)
			{
				aCon = true;
				//newWater.l = 0;
			}else if(isWater(qPos))
			{
				//pixel may need updating
				ll = true;
				//newWater.l = 2;
				//conWater.r = 2;
				if(isInContactList(conWater, 'r'))
				{
				}
			}
		}

		if (y < gameY)
		{
			qPos = new IntPos();
			qPos.set(x, y + 1);

			if (!isWater(qPos) && mudFloorCopy.valueAt(qPos) == 1)
			{
				aCon = true;
				//newWater.d = 0;
			}else if (isWater(qPos))
			{
				//pixel may need updating
				dd = true;
				//newWater.d = 2;
				//conWater.l = 2;
				if(isInContactList(conWater, 'u'))
				{
				}
			}
		}

		if (y > 0)
		{
			qPos = new IntPos();
			qPos.set(x, y - 1);

			if (!isWater(qPos) && mudFloorCopy.valueAt(qPos) == 1)
			{
				aCon = true;
				//newWater.u = 0;
			}else if (isWater(qPos))
			{
				//pixel may need updating
				uu = true;
				//newWater.u = 2;
				//conWater.d = 2;
				if(isInContactList(conWater, 'd'))
				{
				}
			}
		}

		if (aCon == true)
		{
			if (!isInContactList(newWater, 't'))
			{
				airContact.addLast(newWater);
			}
		}*/
	}


	/*
		Returns water depth at position x
	*/
	public int waterDepth(IntPos x)
	{
	    int wdepth = 0;
		IntPos wd = null;
		boolean cont = true;

		while(cont)
		{
		  	if (waterMap.valueAt(x) == 0)
		   	{
				wd.set(x.getX(), (x.getY()) - 1);
				wdepth++;
			}else
			{
				cont = false;
			}
		}


	    return wdepth;
	}

	private Stage gameStage;
	private DataMap mudFloorCopy = new DataMap();
	DataMap waterMap = new DataMap();
	public LinkedList airContact;
	private LinkedList removedPixels;
	private LinkedList addedPixels;
	private Hashtable movedPixels;
	private aContact moPix;
	//private aContact bob = new aContact();

}
