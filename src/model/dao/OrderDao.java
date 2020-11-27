package model.dao;

import java.util.List;

import model.entities.ClientOrder;

public interface OrderDao {
	
	void insert(ClientOrder obj);
	void update(ClientOrder obj);
	void deleteById(Integer id);
	ClientOrder findById(Integer id);
	List<ClientOrder> findAll();
	Double OrderSum(int Id);
	Integer OrderIdClient(String login);
	void deleteByPedido(int Id);
}
