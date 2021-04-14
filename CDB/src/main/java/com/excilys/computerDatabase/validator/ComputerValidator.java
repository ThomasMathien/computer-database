package com.excilys.computerDatabase.validator;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.excilys.computerDatabase.mapper.LocalDateMapper;

@Component
abstract class ComputerValidator implements Validator {

	LocalDateMapper localDateMapper;

	
	ComputerValidator(LocalDateMapper localDateMapper) {
		this.localDateMapper = localDateMapper;
	}
	
	protected void validateId(String id, String field,  Errors e) {
		ValidationUtils.rejectIfEmpty(e, field, "field" + field + "empty");
		try {
			long parsedId = Long.parseLong(id);
			if (parsedId < 0) {
				e.rejectValue("id", "field." + field + ".negative");
			}
		} catch (NumberFormatException numberFormatException) {
			e.rejectValue("id", "field." + field + ".notANumber");
		}

	}
	
	protected void validateName(String name, Errors e) {
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "name", "field.name.blank");
		if (name.length() > 255) {
			e.rejectValue("name", "field.name.maxSize");
		}
	}
	
	protected void validateDateInterval(Optional<LocalDate> introduced, Optional<LocalDate> discontinued, Errors e) {
		if ((introduced.isPresent()) && (discontinued.isPresent()) && discontinued.get().isBefore(introduced.get())) {
			e.rejectValue("discontinued", "field.discontinued.NotAfterIntroduced");
		}
	}
	
	protected void validateCompanyId(String id, Errors e) {
		validateId(id, "companyId", e);
	}

	
	protected Optional<LocalDate> validateDate(String date, String field, Errors e) {
		Optional<LocalDate> parsedDate = Optional.empty();
		if (date != null && !date.isBlank()) {
			parsedDate = localDateMapper.parseToLocalDate(date);
			if (parsedDate.isEmpty()) {
				e.rejectValue(field, "field." + field + ".NotADate");
			}
		}
		return parsedDate;
	}
}
