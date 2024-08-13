package com.four.model.memberAdm;

public class MemberAgeDTO {
	
	private String ageGroup;
    private long count;
    
    
    public MemberAgeDTO() {
    }
    public MemberAgeDTO(String ageGroup, long count) {
        this.ageGroup = ageGroup;
        this.count = count;
    }
	public String getAgeGroup() {
		return ageGroup;
	}
	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
}
