package com.caodaxing.redis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.caodaxing.redis.dao.LoginUserMapper;
import com.caodaxing.redis.entity.LoginUser;
import com.caodaxing.redis.entity.LoginUserExample;
import com.caodaxing.redis.service.LoginUserService;

@Service
public class LoginUserServiceImpl implements LoginUserService {
	
	@Autowired
	private LoginUserMapper loginUserMapper;

	/**
	 * 调用该方法时,会先触发Cache接口的get方法,如果缓存中查到数据,则直接返回数据,不再走该方法
	 * 如果没有查到数据,执行该方法,结束后调用Cache接口的put方法,将返回的数据存入缓存中
	 * 注意:key的值是字符串的话,使用单引号括起来,例:key="'userName'",如果不加单引号默认做Spel表达式识别
	 * 会报SpelEvaluationException错误,spel表达式:#方法参数
	 */
	@Cacheable(value="selectUserInfo",key="'userinfo_'+#pageNumber")
	@Override
	public List<LoginUser> getUserInfoList(int pageNumber) {
		System.out.println("select userInfo start...");
		return getUser(pageNumber);
	}
	
	/**
	 * 删除缓存数据,value为要删除的哪个模块数据,key为要删除的数据键值,调用Cache类的evict方法
	 * 当allEntries为true时,删除value模块里的所有数据,调用Cache类的clear方法,key可不填
	 * allEntries默认为false,此时key值必填,否则报错
	 */
	@CacheEvict(value="selectUserInfo",key="'userinfo_1'",allEntries=false)
	@Override
	public void clearData() {
		System.out.println("clear all data...");
	}
	
	private List<LoginUser> getUser(int pageNumber) {
		System.out.println("query database");
		LoginUserExample example = new LoginUserExample();
		example.setOrderByClause(" id asc limit "+((pageNumber-1)*1)+","+(pageNumber*1));
		List<LoginUser> selectByExample = loginUserMapper.selectByExample(example);
		return selectByExample;
	}

}
