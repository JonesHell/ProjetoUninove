package model.dao;

import java.util.List;

import model.entities.User;

public interface UserDao {
	
	void insert(User obj);
	void update(User obj);
	void deleteById(Integer id);
	User findById(Integer id);
	boolean findByName(String name);
	List<User> findAll();
	User findByNameUser(String login);
}
