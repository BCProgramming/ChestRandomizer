package com.BASeCamp.SurvivalChests;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemBucket<T> {
	private ArrayList<T> _Elements;
	private ArrayList<T> _Original;
	private Random rgen = new Random();

	public ItemBucket(T... Source) {
		this(new Random(), Source);
	}

	public static <K> K[] RepeatArray(K[] tocopy, int copies, Class classtype) {
		if (copies < 0)
			throw new IllegalArgumentException("copies must be non-negative.");

		@SuppressWarnings("unchecked")
		K[] resultarray = (K[]) Array.newInstance(classtype, tocopy.length
				* copies);
		for (int copy = 1; copy < copies; copy++) {

			for (int index = 0; index < resultarray.length; index++) {
				resultarray[index] = tocopy[index % tocopy.length];
			}

		}
		return resultarray;

	}

	public ItemBucket(Random rg, T... Source) {
		rgen = rg;
		_Original = new ArrayList<T>();
		_Elements = new ArrayList<T>();
		for (T addto : Source) {
			_Original.add(addto);
			_Elements.add(addto);
		}
	}

	private T DispenseSingle() {
		// dispense a single item.
		// use the Random instance to get a random index.
		int randompos = rgen.nextInt(_Elements.size());
		// grab that element.
		T retrieved = _Elements.get(randompos);
		_Elements.remove(randompos);
		// if the Elements list is empty, 'refresh' it from the Original.
		if (_Elements.isEmpty()) {
			_Elements.addAll(_Original);
		}
		return retrieved;

	}

	public T Dispense() {
		return DispenseSingle();
	}

	public List<T> Dispense(int Count) {
		ArrayList<T> buildlist = new ArrayList<T>();
		for (int i = 0; i < Count; i++)
			buildlist.add(Dispense());

		return buildlist;
	}

}
