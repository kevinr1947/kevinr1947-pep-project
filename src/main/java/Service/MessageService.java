package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.List;

/**
 * MessageService class containing business logic between the web layer (controller) and
 * persistence layer (DAO). 
 */
public class MessageService {

    MessageDAO messageDAO;
    /**
     * No-args constructor for a messageService instantiates a plain messageDAO.
     */
    public MessageService(){
        messageDAO = new MessageDAO();
    }

    /**
     * Add a new message to the database.
     *
     * @param message an object representing a new Message.
     * @return the newly added message if the add operation was successful, including the message_id. 
     */
    public Message addMessage(Message message){
        if (message.getMessage_text().length() > 255 || message.getMessage_text().length() < 1) return null;
        return messageDAO.insertMessage(message);
    }

    /**
     * Retrieve all messages.
     *
     * @return all messages in the database.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Get a message in the database.
     * 
     * @param message_id the ID of the message to be retrieved.
     * @return message object with values retrieved from message_id, null if message_id not found.
     */
    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    /**
     * Delete an existing message from the database.
     * 
     * @param message_id the ID of the message to be deleted.
     * @return 1 if the message was successfully deleted, 0 otherwise.
     */
    public int deleteMessage(int message_id) {
        return messageDAO.deleteMessage(message_id);
    }

    /**
     * Update an existing message from the database.
     *
     * @param message_id the ID of the message to be modified.
     * @param message an object containing all data that should replace the values contained by the existing message_id.
     * @return the newly updated message if the update operation was successful. Returns null if the update operation was
     *         unsuccessful.
     */
    public Message updateMessage(int message_id, Message message) {
        if (messageDAO.getMessageById(message_id) == null) return null;
        if (message.getMessage_text().length() > 255 || message.getMessage_text().length() < 1) return null;
        messageDAO.updateMessage(message_id, message);
        return messageDAO.getMessageById(message_id);
    }

    /**
     * Retrieve all messages from a user given account ID.
     *
     * @param account_id the ID of the account to get all messages from.
     * @return all messages posted by account_id.
     */
    public List<Message> getAllMessagesByAccountId(int account_id) {
        return messageDAO.getAllMessagesByAccountId(account_id);
    }

}