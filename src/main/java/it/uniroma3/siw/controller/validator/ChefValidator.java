package it.uniroma3.siw.controller.validator;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.service.ChefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class ChefValidator implements Validator {

	@Autowired
	private ChefService chefService;

	private static final int MAX_NAME_LENGTH = 50;
	private static final int MIN_NAME_LENGTH = 2;
	private static final int MIN_YEAR = 1940;
	private static final int MAX_YEAR = 2004;

	@Override
	public boolean supports(Class<?> clazz) {
		return Chef.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Chef chef = (Chef) target;

		String name = chef.getName().trim();
		String surname = chef.getSurname().trim();
		LocalDate dateOfBirth = chef.getData();

		// Validate name
		if (name.isEmpty()) {
			errors.rejectValue("name", "required");
		} else if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
			errors.rejectValue("name", "name.size");
		}

		// Validate surname
		if (surname.isEmpty()) {
			errors.rejectValue("surname", "required");
		} else if (surname.length() < MIN_NAME_LENGTH || surname.length() > MAX_NAME_LENGTH) {
			errors.rejectValue("surname", "name.size");
		}

		// Validate date of birth
		if (dateOfBirth == null) {
			errors.rejectValue("data", "required");
		} else if (dateOfBirth.getYear() < MIN_YEAR || dateOfBirth.getYear() > MAX_YEAR) {
			errors.rejectValue("data", "data.range");
		}

		// Validate if chef already exists
		if (this.chefService.alreadyExists(chef)) {
			errors.reject("chef.duplicato");
		}
	}
}
