/*******************************************************************************
 * Copyright 2015 Fondazione Bruno Kessler
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package it.smartcommunitylab.parking.management.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class WsProxyController {
	
	private static transient final Logger logger = Logger.getLogger(WsProxyController.class);

	@Autowired
	@Value("${smartcommunity.urlws.parking}")
	private String parkingUrl;
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/rest/allGet")
	public @ResponseBody
	String getAll(HttpServletRequest request, @RequestParam String urlWS){
		RestTemplate restTemplate = new RestTemplate();
		logger.error("WS-GET. Method " + urlWS);	//Added for log ws calls info in preliminary phase of portal
		
		String result = "";
		try {
			result = restTemplate.getForObject(parkingUrl + urlWS, String.class);
		} catch (Exception ex){
			logger.error(String.format("Exception in proxyController get ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
			//restTemplate.getErrorHandler();
		}
		
		return result;	
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/rest/allPost")
	public @ResponseBody
	String postAll(HttpServletRequest request, @RequestParam String urlWS, @RequestBody Map<String, Object> data){
		RestTemplate restTemplate = new RestTemplate();
		logger.error("WS-POST. Method " + urlWS + ". Passed data : " + data); //Added for log ws calls info in preliminary phase of portal
		
		String result = "";
		try {
			result = restTemplate.postForObject(parkingUrl + urlWS, data, String.class);
			if(urlWS.compareTo("GetPDF") == 0 && (result.contains("error") || result.contains("exception"))){
				logger.error("WS-POST. Method " + urlWS + ". Error : " + result);
			}
		} catch (Exception ex){
			logger.error(String.format("Exception in proxyController post ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
			//restTemplate.getErrorHandler();
		}
		
		return result;	
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/rest/allPut")
	public @ResponseBody
	String putAll(HttpServletRequest request, @RequestParam String urlWS, @RequestBody Map<String, Object> data){
		RestTemplate restTemplate = new RestTemplate();
		logger.error("WS-PUT. Method " + urlWS + ". Passed data : " + data); //Added for log ws calls info in preliminary phase of portal
		
		String result = "";
		try {
			restTemplate.put(parkingUrl + urlWS, data);
			result = "OK";
		} catch (Exception ex){
			logger.error(String.format("Exception in proxyController put ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
			//restTemplate.getErrorHandler();
		}
		
		return result;	
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/rest/allDelete")
	public @ResponseBody
	String deAll(HttpServletRequest request, @RequestParam String urlWS){
		RestTemplate restTemplate = new RestTemplate();
		logger.error("WS-DELETE. Method " + urlWS + "."); //Added for log ws calls info in preliminary phase of portal
		
		String result = "";
		try {
			restTemplate.delete(parkingUrl + urlWS);
			result = "OK";
		} catch (Exception ex){
			logger.error(String.format("Exception in proxyController delete ws. Method: %s. Details: %s", urlWS, ex.getMessage()));
			//restTemplate.getErrorHandler();
		}
		
		return result;	
	}
	
}
