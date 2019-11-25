package org.valdi.jmusicbot.delivery.webserver.response.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.valdi.jmusicbot.delivery.webserver.response.Response;
import org.valdi.jmusicbot.delivery.webserver.response.ResponseType;

/**
 * Generic JSON response implemented using Gson.
 * <p>
 * Returns a JSON version of the given object.
 *
 * @author Rsl1122
 */
public class JSONResponse extends Response {

    public JSONResponse(Object object) {
        this(new Gson().toJson(object));
    }

    public JSONResponse(JsonElement json) {
        this(json.getAsString());
    }

    public JSONResponse(String jsonString) {
        super(ResponseType.JSON);
        super.setHeader("HTTP/1.1 200 OK");
        super.setContent(jsonString);
    }
}