package org.alopex.ragtag.net.api;

import org.alopex.ragtag.net.worker.WorkerManager;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Specific REST hook for evaluation input
 * @author Kevin Cai
 *
 */
public class Bridge extends ServerResource {
	
	public static String consoleOutput = "";

	@Post("application/text")
	public String process(JsonRepresentation entity) {
		JSONObject json = null;			
		try {
			json = entity.getJsonObject();

			if(json.length() > 0) {
				Object oMethodCall = json.get("rpc");
				if(oMethodCall instanceof String) {
					String methodCall = (String) oMethodCall;
					
					JSONObject responseJSON = new JSONObject();
					switch(methodCall) {
						case "num_workers":
							responseJSON.put("num_workers", WorkerManager.getWorkers().size());
							break;
							
						default:
							responseJSON.put("error", "unknown_rpc_invocation");
							break;
					}
					
					consoleOutput = responseJSON.toString();
				}
				
				return consoleOutput;
			} else {
				return "{\"error\":\"empty_params\"}";
			}
		} catch (Exception e) {
			return "{\"error\":\"no_rpc_defined\"}";
		}
	}
	
	@Get
	public String toString() {
		return "INVALID: API GET ACCESS DISALLOWED";
	}
}