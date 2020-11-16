package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.ProductDao;
import model.entities.Admin;
import model.entities.Product;
import model.entities.Type;

public class ProductDaoJDBC implements ProductDao {
	
	//Implementação da interface com os comandos JDBC.
	
	private Connection conn;
	
	public ProductDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Product obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO produtos "
					+ "(Name, Price, Id_tipo, Id_admin) "
					+ "VALUES "
					+ "(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setDouble(2, obj.getPrice());
			st.setInt(3, obj.getType().getId());
			st.setInt(4, obj.getAdm().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
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
	public void update(Product obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE produtos SET Name = ?, Price = ?, Name_tipo = ? , Id_admin = ? WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setDouble(2, obj.getPrice());
			st.setInt(3, obj.getType().getId());
			st.setInt(4, obj.getAdm().getId());
			st.setInt(5, obj.getId());
			
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
			st = conn.prepareStatement("DELETE FROM produtos WHERE Id = ?");
			
			st.setInt(1, id);
			
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
	public Product findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM produtos WHERE Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Type tp = instantiateType(rs);
				Admin adm = instantiateAdmin(rs);
				Product obj = instantiateProduct(rs, tp, adm);
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

	private Product instantiateProduct(ResultSet rs, Type tp, Admin adm) throws SQLException {
		Product obj = new Product();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("name"));
		obj.setPrice(rs.getDouble("price"));
		obj.setAdm(adm);
		obj.setType(tp);
		return obj;
	}

	private Admin instantiateAdmin(ResultSet rs) throws SQLException {
		Admin adm = new Admin();
		adm.setId(rs.getInt("Id"));
		adm.setName_user(rs.getString("Id_admin"));
		return adm;
	}
	
	private Type instantiateType(ResultSet rs) throws SQLException {
		Type tp = new Type();
		tp.setId(rs.getInt("Id"));
		tp.setName_type(rs.getString("Id_tipo"));
		return tp;
	}
	
	@Override
	public List<Product> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM produtos ORDER BY id");
			
			rs = st.executeQuery();
			
			List<Product> list = new ArrayList<>();
			
			while (rs.next()) {
				Type tp = instantiateType(rs);
				Admin adm = instantiateAdmin(rs);
				Product obj = instantiateProduct(rs, tp, adm);

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
	public List<Product> findByType(Integer type) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM produtos WHERE name_tipo = ?");
			
			st.setInt(1, type);
			rs = st.executeQuery();
			
			List<Product> list = new ArrayList<>();
			
			while (rs.next()) {
				Type tp = instantiateType(rs);
				Admin adm = instantiateAdmin(rs);
				Product obj = instantiateProduct(rs, tp, adm);		
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
}
