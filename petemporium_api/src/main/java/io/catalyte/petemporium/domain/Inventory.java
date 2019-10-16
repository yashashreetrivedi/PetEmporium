package io.catalyte.petemporium.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inventory")
public class Inventory {
    @Id
	private String _id;
  
	private Double amount;
	  private String petsId;
	
	private String type;
	private Double price;
	
	
	

	public String get_id() {
		return _id;
	}




	public void set_id(String _id) {
		this._id = _id;
	}




	public Double getAmount() {
		return amount;
	}




	public void setAmount(Double amount) {
		this.amount = amount;
	}




	public String getPetsId() {
		return petsId;
	}




	public void setPetsId(String petsId) {
		this.petsId = petsId;
	}




	public String getType() {
		return type;
	}




	public void setType(String type) {
		this.type = type;
	}




	public Double getPrice() {
		return price;
	}




	public void setPrice(Double price) {
		this.price = price;
	}




	@Override
	public String toString() {
		return "Inventory [_id=" + _id + ", petsId=" + petsId + ", amount=" + amount + ", price=" + price + ", type="
				+ type + "]";
	}



}
