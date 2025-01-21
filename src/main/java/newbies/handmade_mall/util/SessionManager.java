package newbies.handmade_mall.util;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionManager {

    /**
     * HttpSession 가져오기
     *
     * @param create false: 세션 없으면 생성 안함, true: 세션 없으면 생성
     */
    public static HttpSession getHttpSession(boolean create) {
        // RequestContextHolder에서 ServletRequestAttributes 얻기
        ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        // Session 가져오기
        return servletRequestAttribute.getRequest().getSession(create);
    }

    /**
     * 세션에 속성 추가
     *
     * @param key   세션 속성 키
     * @param value 세션 속성 값
     */
    public static void setHttpSessionAttribute(String key, Object value) {

        HttpSession httpSession = getHttpSession(true);

        httpSession.setAttribute(key, value);
    }

    public static Object getHttpSessionAttribute(String key) {
        HttpSession httpSession = getHttpSession(false);

        if (httpSession == null) return null;

        return httpSession.getAttribute(key);

    }

    public static void removeHttpSessionAttribute(String key) {

        HttpSession httpSession = getHttpSession(false);

        // 세션 존재 널 체크
        if (httpSession == null) return;

        httpSession.removeAttribute(key);
    }
}
