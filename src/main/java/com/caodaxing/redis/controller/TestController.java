package com.caodaxing.redis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.caodaxing.redis.entity.LoginUser;
import com.caodaxing.redis.service.LoginUserService;

@Controller
public class TestController {
	
	@Autowired
	private LoginUserService loginUserService;
	
	@RequestMapping("/test")
	@ResponseBody
	public List<LoginUser> test(int pageNumber){
		return loginUserService.getUserInfoList(pageNumber);
	}
	
	@RequestMapping("/clear")
	@ResponseBody
	public String clear() {
		loginUserService.clearData();
		return "删除成功";
	}

}
