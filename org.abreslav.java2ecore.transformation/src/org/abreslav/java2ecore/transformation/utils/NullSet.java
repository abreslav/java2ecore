package org.abreslav.java2ecore.transformation.utils;

import java.util.AbstractSet;
import java.util.Iterator;

public class NullSet<T> extends AbstractSet<T> {
	@Override
	public Iterator<T> iterator() {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean add(T e) {
		return false;
	}
}