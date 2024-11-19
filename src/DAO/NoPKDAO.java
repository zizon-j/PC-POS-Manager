package DAO;

import java.util.ArrayList;

public interface NoPKDAO <E>{
    public boolean insert(E e);
    public ArrayList<E> findAll();
}
