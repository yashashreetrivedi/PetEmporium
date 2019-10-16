package io.catalyte.petemporium.domain;

public class PurchaseItem {
      private  String item_Id;
	
	private Pet pet;
	private Double price;
	public Pet getPet() {
		return pet;
	}
	public void setPet(Pet pet) {
		this.pet = pet;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}

	public String getItem_Id() {
		return item_Id;
	}
	public void setItem_Id(String item_Id) {
		this.item_Id = item_Id;
	}
}
