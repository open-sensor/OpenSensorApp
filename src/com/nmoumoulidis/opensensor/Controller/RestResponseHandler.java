package com.nmoumoulidis.opensensor.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.text.Html;

import com.nmoumoulidis.opensensor.View.MainActivity;

public class RestResponseHandler 
{
	private HttpEntity entity;
	
	private int statusCode;
	private String contentType;
	private String statusReasonPhrase;
	private String body;
	private Request request;
	
	public RestResponseHandler(HttpResponse response, Request request) {
		this.request = request;
		this.statusCode = response.getStatusLine().getStatusCode();
		this.statusReasonPhrase = response.getStatusLine().getReasonPhrase();
		this.contentType = response.getLastHeader("Content-Type").getValue();
		this.entity = response.getEntity();
		try {
			this.body = entityToString();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String entityToString() throws IllegalStateException, IOException {
		if (entity != null) {
            // get entity contents and convert it to string
            InputStream instream = entity.getContent();
            BufferedReader r = new BufferedReader(new InputStreamReader(instream));
            StringBuilder total = new StringBuilder(instream.available());
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            instream.close();
            return total.toString();
		}
		return "";
	}

	public void handleResponse(MainActivity mainActivity) {
		if(statusCode == 200) {
			if(request.getMethod() == "PUT") {
				mainActivity.getmResultText().setText("The location was successfully changed!");
			}
			else {
				if(request.getAccept() == "application/json") {
					if(request.getRelativeUrl() == "/sensorlist") {
						mainActivity.getmResultText().setText("sensorlist: "+body);
					}
					else if(request.getRelativeUrl() == "/data"){
						mainActivity.getmResultText().setText("sensor data: "+body);
					}
				}
				else {
					mainActivity.getmResultText().setText(body);
				}
			}
		}
		else if(statusCode == 204) {
			mainActivity.getmResultText().setText(statusCode + " " +statusReasonPhrase);
		}
		else {
			mainActivity.getmResultText().setText(Html.fromHtml(body));
		}
	}
}
