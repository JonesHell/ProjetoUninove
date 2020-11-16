package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.TypeDao;
import model.entities.Type;

public class TypeService {
	
	private TypeDao dao = DaoFactory.createTypeDao();
	
	public List<Type> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Type obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}		
	}
	
	public void remove(Type obj) {
		dao.deleteById(obj.getId());
	}
}
