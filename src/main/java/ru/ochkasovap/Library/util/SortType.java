package ru.ochkasovap.Library.util;

import org.springframework.stereotype.Component;

public enum SortType {
	ID("Порядок добавления","id"), YEAR("Год","year"),
	AUTHOR("Автор","author.name"), NAME("Название","name");
	
	private String rusName;
	private String fieldName;
	
	private SortType(String rusName, String fieldName) {
		this.rusName = rusName;
		this.fieldName = fieldName;
	}
	public String fieldName() {
		return fieldName;
	}
	public String translate() {
		return rusName;
	}
}
