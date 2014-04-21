package com.effectivejava.generics;

public class LinkedStack<T> {
	private class Node {
		T item;
		Node next;
		
		public Node() {
			item = null;
			next = null;
		}
		
		public Node(T item, Node top) {
			this.item = item;
			this.next = top;
		}
	}
	private Node top = new Node();
	
	public void push(T item) {
		top = new Node(item, top);
	}
	
	public T pop() {
		T t = top.item;
		if (top.next != null) {
			top = top.next;
			return t;
		}
		return null;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LinkedStack<String> lss = new LinkedStack<String>();
		
		for (String s : "Freedom is nothing but a chance to be better".split(" ")) {
			lss.push(s);
		}
		String i;
		while ((i = lss.pop()) != null) {
			System.out.println(i);
		}
	}
}