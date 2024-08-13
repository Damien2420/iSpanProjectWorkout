package com.four.model.memberAdm;

public class ChatUserDTO {
	
	private String memNo;
    private String memName;
    private byte[] memPic;

    // Getters and Setters
    public String getMemNo() {
        return memNo;
    }

    public void setMemNo(String memNo) {
        this.memNo = memNo;
    }

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public byte[] getMemPic() {
        return memPic;
    }

    public void setMemPic(byte[] memPic) {
        this.memPic = memPic;
    }

    // Convert method
    public ChatUserDTO convertUser(MemberBean user) {
        ChatUserDTO chatUserDTO = new ChatUserDTO();
        chatUserDTO.setMemNo(user.getMemNo() + "");
        chatUserDTO.setMemName(user.getMemName());
        chatUserDTO.setMemPic(user.getMemPic());
        return chatUserDTO;
    }

}
