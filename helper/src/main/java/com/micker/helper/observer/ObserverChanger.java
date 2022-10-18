package com.micker.helper.observer;

public interface ObserverChanger {

	 void registerObserver(Observer o, int id);

	 void removeObserver(Observer o, int id);

	 void notifyObserver(int id, Object... args);

}
