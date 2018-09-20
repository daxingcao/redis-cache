package com.caodaxing.redis.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.caodaxing.redis.utils.MessageUtils;

@RestController
@RequestMapping("/aliyun/video")
public class AliplayerController {

	@Value("${aliyun.accessKeyId}")
	private String accessKeyId;
	@Value("${aliyun.accessKeySecret}")
	private String accessKeySecret;

	/**
	 * 通过上传文件的文件名得到上传需要的凭证,上传调用
	 * @param fileName
	 * @return
	 */
	@RequestMapping("/getToken")
	public Map<String, Object> getToken(String fileName) {
		DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret);
		try {
			CreateUploadVideoResponse response = createUploadVideo(client, fileName);
			Map<String, Object> result = MessageUtils.successMessage("获取凭证成功");
			result.put("response", response);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MessageUtils.errorMessage("获取凭证失败!");
	}
	
	/**
	 * 根据videoId刷新凭证
	 * @param vedioId
	 * @return
	 */
	@RequestMapping("/refreshToken")
	public Map<String, Object> refreshToken(String videoId){
		DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret);
		try {
			RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
			request.setVideoId(videoId);
			RefreshUploadVideoResponse response = client.getAcsResponse(request);
			Map<String, Object> map = MessageUtils.successMessage("刷新成功!");
			map.put("response", response);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MessageUtils.errorMessage("刷新失败!");
	}
	
	/**
	 * 获取播放凭证,点击播放时传videoId获取
	 * @param videoId
	 * @return
	 */
	@RequestMapping("/getPlayAuth")
	public Map<String, Object> getPlayAuth(String videoId) {
		DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret);
		try {
			GetVideoPlayAuthResponse response = getVideoPlayAuth(client, videoId);
			Map<String, Object> map = MessageUtils.successMessage("获取播放凭证成功!");
			map.put("response", response);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MessageUtils.errorMessage("获取播放凭证失败!");
	}
	
	
	private GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client,String videoId) throws Exception {
		GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
		request.setVideoId(videoId);
		request.setAuthInfoTimeout(3000L);
		return client.getAcsResponse(request);
	}

	private DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) {
		final String regionId = "cn-shanghai";
		DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
		DefaultAcsClient client = new DefaultAcsClient(profile);
		return client;
	}

	private CreateUploadVideoResponse createUploadVideo(DefaultAcsClient client, String fileName) throws Exception {
		CreateUploadVideoRequest request = new CreateUploadVideoRequest();
		request.setTitle(fileName.substring(0, fileName.indexOf(".")));
		request.setFileName(fileName);
		return client.getAcsResponse(request);
	}

}
