package com.expensetracker.jwtsecurity;

public class JwtConstants {
	
	private JwtConstants() {
		super();
	}
	
	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 12*60*60l;
	public static final String SECRET_KEY = "<your-256-bit-secret-your-256-bit-secret>";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";
}