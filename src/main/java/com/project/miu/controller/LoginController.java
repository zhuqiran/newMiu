package com.project.miu.controller;

import com.project.miu.bean.utils.ResultUtil;
import com.project.miu.bean.utils.TokenUtil;
import com.project.miu.commons.myEnum.ResultEnum;
import com.project.miu.bean.utils.Result;
import com.project.miu.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static com.project.miu.commons.constants.Constants.SESSION_TOKEN_KEY;
import static com.project.miu.commons.constants.Constants.SESSION_USERNAME_KEY;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login",method = {RequestMethod.POST})
    public Result login(String userName, String password, HttpServletRequest request){
        if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(password)){
            return ResultUtil.error(ResultEnum.USER_NOT_EXIST.getCode(),ResultEnum.USER_NOT_EXIST.getMsg());
        }
        int res = loginService.login(userName,password);
        if(res == 0){
            return ResultUtil.error(ResultEnum.USER_NOT_EXIST.getCode(),ResultEnum.USER_NOT_EXIST.getMsg());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        HttpSession session = request.getSession();
        String token = TokenUtil.genetateToken(userName);
        session.setAttribute(SESSION_TOKEN_KEY, token);
        session.setAttribute(SESSION_USERNAME_KEY, userName);
        session.setMaxInactiveInterval(30 * 60);
        map.put("token", token);
        return ResultUtil.success(map);
    }

    @RequestMapping(value = "/register",method = {RequestMethod.POST})
    public Result register(String userName,String password){
        if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(password)){
            return ResultUtil.error(ResultEnum.USER_NOT_EXIST.getCode(),ResultEnum.USER_NOT_EXIST.getMsg());
        }
        int res = loginService.register(userName, password);
        if(res == 0){
            return ResultUtil.error(ResultEnum.USER_NOT_EXIST.getCode(),ResultEnum.USER_NOT_EXIST.getMsg());
        }
        return ResultUtil.success();
    }

    @RequestMapping(value = "/exit",method = {RequestMethod.POST})
    //todo token中应该包含userName
    public Result exit(String userName, HttpServletRequest request){
        HttpSession session = request.getSession();
        String sessionToken = (String) session.getAttribute(SESSION_TOKEN_KEY);
        String name = (String) session.getAttribute(SESSION_USERNAME_KEY);
        if (sessionToken != null && name.equals(userName)) {
            session.removeAttribute(SESSION_USERNAME_KEY);
            session.removeAttribute(SESSION_TOKEN_KEY);
        }else {
            return ResultUtil.error(ResultEnum.ERROR.getCode(),ResultEnum.ERROR.getMsg());
        }
        return ResultUtil.success();
    }

}
