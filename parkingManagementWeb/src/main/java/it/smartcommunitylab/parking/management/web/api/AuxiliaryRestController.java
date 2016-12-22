package it.smartcommunitylab.parking.management.web.api;

import it.smartcommunitylab.parking.management.web.auxiliary.controller.ObjectController;
import it.smartcommunitylab.parking.management.web.auxiliary.data.GeoObjectManager;
import it.smartcommunitylab.parking.management.web.auxiliary.model.ParkMeter;
import it.smartcommunitylab.parking.management.web.auxiliary.model.ParkStruct;
import it.smartcommunitylab.parking.management.web.auxiliary.model.Parking;
import it.smartcommunitylab.parking.management.web.auxiliary.model.Street;
import it.smartcommunitylab.parking.management.web.bean.DataLogBean;
import it.smartcommunitylab.parking.management.web.bean.RateAreaBean;
import it.smartcommunitylab.parking.management.web.exception.NotFoundException;
import it.smartcommunitylab.parking.management.web.manager.CSVManager;
import it.smartcommunitylab.parking.management.web.manager.StorageManager;
import it.smartcommunitylab.parking.management.web.model.UserSetting;
import it.smartcommunitylab.parking.management.web.security.YamlUserDetailsService;
import it.smartcommunitylab.parking.management.web.utils.AgencyDataSetup;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuxiliaryRestController {

private static final Logger logger = Logger.getLogger(ObjectController.class);
	
	private static final int NO_PERIOD = -1;
	private static final int DEFAULT_COUNT = 500;	// last 500 values
	
	@Autowired
	private GeoObjectManager dataService;
	
	@Autowired
	StorageManager storage;
	
	@Autowired
	private YamlUserDetailsService yamlUserDetailsService;
	
	@Autowired
	private AgencyDataSetup agencyDataSetup;
	
	@Autowired
	CSVManager csvManager;
	
	@RequestMapping(method = RequestMethod.GET, value = "/data-mgt/ping") 
	public @ResponseBody String ping(@RequestHeader(value="Authorization", required=true) String authorization) {
		return "pong";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/data-mgt/{agency}/streets")
	public @ResponseBody List<Street> getStreets(Principal principal, @PathVariable String agency, @RequestParam(required=false) String agencyId, 
		@RequestParam(required=false) Double lat, @RequestParam(required=false) Double lon, @RequestParam(required=false) Double radius, @RequestHeader(value="Authorization", required=true) String authorization) throws Exception {
		String uname = principal.getName();
		UserSetting user = yamlUserDetailsService.getUserDetails(uname);
		Map<String, String> userAgencyData = agencyDataSetup.getAgencyMap(agencyDataSetup.getAgencyById(user.getAgency()));
		if(!StringUtils.hasText(agencyId)){
			agencyId = userAgencyData.get("id");
		}
		if (lat != null && lon != null && radius != null) {
			return dataService.getStreets(agency, lat, lon, radius, agencyId);
		} 
		return dataService.getStreets(agency, agencyId);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/data-mgt/{agency}/parkings") 
	public @ResponseBody List<Parking> getParkings(Principal principal, @PathVariable String agency, @RequestParam(required=false) String agencyId, @RequestParam(required=false) Double lat, @RequestParam(required=false) Double lon, @RequestParam(required=false) Double radius, @RequestHeader(value="Authorization", required=true) String authorization) throws Exception {
		String uname = principal.getName();
		UserSetting user = yamlUserDetailsService.getUserDetails(uname);
		Map<String, String> userAgencyData = agencyDataSetup.getAgencyMap(agencyDataSetup.getAgencyById(user.getAgency()));
		if(!StringUtils.hasText(agencyId)){
			agencyId = userAgencyData.get("id");
		}
		if (lat != null && lon != null && radius != null) {
			return dataService.getParkings(agency, lat, lon, radius, agencyId);
		} 
		return dataService.getParkings(agency, agencyId);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/data-mgt/{agency}/parkingmeters") 
	public @ResponseBody List<ParkMeter> getParkingMeters(Principal principal, @PathVariable String agency, @RequestParam(required=false) String agencyId, @RequestParam(required=false) Double lat, @RequestParam(required=false) Double lon, @RequestParam(required=false) Double radius, @RequestHeader(value="Authorization", required=true) String authorization) throws Exception {
		String uname = principal.getName();
		UserSetting user = yamlUserDetailsService.getUserDetails(uname);
		Map<String, String> userAgencyData = agencyDataSetup.getAgencyMap(agencyDataSetup.getAgencyById(user.getAgency()));
		if(!StringUtils.hasText(agencyId)){
			agencyId = userAgencyData.get("id");
		}
		if (lat != null && lon != null && radius != null) {
			return dataService.getParkingMeters(agency, lat, lon, radius, agencyId);
		} 
		return dataService.getParkingMeters(agency, agencyId);
	}
	
	private String retrieveTypeFromDesc(String desc){
		String corrType = "";
		if(desc.compareTo("street") == 0){					// street occupancy case
			corrType = Street.class.getCanonicalName();
		} else if(desc.compareTo("parking") == 0){			// parking structure occupancy case
			corrType = Parking.class.getCanonicalName();
		} else if(desc.compareTo("parkingmeter") == 0){		// parking meter profit case
			corrType = ParkMeter.class.getCanonicalName();
		} else if(desc.compareTo("parkstruct") == 0){		// parking structure profit case
			corrType = ParkStruct.class.getCanonicalName();
		}
		
		return corrType;
	}
	
	private String retrieveDescFromType(String desc){
		String corrType = "street";		// default case (if no type passed)
		if(desc.compareTo(Street.class.getCanonicalName()) == 0){				// street occupancy case
			corrType = "street";
		} else if(desc.compareTo(Parking.class.getCanonicalName()) == 0){		// parking structure occupancy case
			corrType = "parking";
		} else if(desc.compareTo(ParkMeter.class.getCanonicalName()) == 0){		// parking meter profit case
			corrType = "parkingmeter";
		} else if(desc.compareTo(ParkStruct.class.getCanonicalName()) == 0){	// parking structure profit case
			corrType = "parkstruct";
		}
		
		return corrType;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/data-mgt/{agency}/logs") 
	public @ResponseBody Iterable<DataLogBean> getAllLogs(Principal principal, @PathVariable String agency, 
			@RequestParam(required=false) String id, @RequestParam(required=false) String type, @RequestParam(required=false) String author, @RequestParam(required=false)String mode,
			@RequestParam(required=false) Integer count, @RequestParam(required=false) Integer skip, @RequestHeader(value="Authorization", required=true) String authorization) {
		if (count == null) count = DEFAULT_COUNT;
		if (skip == null) skip = 0;
		String uname = principal.getName();
		UserSetting user = yamlUserDetailsService.getUserDetails(uname);
		Map<String, String> userAgencyData = agencyDataSetup.getAgencyMap(agencyDataSetup.getAgencyById(user.getAgency()));
		String userAgency = userAgencyData.get("id");
		if(StringUtils.hasText(type)){
			if(!type.contains(".")){
				type = retrieveTypeFromDesc(type);
			}
		}
		if(StringUtils.hasText(id)){
			if(!id.contains("@")){
				id = retrieveDescFromType(type) + "@" + agency + "@" + id;
			}
		}
		if(StringUtils.hasText(mode) && mode.compareTo("object") == 0){
			return dataService.findAllLogsObjects(id, agency, userAgency, type, author, skip, count);
		} else {
			return dataService.findAllLogs(id, agency, userAgency, type, author, skip, count);
		}
	}
	
	
	
	// Method open to get all area objects
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/data-mgt/{appId}/area")
	public @ResponseBody
	List<RateAreaBean> getRateAreaDatas(Principal principal, @PathVariable("appId") String appId, @RequestParam(required=false) String agencyId, @RequestHeader(value="Authorization", required=true) String authorization) {
		String uname = principal.getName();
		UserSetting user = yamlUserDetailsService.getUserDetails(uname);
		Map<String, String> userAgencyData = agencyDataSetup.getAgencyMap(agencyDataSetup.getAgencyById(user.getAgency()));
		if(agencyId == null || agencyId.compareTo("") == 0){
			agencyId = userAgencyData.get("id");
		}
		return storage.getAllAreaByAgencyId(appId, agencyId);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value = "/data-mgt/{agency}/parkings/{id}") 
	public @ResponseBody String updateParking(Principal principal, @RequestBody Parking parking,
		@RequestParam(required=false) String userAgencyId, @RequestParam(required=false) boolean isSysLog, 
		@RequestParam(required=false) String username, @RequestParam(required=false) long[] period, 
		@PathVariable String agency, @PathVariable String id, @RequestHeader(value="Authorization", required=true) String authorization) throws Exception, NotFoundException {
		String channelId = "1";	// mobile app mode
		String author = parking.getAuthor();
		String uname = principal.getName();
		UserSetting user = yamlUserDetailsService.getUserDetails(uname);
		Map<String, String> userAgencyData = agencyDataSetup.getAgencyMap(agencyDataSetup.getAgencyById(user.getAgency()));
		if(userAgencyId == null || userAgencyId.compareTo("") == 0){
			userAgencyId = userAgencyData.get("id");
			username = uname;
		}
		try {
			dataService.updateDynamicParkingData(parking, agency, channelId, userAgencyId, isSysLog, username, author, period, NO_PERIOD);
			return "OK";
		} catch (it.smartcommunitylab.parking.management.web.exception.NotFoundException e) {
			e.printStackTrace();
			return "KO";
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value = "/data-mgt/{agency}/streets/{id}") 
	public @ResponseBody String updateStreet(Principal principal, @RequestBody Street street,
		@RequestParam(required=false) String userAgencyId, @RequestParam(required=false) boolean isSysLog, 
		@RequestParam(required=false) String username, @RequestParam(required=false) long[] period, 
		@PathVariable String agency, @PathVariable String id, @RequestHeader(value="Authorization", required=true) String authorization) throws Exception, NotFoundException {
		String channelId = "1";	// mobile app mode
		String author = street.getAuthor();
		String uname = principal.getName();
		if(street.getAreaId() == null){
			street.setAgency(agency);
		}
		UserSetting user = yamlUserDetailsService.getUserDetails(uname);
		Map<String, String> userAgencyData = agencyDataSetup.getAgencyMap(agencyDataSetup.getAgencyById(user.getAgency()));
		if(userAgencyId == null || userAgencyId.compareTo("") == 0){
			userAgencyId = userAgencyData.get("id");
			username = uname;
		}
		try {
			if(period != null){
				logger.debug("Inserted period = " + period[0] + "-" + period[1] );
			}
			dataService.updateDynamicStreetData(street, agency, channelId, userAgencyId, isSysLog, username, author, period, NO_PERIOD);
			return "OK";
		} catch (it.smartcommunitylab.parking.management.web.exception.NotFoundException e) {
			logger.error("Exception in street occupancy log insert: " + e.getMessage());
			return "KO";
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value = "/data-mgt/{agency}/parkingmeters/{id}") 
	public @ResponseBody String updateParkingMeter(Principal principal, @RequestBody ParkMeter parkingMeter, 
		@RequestParam(required=false) String userAgencyId, @RequestParam(required=false) boolean isSysLog, 
		@RequestParam(required=false) String username, @RequestParam(required=false) long[] period, 
		@RequestParam(required=false) Long from, @RequestParam(required=false) Long to, @PathVariable String agency, 
		@PathVariable String id, @RequestHeader(value="Authorization", required=true) String authorization) throws Exception, NotFoundException {
		String channelId = "1";	// mobile app mode
		String author = parkingMeter.getAuthor();
		String uname = principal.getName();
		UserSetting user = yamlUserDetailsService.getUserDetails(uname);
		Map<String, String> userAgencyData = agencyDataSetup.getAgencyMap(agencyDataSetup.getAgencyById(user.getAgency()));
		if(userAgencyId == null || userAgencyId.compareTo("") == 0){
			userAgencyId = userAgencyData.get("id");
			username = uname;
		}
		try {
			dataService.updateDynamicParkingMeterData(parkingMeter, agency, channelId, userAgencyId, isSysLog, username, author, from, to, period, NO_PERIOD);
			return "OK";
		} catch (it.smartcommunitylab.parking.management.web.exception.NotFoundException e) {
			logger.error("Exception in parking meter profit log insert: " + e.getMessage());
			return "KO";
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value = "/data-mgt/{agency}/parkstructprofit/{id}") 
	public @ResponseBody String updateParkStructProfitData(Principal principal, @RequestBody ParkStruct parkStruct, 
		@RequestParam(required=true) String userAgencyId, @RequestParam(required=false) boolean isSysLog, 
		@RequestParam(required=false) String username, @RequestParam(required=false) long[] period, 
		@RequestParam(required=false) Long from, @RequestParam(required=false) Long to, @PathVariable String agency, 
		@PathVariable String id, @RequestHeader(value="Authorization", required=true) String authorization) throws Exception, NotFoundException {
		String channelId = "1";	// mobile app mode
		String author = parkStruct.getAuthor();
		String uname = principal.getName();
		UserSetting user = yamlUserDetailsService.getUserDetails(uname);
		Map<String, String> userAgencyData = agencyDataSetup.getAgencyMap(agencyDataSetup.getAgencyById(user.getAgency()));
		if(userAgencyId == null || userAgencyId.compareTo("") == 0){
			userAgencyId = userAgencyData.get("id");
			username = uname;
		}
		try {
			dataService.updateDynamicParkStructProfitData(parkStruct, agency, channelId, userAgencyId, isSysLog, username, author, from, to, period, NO_PERIOD);
			return "OK";
		} catch (it.smartcommunitylab.parking.management.web.exception.NotFoundException e) {
			logger.error("Exception in parking structure profit log insert: " + e.getMessage());
			return "KO";
		}
	}

}
