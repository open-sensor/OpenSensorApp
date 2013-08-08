package com.nmoumoulidis.opensensor.restInterface;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.nmoumoulidis.opensensor.restInterface.requests.ServerGetRestRequest;
import com.nmoumoulidis.opensensor.view.ServerActivity;

import android.os.AsyncTask;
import android.view.View;

/**
 * An Android AsyncTask for all the REST GET requests that are performed towards 
 * the OpenSensor Server, while providing visual feedback to the user. 
 * All AsyncTasks are queued by the OS. The response is passed down to a 
 * {@link ServerRestResponseHandler} object to handle.
 * @author Nikos Moumoulidis
 *
 */
public class ServerRestRequestTask extends AsyncTask<ServerGetRestRequest, Integer, Boolean>
{
	private ServerActivity serverActivity;

	private HttpClient httpClient;
	private HttpContext localContext;
	private HttpGet httpGet;
	private HttpResponse response;
	private ServerRestResponseHandler responseHandler;

	public ServerRestRequestTask(ServerActivity serverActivity) {
		super();
		this.serverActivity = serverActivity;
		this.httpClient = new DefaultHttpClient();
		this.localContext = new BasicHttpContext();
	}

	@Override
	protected Boolean doInBackground(ServerGetRestRequest... params) {
		publishProgress(0); //------ progress bar update ------
		ServerGetRestRequest request = params[0];
		publishProgress(5); //------ progress bar update ------
		responseHandler = new ServerRestResponseHandler();
		try {
			publishProgress(10); //------ progress bar update ------
			httpGet = new HttpGet(request.getFullUrl());
			publishProgress(12); //------ progress bar update ------
			httpGet.setHeader("Accept", request.getAccept());
			publishProgress(15); //------ progress bar update ------
			response = httpClient.execute(httpGet, localContext);
		} catch (ClientProtocolException e) {
			responseHandler.setFailureReason(ServerRestResponseHandler.SERVER_NOT_REACHABLE);
			publishProgress(90); //------ progress bar update ------
	        publishProgress(100); //------ progress bar update ------
	        return false;
		} catch (UnknownHostException e) {
			responseHandler.setFailureReason(ServerRestResponseHandler.SERVER_NOT_REACHABLE);
			publishProgress(90); //------ progress bar update ------
	        publishProgress(100); //------ progress bar update ------
			return false;
		} catch (SocketException e) {
			responseHandler.setFailureReason(ServerRestResponseHandler.SERVER_NOT_REACHABLE);
			publishProgress(90); //------ progress bar update ------
	        publishProgress(100); //------ progress bar update ------
			return false;
		} catch (IOException e) {
			responseHandler.setFailureReason(ServerRestResponseHandler.SERVER_NOT_REACHABLE);
			publishProgress(90); //------ progress bar update ------
	        publishProgress(100); //------ progress bar update ------
	        return false;
		}
		publishProgress(70); //------ progress bar update ------
		publishProgress(80); //------ progress bar update ------
		boolean successOrNot = responseHandler.handleResponse(response);

		return Boolean.valueOf(successOrNot);
	}

	@Override
    protected void onPreExecute() {
      super.onPreExecute();
      serverActivity.getProgressBar().setVisibility(View.VISIBLE);
    }

	@Override
	protected void onProgressUpdate(Integer... progress) {
		 super.onProgressUpdate(progress[0]);
		 serverActivity.getProgressBar().setProgress(progress[0]);
	}

	@Override
    protected void onPostExecute(Boolean successOrNot) {
		responseHandler.postHandling(serverActivity, successOrNot);
		serverActivity.getProgressBar().setVisibility( View.INVISIBLE);
    	serverActivity.getProgressBar().setProgress(0);
    }
}