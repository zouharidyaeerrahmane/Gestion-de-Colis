package com.app.javaexamen.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<E,U>{
    void create(E e) throws SQLException;
    void deleteById(U u) throws SQLException;
    List<E> findAll()  throws SQLException;
    E findByID(U u) throws SQLException;
    void update(E e)throws SQLException;
}
