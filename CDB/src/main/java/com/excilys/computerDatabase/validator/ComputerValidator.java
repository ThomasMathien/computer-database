package com.excilys.computerDatabase.validator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import com.excilys.computerDatabase.dto.ComputerToDatabaseDTO;
import com.excilys.computerDatabase.exception.invalidValuesException.InvalidDateInterval;
import com.excilys.computerDatabase.exception.invalidValuesException.InvalidIdException;
import com.excilys.computerDatabase.exception.invalidValuesException.InvalidNameException;
import com.excilys.computerDatabase.exception.invalidValuesException.InvalidValuesException;
import com.excilys.computerDatabase.mapper.ComputerMapper;

public class ComputerValidator {
	
	private static ComputerValidator instance = null;
	private ComputerValidator() { };
	
	public static ComputerValidator getInstance() {
		if (instance == null) {
			instance = new ComputerValidator();
		}
		return instance;
	}
	
	public void validateComputerDTO(ComputerToDatabaseDTO dto) throws InvalidValuesException {
		validateName(dto.getName());
		Optional<LocalDate> introduced = ComputerMapper.getInstance().parseToLocalDate(dto.getIntroduced());
		Optional<LocalDate> discontinued = ComputerMapper.getInstance().parseToLocalDate(dto.getDiscontinued());
		validateDateInterval(introduced, discontinued);
		if (dto.getId() != null) {
			validateId(Long.parseLong(dto.getId()));
		}
		if (dto.getCompanyId() != null) {
			validateId(Long.parseLong(dto.getCompanyId()));
		}
	}

	private void validateId(long id) throws InvalidIdException {
		if (id < 0) {
			throw new InvalidIdException("Id should not be negative");
		}
	}
	
	private void validateName(String name) throws InvalidNameException {
		if (name.length() > 255) {
			throw new InvalidNameException("Name should not be more than 255 characters");
		}
		if (name.isBlank()) {
			throw new InvalidNameException("Name should not be comprised of only Spaces characters");	
		}
	}
	
	private void validateDateInterval(Optional<LocalDate> introduced, Optional<LocalDate> discontinued) throws InvalidDateInterval {
		if ((introduced.isPresent()) && (discontinued.isPresent()) && discontinued.get().isBefore(introduced.get())) {
			throw new InvalidDateInterval("Date interval should respect date precedence");
		}
	}
}
