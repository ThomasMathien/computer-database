package com.excilys.computerDatabase.validator;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.excilys.computerDatabase.dto.EditComputerFormDTO;
import com.excilys.computerDatabase.mapper.LocalDateMapper;

@Component
public class EditComputerFormDTOValidator extends ComputerValidator {
	
	public EditComputerFormDTOValidator(LocalDateMapper localDateMapper) {
		super(localDateMapper);
	}

	@Override
	public void validate(Object target, Errors e) {
		EditComputerFormDTO dto = (EditComputerFormDTO) target;
		validateName(dto.getName(), e);
		validateId(dto.getId(), "id", e);
		validateDate(dto.getIntroduced(), "introduced", e);
		validateDate(dto.getDiscontinued(), "discontinued", e);
		Optional<LocalDate> introduced = localDateMapper.parseToLocalDate(dto.getIntroduced());
		Optional<LocalDate> discontinued = localDateMapper.parseToLocalDate(dto.getDiscontinued());
		validateDateInterval(introduced, discontinued, e);
		validateCompanyId(dto.getCompanyId(), e);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return EditComputerFormDTO.class.isAssignableFrom(clazz);
	}
}
