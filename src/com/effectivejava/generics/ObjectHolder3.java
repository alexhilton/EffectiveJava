package com.effectivejava.generics;

public class ObjectHolder3<T> {
	private T holder1;
	private T holder2;
	private T holder3;
	
	public ObjectHolder3(T t1, T t2, T t3) {
		holder1 = t1;
		holder2 = t2;
		holder3 = t3;
	}
	
	public T getFirst() {
		return holder1;
	}
	
	public T getSecond() {
		return holder2;
	}
	
	public T getThird() {
		return holder3;
	}
	
	public static void main(String[] args) {
		ObjectHolder3<Something> h = new ObjectHolder3<Something>(new Something(), new Something(), new Something());
		h.getFirst().identify();
		h.getSecond().identify();
		h.getThird().identify();
	}
	
	private static class Something {
		private static int number = 1;
		private int identity;
		public Something() {
			identity = ++number;
		}

		public void identify() {
			System.out.println(this);
		}
		
		@Override
		public String toString() {
			return "hello, i am number " + identity;
		}
	}
}