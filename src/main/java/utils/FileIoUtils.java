package utils;


import model.Session;
import model.User;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class FileIoUtils {
    private static final String STATIC_RESOURCE_PATH = FileIoUtils.class.getClassLoader().getResource("./static").getPath();
    private static final String TEMPLATE_RESOURCE_PATH = FileIoUtils.class.getClassLoader().getResource("./templates").getPath();
    private static final Set<String> TEMPLATE_EXTENSIONS = Set.of("html", "ico");

    public static byte[] loadFile(String file) throws IOException {
        String fileExtension = getExtension(file);
        if (TEMPLATE_EXTENSIONS.contains(fileExtension))
            return Files.readAllBytes(new File(TEMPLATE_RESOURCE_PATH + file).toPath());
        return Files.readAllBytes(new File(STATIC_RESOURCE_PATH + file).toPath());
    }

    public static String getExtension(String file) {
        return file.substring(file.lastIndexOf(".") + 1);
    }

    public static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> map = new HashMap<>();
        Arrays.stream(queryString.split("&")).filter(param -> !param.startsWith("="))
                .forEach(param -> {
                    String[] keyValuePair = param.split("=", 2);
                    String name = URLDecoder.decode(keyValuePair[0], StandardCharsets.UTF_8);
                    String value = keyValuePair.length > 1 ? URLDecoder.decode(keyValuePair[1], StandardCharsets.UTF_8) : "";
                    map.put(name, value);
                });
        return map;
    }

    public static byte[] makePage(String path, Session session) throws IOException {
        String page = new String(loadFile(path));
        if (path.contains("index.html") && session != null) {
            page = page.replace("<!--username-->", String.format("<li><a>%s</a></li>", session.getName()));
        }
        return removeUselessButton(page, session != null).getBytes();
    }

    private static String removeLoginButton(String page) {
        String target_index = "<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>";
        String target_others = "<li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>";
        return page.replace(target_index, "").replace(target_others, "");
    }

    private static String removeLogoutButton(String page) {
        String target = "<li><a href=\"/user/logout\" role=\"button\">로그아웃</a></li>";
        return page.replace(target, "");
    }

    private static String removeUselessButton(String page, boolean loggedIn) {
        if (loggedIn)
            return removeLoginButton(page);
        return removeLogoutButton(page);
    }

    public static byte[] makeUserListPage(Collection<User> users, Session session) throws IOException {
        StringBuilder sb = new StringBuilder();
        String userListPage = removeUselessButton(new String(loadFile(PathManager.USER_LIST_PATH)), session != null);
        sb.append(makeLoginUserHTML(session));
        int cnt = 2;
        for (User user : users) {
            if (user.getUserId().equals(session.getUserId()))
                continue;
            sb.append("<tr>");
            sb.append(String.format("<th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td> <td>",
                    cnt++, user.getUserId(), user.getName(), user.getEmail()));
            sb.append("</td> </tr>");
        }
        return userListPage.replace("<!--userList-->", sb.toString()).getBytes();
    }

    public static String makeLoginUserHTML(Session session) {
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        sb.append(String.format("<th scope=\"row\">1</th> <td>%s</td> <td>%s</td> <td>%s</td>",
                session.getUserId(), session.getName(), session.getEmail()));
        sb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
        sb.append("</tr>");
        return sb.toString();
    }
}
