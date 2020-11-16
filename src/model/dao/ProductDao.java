package model.dao;

import java.util.List;

import model.entities.Product;

public interface ProductDao {

	//Interface responsável pelo acesso dos dados do Product.
	
	void insert(Product obj);
	void update(Product obj);
	void deleteById(Integer id);
	Product findById(Integer id);
	List<Product> findAll();
	List<Product> findByType(Integer type);
}