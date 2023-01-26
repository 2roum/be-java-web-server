package controller;

import exception.DuplicateIdException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.user.UserService;
import utils.enums.ContentType;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static utils.PathManager.HOME_PATH;

public class UserCreateController extends AbstractController {
    public static final String PATH = "/user/create";
    private static final Logger logger = LoggerFactory.getLogger(UserCreateController.class);
    private final UserService userService;


    public UserCreateController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            Map<String, String> bodyParams = httpRequest.getRequestBody();
            userService.createUser(bodyParams);
            httpResponse.redirect(HOME_PATH);
        }
        catch (DuplicateIdException e) {
            httpResponse.setBody(e.getMessage().getBytes(StandardCharsets.UTF_8));
            httpResponse.setContentType(ContentType.HTML);
        }
    }
}
