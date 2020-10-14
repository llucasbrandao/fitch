package com.fitch.teste.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fitch.teste.enums.UserRoleEnum;

public class UserDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "first_name must not be empty")
	@Length(min =  2, max = 30, message = "first_name must have at least 2 characters and 30 max")
	private String first_name;
	
	@NotBlank(message = "last_name must not be empty")
	@Length(min =  2, max = 30, message = "last_name must have at least 2 characters and 30 max ")
	private String last_name;
	
	@NotBlank(message = "email must not be empty")
	@Email(message = "Invalid email address")
	private String email;
	
	@NotBlank(message = "password must not be empty")
	@Length(min =  2, max = 80, message = "password must have at least 8 characters and 80 max ")
	private String password;
	
	@NotNull(message = "birthday must not be empty")
	@DateTimeFormat(pattern = "DD/MM/YYYY")
	private Date birthday;
	
	private Set<Integer> roles = new HashSet<>();
	
	public UserDTO() {
		// Todo usuário recebe o role user, mesmo que ele também seja ADMIN
		addRole(UserRoleEnum.ROLE_USER);
	}
	
	public UserDTO(String first_name, String last_name, String email, String password, Date birthday, Set<Integer> role) {
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.password = password;
		this.birthday = birthday;
		this.roles = role;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public Set<Integer> getRole() {
		return this.roles;
	}

	public void setRole(Set<Integer> role) {
		this.roles = role;
	}
	
	public void addRole(UserRoleEnum role) {
		this.roles.add(role.getCode());
	}
}
