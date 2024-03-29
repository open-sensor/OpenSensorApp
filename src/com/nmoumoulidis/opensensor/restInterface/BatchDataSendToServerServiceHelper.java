package com.nmoumoulidis.opensensor.restInterface;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
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

/**
 * The second key class that utilizes the phone as an data aggregator between
 * the OpenSensor Station and Server. It is instantiated and used by the 
 * {@link BatchDataRetrieveService} to send the data that was retrieved from the
 * OpenSensor Station and now are available on the phone, to the remote OpenSensor Server.
 * @author Nikos Moumoulidis
 *
 */
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
	private StringEntity sEntity;
	
	public BatchDataSendToServerServiceHelper(ServerPostRestRequest postRequest) {
		this.postDataRequest = postRequest;
		this.httpClient = new DefaultHttpClient();
		this.localContext = new BasicHttpContext();
	}

	/**
	 * HTTP REST POST request.
	 * @return
	 */
	public boolean performRequest() {
		httpPost = new HttpPost(postDataRequest.getBaseUrl());
		httpPost.setHeader("Accept", postDataRequest.getAccept());
		try {
			sEntity = new StringEntity(((ServerPostRestRequest) postDataRequest).getData());
			httpPost.setEntity(sEntity);
			response = httpClient.execute(httpPost, localContext);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			return false; // return to try again.
		} catch (SocketException e) {
			return false; // return to try again.
		} catch (ClientProtocolException e) {
			return false; // return to try again.
		} catch (IOException e) {
			return false; // return to try again.
		}
		return true;
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
