package com.hlf.uncccars.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class AssetDTO {
	
	private String id;
	
	private String color;
	
	private String size;
	
	private String owner;
	
	private String price;
	

	public AssetDTO() {
	}

	public AssetDTO(String id, String color, String size, String owner, String price) {
		super();
		this.id = id;
		this.color = color;
		this.size = size;
		this.owner = owner;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
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
		AssetDTO other = (AssetDTO) obj;
		return Objects.equals(color, other.color) && id == other.id && Objects.equals(owner, other.owner)
				&& Objects.equals(price, other.price) && size == other.size;
	}
	
	
	

}
