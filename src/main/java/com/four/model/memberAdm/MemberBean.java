package com.four.model.memberAdm;

import java.io.Serializable;
import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity @Table(name = "member")
public class MemberBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @Column(name = "memNo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer memNo;
	
	private String memName;
	private String memEmail;
	@Column(nullable = true)
	private String memPassword;
	private Boolean isGoogleLogin;
	private Integer gender;
	private String birth;
	private Integer age;
	private String phone;
	private String regDate;
	
	@Column(name = "status", columnDefinition = "int default 1")
	private Integer status = 1;
	private String nickName;
	
	@Lob
//    @Column(name = "memPic", columnDefinition="LONGBLOB")
	private byte[] memPic;
	
	public MemberBean() {
	}

	public MemberBean(String memName, String memEmail, String memPassword, Integer gender, String birth, String phone) {
		this.memName = memName;
		this.memEmail = memEmail;
		this.memPassword = memPassword;
		this.gender = gender;
		this.birth = birth;
		this.phone = phone;
	}

	public MemberBean(String memName, String memEmail, String memPassword, Integer gender, String birth, String phone,
			String nickName) {
		this.memName = memName;
		this.memEmail = memEmail;
		this.memPassword = memPassword;
		this.gender = gender;
		this.birth = birth;
		this.phone = phone;
		this.nickName = nickName;
	}
	
	public MemberBean(Integer memNo, String memName, String memEmail, String memPassword, Integer gender, String birth,
			String phone, Integer status, String nickName, byte[] memPic) {
		this.memNo = memNo;
		this.memName = memName;
		this.memEmail = memEmail;
		this.memPassword = memPassword;
		this.gender = gender;
		this.birth = birth;
		this.phone = phone;
		this.status = status;
		this.nickName = nickName;
		this.memPic = memPic;
	}

	public Integer getMemNo() {
		return memNo;
	}

	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public String getMemEmail() {
		return memEmail;
	}

	public void setMemEmail(String memEmail) {
		this.memEmail = memEmail;
	}

	public String getMemPassword() {
		return memPassword;
	}

	public void setMemPassword(String memPassword) {
		this.memPassword = memPassword;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public byte[] getMemPic() {
		return memPic;
	}

	public void setMemPic(byte[] memPic) {
		this.memPic = memPic;
	}
	
	public Boolean getIsGoogleLogin() {
		return isGoogleLogin;
	}

	public void setIsGoogleLogin(Boolean isGoogleLogin) {
		this.isGoogleLogin = isGoogleLogin;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MemberBean [memNo=" + memNo + ", memName=" + memName + ", memEmail=" + memEmail + ", memPassword="
				+ memPassword + ", gender=" + gender + ", birth=" + birth + ", age=" + age + ", phone=" + phone
				+ ", regDate=" + regDate + ", status=" + status + ", nickName=" + nickName + ", memPic="
				+ Arrays.toString(memPic) + "]";
	}

}

