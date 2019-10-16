package io.catalyte.petemporium.domain;

public class Address {

	private String street;
	private String city;
	private String state;
	private String zip;

	public Address() {
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		// if(validateZipCode(zip))
		// {
		this.zip = zip;
		// }
	}

	// public Boolean validateZipCode(String zipcode)
	// {
	// if(zipcode.matches("^[0-9]{5}"))
	// {
	// return true;
	// }
	// else {
	// throw new IllegalArgumentException(" Zipcode should be in formate.");
	// }
	// }
}
