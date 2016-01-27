package org.alopex.ragtag.net.api;

import java.util.ArrayList;

import org.alopex.ragtag.core.RagCore;
import org.alopex.ragtag.core.SysRes;
import org.alopex.ragtag.module.Job;
import org.alopex.ragtag.net.worker.WorkerManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Specific REST hook for evaluation input
 *
 * @author Kevin Cai
 */
public class Bridge extends ServerResource {

	public void setHeaders() {
		this.getResponse().setAccessControlAllowOrigin("*");
	}

	@Post("application/text")
	public String process(JsonRepresentation entity) {
		JSONObject json = null;
		JSONObject responseJSON = new JSONObject();
		String consoleOutput = "";
		try {
			json = entity.getJsonObject();
			if(json.length() > 0) {
				Object oMethodCall = json.get("rpc");
				if(oMethodCall instanceof String) {
					String methodCall = (String) oMethodCall;

					switch(methodCall) {
						case "exec_init":
							try {
								String fileName = json.getString("bin");
								JSONArray dataSet = (JSONArray) json.get("data");

								ArrayList<String> listdata = new ArrayList<String>();
								if(dataSet != null) {
									for(int i = 0; i < dataSet.length(); i++) {
										listdata.add(dataSet.get(i).toString());
									}
								}

								boolean attempt = false;
								if(listdata.size() > 0) {
									attempt = RagCore.attemptExec(fileName, listdata);
									responseJSON.put("value", attempt ? "success" : "fail");
								} else {
									responseJSON.put("value", "fail");
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							break;

						case "num_workers":
							responseJSON.put("value", WorkerManager.getWorkers().size());
							break;

						case "num_jobs":
							responseJSON.put("value", Job.getJobs().size());
							break;

						case "disk_space":
							responseJSON.put("value", SysRes.diskSpace());
							break;

						case "load":
							responseJSON.put("value", SysRes.load() + "");
							break;

						default:
							responseJSON.put("error", "unknown_rpc_invocation");
							break;
					}
				}
			} else {
				responseJSON.put("error", "empty_params");
			}
		} catch (Exception e) {
			try {
				responseJSON.put("error", "no_rpc_defined");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		consoleOutput = responseJSON.toString();
		setHeaders();
		return consoleOutput;
	}

	@Get
	public String toString() {
		return "INVALID: API GET ACCESS DISALLOWED";
	}
}