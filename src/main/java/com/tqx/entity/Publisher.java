package com.tqx.entity;

import java.util.Observable;

public class Publisher extends Observable{

	@Override
	public void notifyObservers(Object arg) {
		if (!hasChanged()) {
			setChanged();
		}
		super.notifyObservers(arg);
	}
	
}
