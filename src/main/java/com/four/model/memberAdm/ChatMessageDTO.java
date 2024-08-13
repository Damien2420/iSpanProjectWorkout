package com.four.model.memberAdm;

public class ChatMessageDTO {
	
	private ChatUserDTO sender;
    private ChatUserDTO recipient;
    private String message;
    private String createDate;
    private String createTime;

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public ChatUserDTO getSender() {
		return sender;
	}

	public void setSender(ChatUserDTO sender) {
		this.sender = sender;
	}

	public ChatUserDTO getRecipient() {
		return recipient;
	}

	public void setRecipient(ChatUserDTO recipient) {
		this.recipient = recipient;
	}

	public void setMessage(String message) {
        this.message = message;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

