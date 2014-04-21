package com.effectivejava.generics;

import java.util.ArrayList;
import java.util.Random;

public class RandomSelectedList<T> {
	private ArrayList<T> storage;
	
	private Random ran = new Random(47);
	
	public RandomSelectedList() {
		storage = new ArrayList<T>();
	}
	
	public void add(T item) {
		storage.add(item);
	}
	
	public T select() {
		return storage.get(ran.nextInt(storage.size()));
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RandomSelectedList<String> rs = new RandomSelectedList<String>(); 
		for (String s : "The quick brown fox jumps over the lazy dog".split(" ")) {
			rs.add(s);
		}
		
		for (int i = 0; i < 11; i++) {
			System.out.println(rs.select());
		}
		
		RandomSelectedList<Integer> ri = new RandomSelectedList<Integer>();
		for (int j = 0; j < 10; j++) {
			ri.add(Integer.valueOf(((int) (Math.random() * 100f))));
		}
		
		for (int j = 0; j < 10; j++) {
			System.out.println(ri.select());
		}
	}
}