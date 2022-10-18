package com.micker.helper.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 通用监听者
 * <p/>
 * <p>
 * 注意事项：所有registerObserver的地方必须存在removeObserver
 * </p>
 */
public class ObserverManger implements ObserverChanger {

    final private HashMap<Integer, ArrayList<Observer>> observers = new HashMap<>();

    private static volatile ObserverManger Instance = null;
    public static ObserverManger getInstance() {
        ObserverManger localInstance = Instance;
        if (localInstance == null) {
            synchronized (ObserverManger.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new ObserverManger();
                }
            }
        }
        return localInstance;
    }

    @Override
    public void registerObserver(Observer observer, int id) {
        synchronized (observers) {
            ArrayList<Observer> objects = observers.get(id);
            if (objects == null) {
                observers.put(id, (objects = new ArrayList<Observer>()));
            }
            if (objects.contains(observer)) {
                return;
            }
            objects.add(observer);
        }
    }

    public void registerObserver(Observer observer, int...ids) {
        synchronized (observers) {
            for (int i = 0; i < ids.length; i++) {
                registerObserver(observer,ids[i]);
            }
        }
    }

    @Override
    public void removeObserver(Observer observer, int id) {
        synchronized (observers) {
            ArrayList<Observer> objects = observers.get(id);
            if (objects != null) {
                objects.remove(observer);
                if (objects.size() == 0) {
                    observers.remove(id);
                }
            }
        }
    }

    public void removeObserver(Observer observer) {
        synchronized (observers) {
            List<Integer> mObservers=new ArrayList<>();
            Iterator<Integer> iterator = observers.keySet().iterator();
            while (iterator.hasNext()) {
                Integer next = iterator.next();
                ArrayList<Observer> objects = observers.get(next);
                for (Observer obs: objects){
                    if (obs == observer) {
                        mObservers.add(next);
                    }
                }
            }
            for (Integer item: mObservers) {
                removeObserver(observer,item);
            }
        }
    }

    @Override
    public void notifyObserver(int id, Object... args) {
        try {
            synchronized (observers) {
                if(observers.get(id) != null) {
                    ArrayList<Observer> objects = new ArrayList<>(observers.get(id));
                    if (objects != null && !objects.isEmpty()) {
                        for (Observer obj : objects) {
                            obj.update(id, args);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
