package dev.andrylat.dao;

import java.util.Collection;

public interface Dao <T>{
	
    T get(long id);
    Collection<T> getAll();
    int save(T t);
    void update(T t);
    void delete(long id);
}
