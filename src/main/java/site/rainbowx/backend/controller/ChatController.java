package site.rainbowx.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.rainbowx.backend.entity.Chat;
import site.rainbowx.backend.service.ChatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // 创建聊天
    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody Chat chat) {
        chat.setCreatedAt(LocalDateTime.now()); // 设置创建时间
        Chat created = chatService.createChat(chat);
        return ResponseEntity.ok(created);
    }

    // 更新聊天
    @PutMapping("/{id}")
    public ResponseEntity<Chat> updateChat(@PathVariable Long id, @RequestBody Chat chat) {
        chat.setId(id);
        Chat updated = chatService.updateChat(chat);
        return ResponseEntity.ok(updated);
    }

    // 删除聊天
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        chatService.deleteChat(id);
        return ResponseEntity.noContent().build();
    }

    // 获取聊天详情
    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long id) {
        Chat chat = chatService.getChatById(id);
        return ResponseEntity.ok(chat);
    }

    // 获取所有聊天
    @GetMapping
    public ResponseEntity<List<Chat>> getAllChats() {
        return ResponseEntity.ok(chatService.getAllChats());
    }

    // 分页获取聊天
    @GetMapping("/page")
    public ResponseEntity<Page<Chat>> getChatsByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(chatService.getChatsByPage(PageRequest.of(page, size)));
    }

    // 按类型获取聊天（group/private）
    @GetMapping("/type")
    public ResponseEntity<List<Chat>> getChatsByType(@RequestParam String type) {
        return ResponseEntity.ok(chatService.getChatsByType(type));
    }

    // 添加参与者
    @PostMapping("/{chatId}/participants")
    public ResponseEntity<Void> addParticipant(@PathVariable Long chatId, @RequestParam String userId) {
        chatService.addParticipant(chatId, userId);
        return ResponseEntity.ok().build();
    }

    // 移除参与者
    @DeleteMapping("/{chatId}/participants")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long chatId, @RequestParam String userId) {
        chatService.removeParticipant(chatId, userId);
        return ResponseEntity.ok().build();
    }
}
