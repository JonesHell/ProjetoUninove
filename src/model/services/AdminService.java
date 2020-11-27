package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.AdminDao;
import model.entities.Admin;

public class AdminService {
	
	private AdminDao dao = DaoFactory.createAdminDao();
	
	public List<Admin> findAll(){
		return dao.findAll();
	}
	
	public List<Admin> findAllNameUser(){
		return dao.findAllNameUser();
	}
	
	public void saveOrUpdate(Admin obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}		
	}
	
	public void remove(Admin obj) {
		dao.deleteById(obj.getId());
	}
}
