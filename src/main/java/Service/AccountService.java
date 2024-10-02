package Service;

import Model.Account;
import DAO.AccountDAO;

/**
 * AccountService class containing business logic between the web layer (controller) and
 * persistence layer (DAO). 
 */
public class AccountService {

    AccountDAO accountDAO;
    /**
     * No-args constructor for a accountService instantiates a plain accountDAO.
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /**
     * Add a new account to the database.
     *
     * @param account an object representing a new Account.
     * @return the newly registered account if the add operation was successful, including the account_id. 
     */
    public Account addAccount(Account account) {
        if (account.getUsername().length() < 1) return null;
        if (account.getPassword().length() < 4) return null;
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) return null;
        return accountDAO.insertAccount(account);
    }

    /**
     * Get an account in the database.
     * 
     * @param account_id the ID of the account to be retrieved.
     * @return Account object with values retrieved from account_id, null if account_id not found.
     */
    public Account getAccountById(int account_id) {
        return accountDAO.getAccountById(account_id);
    }

    /**
     * Validate login with account retrieval by username and password.
     * 
     * @param account an object representing an Account login attempt.
     * @return the existing account if the query is successful, including the account_id.
     */
    public Account validLogin(Account account) {
        return accountDAO.getAccountByUsernameAndPassword(account);
    }
}
