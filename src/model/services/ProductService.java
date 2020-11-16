package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ProductDao;
import model.entities.Product;

public class ProductService {
	
	private ProductDao dao = DaoFactory.createProductDao();
	
	public List<Product> findAll(){
		return dao.findAll();
	}
	
	public List<Product> findForType(Integer type){
		return dao.findByType(type);
	}
	
	public void saveOrUpdate(Product obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}		
	}
	
	public void remove(Product obj) {
		dao.deleteById(obj.getId());
	}
}
