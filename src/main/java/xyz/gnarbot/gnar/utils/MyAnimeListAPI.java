package xyz.gnarbot.gnar.utils;

import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;

import java.io.IOException;

import static xyz.gnarbot.gnar.Bot.LOG;

/**
 * Created by Gatt on 20/06/2017.
 */
public class MyAnimeListAPI {

    private String username, password;
    private boolean loggedIn = false, canAttemptLogIn = true;
    private String apiStart = "https://myanimelist.net/api/";
    public final String SEARCH_ANIME = "anime/search.xml";
    public final String SEARCH_MANGA = "manga/search.xml";
    private long lastLogIn;

    public MyAnimeListAPI(String username, String password) {
        LOG.info("New MAL Class created");
        this.username = username;
        this.password = password;
        loggedIn = attemptLogIn();
        LOG.info("Log in state: " + loggedIn);
    }

    public MyAnimeListAPI(boolean noCredentials){
        loggedIn = false;
        canAttemptLogIn = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean attemptLogIn(){
        if (canAttemptLogIn) {
            LOG.info("Attempting to log in.");
            Request request = new Request.Builder().url(apiStart + "account/verify_credentials.xml")
                    .header("Authorization", Credentials.basic(username, password))
                    .build();
            try (Response response = HttpUtils.CLIENT.newCall(request).execute()) {
                JSONObject jso = new JSONObject(new JSONTokener(response.body().byteStream()));
                response.close();
                if (jso.has("user") && jso.getJSONObject("user").has("id")) {
                    lastLogIn = System.currentTimeMillis();
                    return true;
                }
            } catch (IOException e) {
                return false;
            }
            return false;
        }else{
            return false;
        }
    }

    public JSONObject makeRequest(String target, String arguments){
        Request request = new Request.Builder().url(apiStart + target + "?" + arguments)
                .header("Authorization", Credentials.basic(username, password))
                .build();
        try (Response response = HttpUtils.CLIENT.newCall(request).execute()) {
            JSONObject jso = new JSONObject(new JSONTokener(response.body().byteStream()));
            response.close();
            return jso;
        } catch (IOException e) {
            return null;
        }
    }
}