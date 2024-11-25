package DAO;

import java.util.ArrayList;

//primary key 가 없는 애들을 위한 dao
//신경 안써됨
//dao만 implement 하면 되게 설계 해놓음

public interface NoPKDAO <E>{
    public boolean insert(E e);
    public ArrayList<E> findAll();
}
