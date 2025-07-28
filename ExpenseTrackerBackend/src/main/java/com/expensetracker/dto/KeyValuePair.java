package com.expensetracker.dto;

public class KeyValuePair {
	private String key;
    private String value;

    public KeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // Getters
    public String getKey() { return key; }
    public String getValue() { return value; }
}
