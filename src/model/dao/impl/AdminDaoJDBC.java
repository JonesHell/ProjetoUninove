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
import model.dao.AdminDao;
import model.entities.Admin;

public class AdminDaoJDBC implements AdminDao{
	
	//Implementação da interface com os comandos JDBC.
	
		private Connection conn;
		
		public AdminDaoJDBC(Connection conn) {
			this.conn = conn;
		}
		
		@Override
		public Admin findById(Integer id) {
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				st = conn.prepareStatement(
					"SELECT * FROM admin WHERE Id = ?");
				st.setInt(1, id);
				rs = st.executeQuery();
				if (rs.next()) {
					Admin obj = new Admin();
					obj.setId(rs.getInt("Id"));
					obj.setName(rs.getString("name"));
					obj.setSenha(rs.getString("senha"));
					obj.setName_user(rs.getString("name_user"));
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
					"SELECT * FROM admin WHERE name_user = ?");
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
		public List<Admin> findAll() {
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				st = conn.prepareStatement(
					"SELECT * FROM admin ORDER BY Id");
				rs = st.executeQuery();

				List<Admin> list = new ArrayList<>();

				while (rs.next()) {
					Admin obj = new Admin();
					obj.setId(rs.getInt("Id"));
					obj.setName(rs.getString("name"));
					obj.setSenha(rs.getString("senha"));
					obj.setName_user(rs.getString("name_user"));
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
		public void insert(Admin obj) {
			PreparedStatement st = null;
			try {
				st = conn.prepareStatement(
					"INSERT INTO admin " +
					"(name, senha, name_user)" +
					"VALUES " +
					"(?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);

				st.setString(1, obj.getName());
				st.setString(2, obj.getSenha());
				st.setString(3, obj.getName_user());
				
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
		public void update(Admin obj) {
			PreparedStatement st = null;
			try {
				st = conn.prepareStatement(
					"UPDATE admin SET name = ? , senha = ? , name_user = ? WHERE Id = ?");

				st.setString(1, obj.getName());
				st.setString(2, obj.getSenha());
				st.setString(3, obj.getName_user());
				st.setInt(4, obj.getId());

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
					"DELETE FROM admin WHERE Id = ?");

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
