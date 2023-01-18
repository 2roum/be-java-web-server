package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import utils.FileIoUtils;
import utils.HttpMethod;

import java.util.Map;

public class UserCreateController implements Controller {
    public static final String PATH = "/user/create";
    private static final Logger logger = LoggerFactory.getLogger(UserCreateController.class);
    private final UserService userService;


    public UserCreateController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public HttpResponse service(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.getHttpMethod().equals(HttpMethod.POST)) {
            return doPost(httpRequest, httpResponse);
        }
        throw new IllegalArgumentException("존재하지 않는 Http 메서드입니다.");
    }

    private HttpResponse doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> params = FileIoUtils.parseQueryString(httpRequest.getRequestBody());
        userService.createUser(params);
        httpResponse.redirectHome();
        return httpResponse;
    }
}
