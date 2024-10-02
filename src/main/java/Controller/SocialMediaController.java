package Controller;

import Service.MessageService;
import Service.AccountService;
import Model.Message;
import Model.Account;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private MessageService messageService;
    private AccountService accountService;

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        messageService = new MessageService();
        accountService = new AccountService();

        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);

        return app;
    }

    /**
     * POST '/register' Handler - Creates a new account.
     * 
     * @param ctx Javalin context object contains info about HTTP request and response.
     * @throws JsonProcessingException
     */
    private void postRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        String jsonString = ctx.body();
        Account account = om.readValue(jsonString, Account.class);
        Account newAccount = accountService.addAccount(account);
        if (newAccount != null) {
            ctx.json(newAccount).status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * POST '/login' Handler - Verifies Login attempt with username and password.
     * 
     * @param ctx Javalin context object contains info about HTTP request and response.
     * @throws JsonProcessingException
     */
    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        String jsonString = ctx.body();
        Account account = om.readValue(jsonString, Account.class);
        Account existingAccount = accountService.validLogin(account);
        if (existingAccount != null) {
            ctx.json(existingAccount).status(200);
        } else {
            ctx.status(401);
        }
    }

    /**
     * POST '/messages' Handler - Creates a new message.
     * 
     * @param ctx Javalin context object contains info about HTTP request and response.
     * @throws JsonProcessingException
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        String jsonString = ctx.body();
        Message message = om.readValue(jsonString, Message.class);
        Message newMessage = messageService.addMessage(message);
        if (newMessage != null && accountService.getAccountById(message.getPosted_by()) != null) {
            ctx.json(newMessage).status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * GET '/messages' Handler - Retrieves all messages.
     * 
     * @param ctx Javalin context object contains info about HTTP request and response.
     */
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages).status(200);
    }

    /**
     * GET 'messages/{message_id}' Handler - Retrieves the message with message_id.
     * 
     * @param ctx Javalin context object contains info about HTTP request and response.
     */
    private void getMessageByIdHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(id);
        if (message != null) ctx.json(message);
        ctx.status(200);
    }

    /**
     * DELETE '/messages/{message_id}' Handler - Deletes the message with message_id.
     * 
     * @param ctx Javalin context object contains info about HTTP request and response.
     */
    private void deleteMessageHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(id);
        if (message != null) {
            messageService.deleteMessage(id);
            ctx.json(message);
        }
        ctx.status(200);
    }

    /**
     * PATCH '/messages/{message_id}' Handler - Updates the message with message_id.
     * 
     * @param ctx Javalin context object contains info about HTTP request and response.
     * @throws JsonProcessingException
     */
    private void patchMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        String jsonString = ctx.body();
        Message message = om.readValue(jsonString, Message.class);

        Message updatedMessage = messageService.updateMessage(id, message);
        if (updatedMessage != null) {
            ctx.json(updatedMessage).status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * GET '/accounts/{account_id}/messages' Handler - Retrieves all messages from a user.
     * 
     * @param ctx Javalin context object contains info about HTTP request and response.
     */
    private void getMessagesByAccountHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByAccountId(id);
        ctx.json(messages).status(200);
    }
}