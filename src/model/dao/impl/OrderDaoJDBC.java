package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.OrderDao;
import model.entities.ClientOrder;
import model.entities.Product;
import model.entities.User;

public class OrderDaoJDBC implements OrderDao{
	
	private Connection conn;
	
	public OrderDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(ClientOrder obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO pedido_produto" +
				"(Id_cliente, Id_produto, quantidade)" +
				"VALUES " +
				"(?, ?, ?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getCliente().getId());
			st.setInt(2, obj.getPd().getId());
			st.setInt(3, obj.getPd().getQtd());
		
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setid(id);
				}
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(ClientOrder obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE pedido_cliente SET id_cliente = ? , Id_produto = ? , quantidade = ? WHERE Id = ?");

			st.setInt(1, obj.getCliente().getId());
			st.setInt(2, obj.getPd().getId());
			st.setInt(3, obj.getQtd());
			st.setInt(4, obj.getid());

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM pedido_cliente WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public ClientOrder findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM pedido_cliente WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				
				User user = instantiateUser(rs);
				Product pd = instantiateProduct(rs);
				ClientOrder obj = instantiateOrder(rs, pd, user);
				
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public Double OrderSum(int Id) {
		double total = 0.0;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT produtos.Id, produtos.Name, produtos.Price, pedido_produto.Id_produto, pedido_produto.quantidade, pedido_produto.Id_cliente"
					+ " FROM produtos JOIN pedido_produto ON produtos.Id = pedido_produto.Id_produto WHERE Id_cliente = ?");
				
				st.setInt(1, Id);
				rs = st.executeQuery();
			while(rs.next()) {
					total = total + (rs.getDouble("Price") * rs.getInt("quantidade"));
			}
			return total;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public Integer OrderIdClient(String login) {
		int Id = 0;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM cliente WHERE name_user = ?");
			
			st.setString(1, login);
			rs = st.executeQuery();
			
			while(rs.next()) {
				Id = rs.getInt("Id");
						
			}
			return Id;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}	
	
	
	@Override
	public List<ClientOrder> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM pedido_cliente ORDER BY id");
			
			rs = st.executeQuery();
			
			List<ClientOrder> list = new ArrayList<>();
			
			while (rs.next()) {
				
				ClientOrder obj = new ClientOrder();
				obj.setid(rs.getInt("Id"));
				obj.setQtd(rs.getInt("quantidade"));
				
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public void deleteByPedido(int Id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
				"DELETE FROM pedido_produto WHERE Id_cliente = ?");

			st.setInt(1, Id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private User instantiateUser(ResultSet rs) throws SQLException{
		User obj = new User();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("name"));
		obj.setName_user(rs.getString("name_user"));
		obj.setSenha(rs.getString("senha"));
		obj.setEmail(rs.getString("email"));
		obj.setRua(rs.getString("rua"));
		obj.setNumero(rs.getInt("numero"));
		obj.setComplemento(rs.getString("complemento"));
		
		return obj;
	}
	
	private Product instantiateProduct(ResultSet rs) throws SQLException{
		Product obj = new Product();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("name"));
		obj.setPrice(rs.getDouble("price"));		
		
		return obj;
	}
	
	private ClientOrder instantiateOrder(ResultSet rs, Product pd, User user) throws SQLException{
		ClientOrder obj = new ClientOrder();
		obj.setid(rs.getInt("Id"));
		obj.setQtd(rs.getInt("quantidade"));
		obj.setPd(pd);
		obj.setCliente(user); 
		
		return obj;
	}
}
