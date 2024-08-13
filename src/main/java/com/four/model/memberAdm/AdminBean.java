package com.four.model.memberAdm;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "admin")
public class AdminBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @Column(name = "admNo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int admNo;
	private String admName;
	private String admEmail;
	private String admPassword;
	private String phone;
	@Column(name = "status", nullable = false, columnDefinition = "int default 1")
	private int status = 1;
	
	// getters and setters
	public int getAdmNo() {
		return admNo;
	}
	public void setAdmNo(int admNo) {
		this.admNo = admNo;
	}
	public String getAdmName() {
		return admName;
	}
	public void setAdmName(String admName) {
		this.admName = admName;
	}
	public String getAdmEmail() {
		return admEmail;
	}
	public void setAdmEmail(String admEmail) {
		this.admEmail = admEmail;
	}
	public String getAdmPassword() {
		return admPassword;
	}
	public void setAdmPassword(String admPassword) {
		this.admPassword = admPassword;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	// constructor
	public AdminBean(String admName, String admEmail, String admPassword, String phone, int status) {
		super();
		this.admName = admName;
		this.admEmail = admEmail;
		this.admPassword = admPassword;
		this.phone = phone;
		this.status = status;
	}
	public AdminBean(int admNo, String admName, String admEmail, String admPassword, String phone, int status) {
		super();
		this.admNo = admNo;
		this.admName = admName;
		this.admEmail = admEmail;
		this.admPassword = admPassword;
		this.phone = phone;
		this.status = status;
	}
	public AdminBean() {
	}

}


