package model.dao;

import db.DB;
import model.dao.impl.AdminDaoJDBC;
import model.dao.impl.OrderDaoJDBC;
import model.dao.impl.ProductDaoJDBC;
import model.dao.impl.TypeDaoJDBC;
import model.dao.impl.UserDaoJDBC;

public class DaoFactory {
	
	//Classe para instanciar os Daos.

	public static ProductDao createProductDao() {
		return new ProductDaoJDBC(DB.getConnection());
	}
	
	public static UserDao createUserDao() {
		return new UserDaoJDBC(DB.getConnection());
	}
	
	public static AdminDao createAdminDao() {
		return new AdminDaoJDBC(DB.getConnection());
	}
	
	public static TypeDao createTypeDao() {
		return new TypeDaoJDBC(DB.getConnection());
	}
	
	public static OrderDao createOrderDao() {
		return new OrderDaoJDBC(DB.getConnection());
	}
	
	
	
}
