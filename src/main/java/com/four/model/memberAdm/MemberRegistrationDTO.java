package com.four.model.memberAdm;

// DTO
public class MemberRegistrationDTO {
	
	private int month;
    private long count;
    
	public MemberRegistrationDTO() {
	}
	
	public MemberRegistrationDTO(int month, long count) {
		this.month = month;
		this.count = count;
	}

	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
    
}
