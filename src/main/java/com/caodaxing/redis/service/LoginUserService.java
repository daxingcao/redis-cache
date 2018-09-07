package com.caodaxing.redis.service;

import java.util.List;

import com.caodaxing.redis.entity.LoginUser;

public interface LoginUserService {
	
	public List<LoginUser> getUserInfoList(int pageNumber);
	
	public void clearData();

}
