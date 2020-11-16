package model.dao;

import java.util.List;

import model.entities.Admin;

public interface AdminDao {
	
	//Interface responsável pelo acesso dos dados do Admin.
	
		void insert(Admin obj);
		void update(Admin obj);
		void deleteById(Integer id);
		boolean findByName(String name);
		Admin findById(Integer id);
		List<Admin> findAll();
}
