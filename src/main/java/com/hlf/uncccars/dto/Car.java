package com.hlf.uncccars.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Car {
	
	private int id;
	
	private String color;
	
	private int size;
	
	private String owner;
	
	private BigDecimal price;
	

	public Car() {
	}

	public Car(int id, String color, int size, String owner, BigDecimal price) {
		super();
		this.id = id;
		this.color = color;
		this.size = size;
		this.owner = owner;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Car [id=" + id + ", color=" + color + ", size=" + size + ", owner=" + owner + ", price=" + price + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(color, id, owner, price, size);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		return Objects.equals(color, other.color) && id == other.id && Objects.equals(owner, other.owner)
				&& Objects.equals(price, other.price) && size == other.size;
	}
	
	
	

}
