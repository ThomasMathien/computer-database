package com.excilys.computerDatabase.validator;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.excilys.computerDatabase.dto.ComputerToDatabaseDTO;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.mapper.LocalDateMapper;
import com.excilys.computerDatabase.service.CompanyService;

@Component
public class ComputerValidator implements Validator {

	ComputerMapper computerMapper;
	LocalDateMapper localDateMapper;
	CompanyService companyService;
	
	public ComputerValidator(@Lazy ComputerMapper computerMapper, LocalDateMapper localDateMapper, @Lazy CompanyService companyService) {
		this.computerMapper = computerMapper;
		this.localDateMapper = localDateMapper;
		this.companyService = companyService;
	}

	@Override
	public void validate(Object target, Errors e) {
		ComputerToDatabaseDTO dto = (ComputerToDatabaseDTO) target;
		validateName(dto.getName(), e);
		validateId(dto.getId(), "id", e);
		validateDate(dto.getIntroduced(), "introduced", e);
		validateDate(dto.getDiscontinued(), "discontinued", e);
		Optional<LocalDate> introduced = localDateMapper.parseToLocalDate(dto.getIntroduced());
		Optional<LocalDate> discontinued = localDateMapper.parseToLocalDate(dto.getDiscontinued());
		validateDateInterval(introduced, discontinued, e);
		validateCompanyId(dto.getCompanyId(), e);
	}

	
	private void validateId(String id, String field,  Errors e) {
		ValidationUtils.rejectIfEmpty(e, "id", "field.id.empty");
		try {
			long parsedId = Long.parseLong(id);
			if (parsedId < 0) {
				e.rejectValue("id", "field." + field + ".negative");
			}
		} catch (NumberFormatException numberFormatException) {
			e.rejectValue("id", "field." + field + ".notANumber");
		}

	}
	
	private void validateName(String name, Errors e) {
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "name", "field.name.blank");
		if (name.length() > 255) {
			e.rejectValue("name", "field.name.maxSize");
		}
	}
	
	private void validateDateInterval(Optional<LocalDate> introduced, Optional<LocalDate> discontinued, Errors e) {
		if ((introduced.isPresent()) && (discontinued.isPresent()) && discontinued.get().isBefore(introduced.get())) {
			e.rejectValue("discontinued", "field.discontinued.NotAfterIntroduced");
		}
	}
	
	private void validateCompanyId(String id, Errors e) {
		validateId(id, "companyId", e);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return ComputerToDatabaseDTO.class.isAssignableFrom(clazz);
	}
	
	private Optional<LocalDate> validateDate(String date, String field, Errors e) {
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
