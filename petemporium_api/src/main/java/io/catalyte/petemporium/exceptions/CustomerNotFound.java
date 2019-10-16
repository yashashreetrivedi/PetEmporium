package io.catalyte.petemporium.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The Customer with the given ID could not be found.")
public class CustomerNotFound   extends RuntimeException{

	
}
