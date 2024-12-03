package DAO;

//dao들은 dao interface 상속 받음
//data acess object
public interface DAO<E, K> extends NoPKDAO<E> {
    E findById(K k);

    boolean delete(K k);

    boolean update(E e);
}
