package net.swordie.ms.handlers.http;

import net.swordie.ms.ServerConstants;
import net.swordie.ms.client.User;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.handlers.CashShopHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class ApiRequestHandler {
    private static final Logger log = LogManager.getLogger(ApiRequestHandler.class);

    @HttpHandler(method = "POST", path = "/create")
    public static JSONObject handleCreateAccount(JSONObject requestData) {
        String username = (String) requestData.get("username");
        String password = (String) requestData.get("password");
        String email = (String) requestData.get("email");

        boolean success = true;
        String reason = null;
        if (User.getFromDBByName(username) != null) {
            success = false;
            reason = "Username is already in use.";
        }
        if (User.getFromDBByEmail(email) != null) {
            success = false;
            reason = "Email address is already in use.";
        }
        if (username.length() < 4 || password.length() < 4) {
            success = false;
            reason = "Username or password is too short.";
        }

        if (success) {
            User user = new User(username);
            user.setHashedPassword(password);
            user.setEmail(email);
            user.setCharacterSlots(ServerConstants.START_CHARACTERS);
            DatabaseManager.saveToDB(user);
            reason = "Account has been successfully created.";
        }

        JSONObject responseData = new JSONObject();
        responseData.put("success", success);
        responseData.put("reason", reason);
        return responseData;
    }

    @HttpHandler(method = "POST", path = "/token")
    public static JSONObject handleRequestToken(JSONObject requestData) {
        String username = (String) requestData.get("username");
        String password = (String) requestData.get("password");

        boolean success = true;
        String reason = null;
        User user = User.getFromDBByName(username);
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            success = false;
            reason = "Incorrect username or password.";
        }

        JSONObject responseData = new JSONObject();
        responseData.put("success", success);
        responseData.put("reason", reason);
        if (success) {
            responseData.put("token", username);
        }
        return responseData;
    }

}
