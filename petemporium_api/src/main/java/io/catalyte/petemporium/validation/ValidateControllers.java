package io.catalyte.petemporium.validation;

public class ValidateControllers {

	public Boolean validatePhoneNumber(String phNumber) {
		if (phNumber.matches("\\d{10}")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validateEmail(String validEmail) {
		if (validEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			return true;
		}

		else {
			return false;
		}
	}

	public boolean validateZipCode(String zipcode) {
		if (zipcode.matches("^[0-9]{5}")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validatePetId(String pId) {
		if (pId != null && pId != "") {
			return true;
		}
		return false;
	}

	public boolean validateAmount(Double amt) {
		if (amt != null && amt > 0 && amt != 0) {
			return true;
		}
		return false;
	}

	public boolean validatePet(String id, String name, String sex, String color, String age) {
		if (  name != null && name != "" && sex != null && sex != "" && color != null
				&& color != "" && age != null && age != null) {
			return true;
		}
		return false;
	}
	
}

