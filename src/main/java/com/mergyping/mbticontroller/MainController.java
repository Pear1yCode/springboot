package com.mergyping.mbticontroller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping(value = {"/", "/main"})
    public String main(HttpSession session) {
        session.invalidate(); // 메인으로 오면 세션을 만료시키는건데 넣은 이유는 만약에 뒤로갔을때 세션이 유지되는 문제가 있어서 값이 계속 유지가 되기 때문 -> 따라서 만든 객체를 null로 만들기 위해서 생성
        return "main";
    }
}
