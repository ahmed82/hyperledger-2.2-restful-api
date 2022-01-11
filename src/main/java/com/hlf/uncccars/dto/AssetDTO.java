package com.hlf.uncccars.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class AssetDTO {

	@JsonProperty("ID")
	private String id;

	@JsonProperty("Color")
	private String color;

	@JsonProperty("Size")
	private String size;

	@JsonProperty("Owner")
	private String owner;

	@JsonProperty("AppraisedValue")
	private String appraisedValue;

	public AssetDTO() {
	}

	public AssetDTO(String id, String color, String size, String owner, String price) {
		super();
		this.id = id;
		this.color = color;
		this.size = size;
		this.owner = owner;
		this.appraisedValue = price;
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


	public String getAppraisedValue() {
		return appraisedValue;
	}

	public void setAppraisedValue(String appraisedValue) {
		this.appraisedValue = appraisedValue;
	}

	@Override
	public String toString() {
		return "Car [id=" + id + ", color=" + color + ", size=" + size + ", owner=" + owner + ", price="
				+ appraisedValue + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(color, id, owner, appraisedValue, size);
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
				&& Objects.equals(appraisedValue, other.appraisedValue) && size == other.size;
	}

}
