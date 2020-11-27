package model.entities;

import java.io.Serializable;

public class ClientOrder extends Product implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer qtd;
	
	private User cliente;
	private Product pd;
	
	public ClientOrder () {
		
	}
	
	public ClientOrder(Integer id, Integer qtd, User cliente, Product pd) {
		this.id = id;
		this.qtd = qtd;
		this.cliente = cliente;
		this.pd = pd;
	}

	public Integer getid() {
		return id;
	}

	public void setid(Integer id) {
		this.id = id;
	}

	public Integer getQtd() {
		return pd.getQtd();
	}

	public void setQtd(Integer qtd) {
		this.qtd = qtd;
	}

	public User getCliente() {
		return cliente;
	}

	public void setCliente(User cliente) {
		this.cliente = cliente;
	}

	public Product getPd() {
		return pd;
	}

	public void setPd(Product pd) {
		this.pd = pd;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientOrder other = (ClientOrder) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClientOrder [id=" + id + ", qtd=" + qtd + ", cliente=" + cliente + ", pd=" + pd + "]";
	}
	
	
}
