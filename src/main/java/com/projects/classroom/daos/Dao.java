package com.projects.classroom.daos;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface Dao <T>{
	
    T get(long id);
    Page<T> getAll(Pageable page);
    int save(T t);
    void update(T t);
    void delete(long id);
}
