package com.kaikeba.controller;

import com.kaikeba.bean.Message;
import com.kaikeba.mvc.ResponseBody;
import com.kaikeba.service.AdminService;
import com.kaikeba.util.JSONUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class AdminController {

    @ResponseBody("/admin/login.do")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        // 1. 接参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // 2. 调用service传参数，并获取结果
        boolean result = AdminService.login(username, password);

        // 3. 根据结果，返回不同的数据给ajax
        Message msg = null;
        if (result) {
            msg = new Message(0, "登录成功");   // {status: 0, result: "登录成功"}
            // 登陆时间和登录ip的更新
            Date date = new Date();
            String ip = request.getRemoteAddr();
            AdminService.updateLoginTimeAndIp(username, date, ip);
            request.getSession().setAttribute("adminUserName", username);
        } else  {
            msg = new Message(-1, "登录失败");   // {status: -1, result: "登录失败"}
        }

        // 4. 将数据转换为JSON
        String json = JSONUtil.toJSON(msg);
        // 5. 将json回复给ajax
        return json;
    }
}
