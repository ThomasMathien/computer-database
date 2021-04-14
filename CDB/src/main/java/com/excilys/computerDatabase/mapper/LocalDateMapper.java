package com.excilys.computerDatabase.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class LocalDateMapper {
	
	public Optional<LocalDate> parseToLocalDate(String date) {
		try {
			if (date != null) {
				return Optional.of(LocalDate.parse(date));
			}
			return Optional.empty();
		} catch (DateTimeParseException e) {
			return Optional.empty();
		}
	}
	
}
