package com.four.controller.memberAdm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.four.model.memberAdm.ChatMessageDTO;

@RestController
public class ChatApiController {
	
	private List<ChatMessageDTO> chatMessages = new ArrayList<>();
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 獲取聊天列表
    @GetMapping("/api/chat/list")
    public List<ChatMessageDTO> getChatList() {
    	// 創建一個HashMap來儲存每個對話的最新消息。key是一個組合鍵，由參與對話的兩個用戶的memNo組成，值是最新的消息。
        Map<String, ChatMessageDTO> latestMessages = new HashMap<>();

        for (ChatMessageDTO message : chatMessages) {  // 遍歷所有的聊天訊息
        	// 生成唯一組合鍵
            String key = message.getSender().getMemNo().compareTo(message.getRecipient().getMemNo()) < 0 
                ? message.getSender().getMemNo() + "-" + message.getRecipient().getMemNo()
                : message.getRecipient().getMemNo() + "-" + message.getSender().getMemNo();

            latestMessages.put(key, message);  // 將最新消息存入latestMessages。如果同一個鍵（對話）的消息已經存在，它將被新消息替換，確保始終是最新的消息。
        }
        return new ArrayList<>(latestMessages.values());  // 返回一個包含所有最新消息的列表。
    }
    
    // 獲取特定聊天歷史紀錄
    @GetMapping("/api/chat/history")
    public List<ChatMessageDTO> getChatHistory(@RequestParam String sender, @RequestParam String recipient) {
        return chatMessages.stream()  // 將chatMessages轉換為一個流，以便進行過濾操作。
        		//  過濾消息，僅保留那些發送者和接收者匹配sender和recipient參數的消息。
                .filter(msg -> (msg.getSender().getMemNo().equals(sender) && msg.getRecipient().getMemNo().equals(recipient)) ||
                               (msg.getSender().getMemNo().equals(recipient) && msg.getRecipient().getMemNo().equals(sender)))
                .collect(Collectors.toList());  //  將過濾後的消息蒐集到一個列表中並返回。
    }

    @MessageMapping("/chat")
    public void sendMessage(ChatMessageDTO message) throws Exception {
        message.setCreateTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        chatMessages.add(message);
        
        messagingTemplate.convertAndSendToUser(message.getSender().getMemNo(), "/queue/messages", message);
        messagingTemplate.convertAndSendToUser(message.getRecipient().getMemNo(), "/queue/messages", message);
        // 通知管理員有新消息
        messagingTemplate.convertAndSend("/topic/admin", "New message from " + message.getSender().getMemNo());
    }
    

}
