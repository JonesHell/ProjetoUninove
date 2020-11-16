package model.dao;

import java.util.List;

import model.entities.Type;

public interface TypeDao {
	
	void insert(Type obj);
	void update(Type obj);
	void deleteById(Integer id);
	Type findById(Integer id);
	List<Type> findAll();
}
