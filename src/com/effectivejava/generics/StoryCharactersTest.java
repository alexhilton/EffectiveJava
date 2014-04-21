package com.effectivejava.generics;

import java.util.Iterator;
import java.util.Random;

public class StoryCharactersTest implements Iterable<StoryCharacters>, Generator<StoryCharacters> {
	private int size;
	
	public StoryCharactersTest(int count) {
		size = count;
	}
	private Random rand = new Random(47);
	
	private Class[] type = new Class[] {
			IronMan.class, Smith.class, Gagawo.class, Neo.class
	};
	public static void main(String[] args) {
		for (StoryCharacters hero : new StoryCharactersTest(6)) {
			hero.say();
		}
	}

	@Override
	public StoryCharacters next() {
		try {
			return (StoryCharacters) type[rand.nextInt(type.length)].newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		return null;
	}

	@Override
	public Iterator<StoryCharacters> iterator() {
		return new Iterator<StoryCharacters>() {
			@Override
			public boolean hasNext() {
				return size > 0;
			}

			@Override
			public StoryCharacters next() {
				size--;
				return StoryCharactersTest.this.next();
			}

			@Override
			public void remove() {
			}
		};
	}
}

class StoryCharacters {
	private static int id = 0;
	
	private int identity;
	
	public StoryCharacters() {
		identity = ++id;
	}
	
	public void say() {
		System.out.println(this);
	}
	
	public String toString() {
		return " my security identity " + this.getClass().getSimpleName() + " #" + identity;
	}
}

class BadGuys extends StoryCharacters {
	@Override
	public String toString() {
		return " i am the evil and " + super.toString();
	}
}

class GoodGuys extends StoryCharacters {
	@Override
	public String toString() {
		return " I am here to help you! and " + super.toString();
	}
}

class IronMan extends GoodGuys {
	@Override
	public String toString() {
		return  "I am Iron man and " + super.toString();
	}
}

class Smith extends BadGuys {
	@Override
	public String toString() {
		return "I am Smith and " + super.toString();
	}
}

class Neo extends GoodGuys {
	@Override
	public String toString() {
		return "this is neo and " + super.toString();
	}
}

class Gagawo extends BadGuys {
	@Override
	public String toString() {
		return "gagawo and " + super.toString();
	}
}