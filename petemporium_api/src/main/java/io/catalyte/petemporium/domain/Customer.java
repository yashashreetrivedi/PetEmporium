package io.catalyte.petemporium.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;

@Document(collection = "customers")
public class Customer {

	private static final String Boolean = null;

	@Id
	private String _id;

	private String firstName;
	private String lastName;
	private String phoneNumber;

	private String email;
	private Address address;

	public Customer() {

	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {

		// if (validatePhoneNumber(phoneNumber)) {
		this.phoneNumber = phoneNumber;
		// }

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		// if (validateEmail(email)) {
		this.email = email;
		// }
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {

		this.address = address;

	}

	@Override
	public String toString() {
		return "Customer [_id=" + _id + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber="
				+ phoneNumber + ", email=" + email + ", address=" + address + "]";

	// public Boolean validatePhoneNumber(String phNumber) {
	// if (phNumber.matches("\\d{10}")) {
	// return true;
	// } else {
	// throw new IllegalArgumentException("Number should be in formate.");
	// }
	// }
	//
	// public Boolean validateEmail(String validEmail) {
	// if (validEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
	// return true;
	// }
	//
	// else {
	// throw new IllegalArgumentException(" Email should be in formate.");
	// }
	// }

}

}