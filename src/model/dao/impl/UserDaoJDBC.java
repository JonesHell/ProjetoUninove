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
import model.dao.UserDao;
import model.entities.User;

public class UserDaoJDBC implements UserDao{
	
	//Implementação da interface com os comandos JDBC.
	
	private Connection conn;
	
	public UserDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public User findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM cliente WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
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
	public User findByNameUser(String login) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM cliente WHERE Id = ?");
			st.setString(1, login);
			
			rs = st.executeQuery();
			
			if (rs.next()) {
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
	public boolean findByName(String name) {
		PreparedStatement st = null;
		ResultSet rs = null;
		boolean check = false;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM cliente WHERE name_user = ?");
			st.setString(1, name);
			rs = st.executeQuery();
			
			if (rs.next()) {
				check = true;
				
			}
			return check;
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
	public List<User> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM cliente ORDER BY Id");
			rs = st.executeQuery();

			List<User> list = new ArrayList<>();

			while (rs.next()) {
				User obj = new User();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("name"));
				obj.setName_user(rs.getString("name_user"));
				obj.setSenha(rs.getString("senha"));
				obj.setEmail(rs.getString("email"));
				obj.setRua(rs.getString("rua"));
				obj.setNumero(rs.getInt("numero"));
				obj.setComplemento(rs.getString("complemento"));
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
	public void insert(User obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO cliente " +
				"(name, name_user, senha, email, rua, numero, complemento)" +
				"VALUES " +
				"(?, ?, ?, ?, ?, ?, ?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getName_user());
			st.setString(3, obj.getSenha());
			st.setString(4, obj.getEmail());
			st.setString(5, obj.getRua());
			st.setInt(6, obj.getNumero());
			st.setString(7, obj.getComplemento());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
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
	public void update(User obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE cliente SET name = ? , name_user = ? , senha = ? "
				+ ", email = ? , rua = ?, numero = ?, complemento = ? WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setString(2, obj.getName_user());
			st.setString(3, obj.getSenha());
			st.setString(4, obj.getEmail());
			st.setString(5, obj.getRua());
			st.setInt(6, obj.getNumero());
			st.setString(7, obj.getComplemento());
			st.setInt(8, obj.getId());

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
				"DELETE FROM cliente WHERE Id = ?");

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
	
}
