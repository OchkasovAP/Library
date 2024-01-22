package ru.ochkasovap.Library.util;

import org.springframework.core.convert.converter.Converter;

public class StringSortTypeConverter implements Converter<String, SortType>{

	@Override
	public SortType convert(String source) {
		try {
			return SortType.valueOf(source.toUpperCase());
		} catch (IllegalArgumentException ex) {
			return SortType.ID;
		}
	}

}
