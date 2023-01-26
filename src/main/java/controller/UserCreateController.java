package controller;

import exception.HttpMethodException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.user.UserService;
import utils.enums.HttpMethod;

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
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        HttpMethod requestHttpMethod = httpRequest.getHttpMethod();
        if (HttpMethod.POST.equals(requestHttpMethod)) {
            doPost(httpRequest, httpResponse);
            return;
        }
        throw new HttpMethodException(requestHttpMethod);
    }

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> bodyParams = httpRequest.getRequestBody();
        userService.createUser(bodyParams);
        httpResponse.redirect(HOME_PATH);
    }
}
