package com.nmoumoulidis.opensensor.restInterface;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.text.Html;

import com.nmoumoulidis.opensensor.restInterface.requests.ServerPostRestRequest;

public class BatchDataSendToServerServiceHelper
{
	private ServerPostRestRequest postDataRequest;
	private HttpPost httpPost;
	private HttpClient httpClient;
	private HttpContext localContext;
	private HttpResponse response;
	
	private int statusCode;

	private HttpEntity entity;
	private String body;
	
	public BatchDataSendToServerServiceHelper(ServerPostRestRequest postRequest) {
		this.postDataRequest = postRequest;
		this.httpClient = new DefaultHttpClient();
		this.localContext = new BasicHttpContext();
	}

	public void performRequest() {
		httpPost = new HttpPost(postDataRequest.getBaseUrl());
		httpPost.setHeader("Accept", postDataRequest.getAccept());
		StringEntity sEntity;
		try {
			sEntity = new StringEntity(((ServerPostRestRequest) postDataRequest).getData());
			httpPost.setEntity(sEntity);
			response = httpClient.execute(httpPost, localContext);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace(); // Cannot find host name...
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleResponse() {
		if(response == null) {
			return;
		}

		this.statusCode = response.getStatusLine().getStatusCode();
		this.entity = response.getEntity();
		try {
			if(entity != null) {
				this.body = EntityUtils.toString(entity);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(statusCode == 200) {
			System.out.println("BATCH DATA Sent to server Successfully!");
		}
		else {
			System.out.println("BATCH DATA transfer to server failed: ");
			System.out.println(Html.fromHtml(body));
		}
	}
}
