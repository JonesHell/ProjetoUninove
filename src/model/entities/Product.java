package model.entities;

import java.io.Serializable;

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private Double price;
	private Integer tipo;
	
	private Type type;
	private Admin adm;

	public Product() {
	}
	
	public Product(String name, Double price) {
		this.name = name;
		this.price = price;	
	}
	
	public Product(Integer id, String name, Double price, Type type, Admin adm) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.type = type;
		this.adm = adm;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Admin getAdm() {
		return adm;
	}

	public void setAdm(Admin adm) {
		this.adm = adm;
	}
	
	public void setTipo(Type type) {
		this.tipo = getType().getId();
	}
	
	public Integer getTipo() {
		return tipo;
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
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", Price=" + price + ", type=" + type + ", adm=" + adm + "]";
	}

}
