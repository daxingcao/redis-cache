package com.caodaxing.redis.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang.StringUtils;

/**
 * 逻辑判断表达式判断工具类
 * @author Administrator
 *
 */
public class ExprMatchUtils {
	
	private static final ScriptEngineManager manager = new ScriptEngineManager();
	private static ThreadLocal<ScriptEngine> threadLocal = new ThreadLocal<>();
	
	/**
	 * 将逻辑表达式中的参数放入ScriptEngine对象中
	 * @param params
	 */
	public static void setMatchParam(Map<String, Object> params) {
		ScriptEngine engine = getScriptEngine();
		Set<Entry<String, Object>> entrySet = params.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			Object obj = entry.getValue();
			Double value = null;
			if(obj != null && StringUtils.isNotEmpty(obj.toString()) 
					&& StringUtil.isNumber(obj.toString())) {
				value = Double.valueOf(obj.toString());
			}else {
				continue;
			}
			engine.put(entry.getKey(), value);
		}
	}
	
	/**
	 * 验证逻辑表达式是否成立
	 * 使用该方法前,请确认逻辑表达式中的参数已经放入ScriptEngine对象中
	 * 可使用ScriptEngine()方法放入参数
	 * @param expr
	 * @return
	 * @throws ScriptException
	 */
	public static boolean verifyExpr(String expr) throws ScriptException {
		Boolean eval = false;
		ScriptEngine engine = getScriptEngine();
		if(StringUtils.isEmpty(expr)) {
			return true;
		}
		eval = (boolean)engine.eval(expr);
		return eval;
	}
	
	/**
	 * 传入表达式参数和表达式,验证逻辑表达式是否成立
	 * @param params
	 * @param expr
	 * @return
	 * @throws ScriptException
	 */
	public static boolean verifyExper(Map<String, Object> params,String expr) throws ScriptException {
		setMatchParam(params);
		return verifyExpr(expr);
	}
	
	private static ScriptEngine getScriptEngine() {
		ScriptEngine engine = threadLocal.get();
		if(engine == null) {
			engine = manager.getEngineByName("javascript");
			threadLocal.set(engine);
		}
		return threadLocal.get();
	}
	
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<>();
		map.put("aa", 2.3);
		map.put("bb", 6);
		setMatchParam(map);
		try {
			boolean verifyExpr = verifyExpr("aa > 2 && bb < 7");
			System.out.println(verifyExpr);
			boolean verifyExpr1 = verifyExpr("aa > 2 && bb > 7");
			System.out.println(verifyExpr1);
			boolean verifyExpr2 = verifyExpr("aa > 2 && bb <= 6");
			System.out.println(verifyExpr2);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
