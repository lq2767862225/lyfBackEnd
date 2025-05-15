package site.rainbowx.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.rainbowx.backend.entity.Chat;
import site.rainbowx.backend.exception.NotFoundException;
import site.rainbowx.backend.repository.ChatRepository;

import java.util.List;

@Service
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    /**
     * 创建聊天会话
     */
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    /**
     * 更新聊天信息
     */
    public Chat updateChat(Chat chat) {
        if (!chatRepository.existsById(chat.getId())) {
            throw new NotFoundException("聊天记录不存在 ID: " + chat.getId());
        }
        return chatRepository.save(chat);
    }

    /**
     * 删除聊天
     */
    public void deleteChat(Long id) {
        chatRepository.deleteById(id);
    }

    /**
     * 根据ID获取聊天详情
     */
    @Transactional(readOnly = true)
    public Chat getChatById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("聊天记录不存在 ID: " + id));
    }

    /**
     * 获取所有聊天列表
     */
    @Transactional(readOnly = true)
    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    /**
     * 分页查询聊天记录
     */
    @Transactional(readOnly = true)
    public Page<Chat> getChatsByPage(Pageable pageable) {
        return chatRepository.findAll(pageable);
    }

    /**
     * 根据类型筛选聊天
     */
    @Transactional(readOnly = true)
    public List<Chat> getChatsByType(String type) {
        return chatRepository.findByType(type);
    }

    /**
     * 添加聊天参与者
     */
    public void addParticipant(Long chatId, String userId) {
        Chat chat = getChatById(chatId);
        chat.getParticipants().add(userId);
        chatRepository.save(chat);
    }

    /**
     * 移除聊天参与者
     */
    public void removeParticipant(Long chatId, String userId) {
        Chat chat = getChatById(chatId);
        chat.getParticipants().remove(userId);
        chatRepository.save(chat);
    }
}