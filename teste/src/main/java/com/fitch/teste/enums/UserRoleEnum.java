package com.fitch.teste.enums;

public enum UserRoleEnum {
	ROLE_ADMIN(1, "ROLE_ADMIN"),
	ROLE_USER(2, "ROLE_USER");
	
	private int code;
	private String description;
	
	private UserRoleEnum(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
	
	public static UserRoleEnum toEnum(Integer code) {
		if (code == null)
			return null;
		
		for (UserRoleEnum x : UserRoleEnum.values())
			if (code.equals(x.getCode()))
				return x;
		
		throw new IllegalArgumentException("Role inválido: " + code);
	}
}
