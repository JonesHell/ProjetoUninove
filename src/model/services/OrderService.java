package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.OrderDao;
import model.entities.ClientOrder;

public class OrderService {
	
	private OrderDao dao = DaoFactory.createOrderDao();
	
	public List<ClientOrder> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(ClientOrder obj) {
		if(obj.getid() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}		
	}
	
	public void remove(ClientOrder obj) {
		dao.deleteById(obj.getid());
	}
	
	public ClientOrder findById(Integer Id) {
		return dao.findById(Id);
	}
	
	public Double orderSum(int Id) {
		return dao.OrderSum(Id);
	}
	
	public Integer orderIdClient(String login) {
		return dao.OrderIdClient(login);
	}
	
	public void deleteByPedido(int Id) {
		dao.deleteByPedido(Id);
	}
}
