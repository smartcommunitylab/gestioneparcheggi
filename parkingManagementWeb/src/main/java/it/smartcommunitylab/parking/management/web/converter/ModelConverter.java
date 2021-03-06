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
package it.smartcommunitylab.parking.management.web.converter;

import it.smartcommunitylab.parking.management.web.bean.LineBean;
import it.smartcommunitylab.parking.management.web.bean.StreetBean;
import it.smartcommunitylab.parking.management.web.bean.ZoneBean;
import it.smartcommunitylab.parking.management.web.model.ParkingStructure.PaymentMode;
import it.smartcommunitylab.parking.management.web.model.RateArea;
import it.smartcommunitylab.parking.management.web.model.Street;
import it.smartcommunitylab.parking.management.web.model.Zone;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

public class ModelConverter {

	private static final Logger logger = Logger.getLogger(ModelConverter.class);
	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.configure(
				org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
	}

	public static <T> T convert(Object o, Class<T> javaClass) {
		return mapper.convertValue(o, javaClass);
	}

	public static String fromPaymentMode(PaymentMode mode) {
		return mode.toString();
	}

	public static List<String> fromPaymentMode(List<PaymentMode> modes) {
		List<String> result = new ArrayList<String>();
		for (PaymentMode mode : modes) {
			result.add(fromPaymentMode(mode));
		}
		return result;
	}

	public static PaymentMode toPaymentMode(String mode) {
		try {
			return PaymentMode.valueOf(mode);
		} catch (IllegalArgumentException e) {
			logger.error(String.format("%s not present in PaymentMode enum",
					mode));
			return null;
		}
	}

	public static List<PaymentMode> toPaymentMode(List<String> modes) {
		List<PaymentMode> result = new ArrayList<PaymentMode>();
		for (String mode : modes) {
			PaymentMode m = toPaymentMode(mode);
			if (m != null) {
				result.add(m);
			}
		}
		return result;
	}
	
	/**
	 * Method toStreetBean: used to transform a Street object in a StreetBean object
	 * @param area
	 * @param s
	 * @return
	 */
	public static StreetBean toStreetBean(RateArea area, Street s){
		StreetBean sb = new StreetBean();
		sb.setId(s.getId());
		sb.setId_app(s.getId_app());
		sb.setStreetReference(s.getStreetReference());
		sb.setSlotNumber(s.getSlotNumber());
		sb.setFreeParkSlotNumber(s.getFreeParkSlotNumber());
		sb.setFreeParkSlotOccupied(s.getFreeParkSlotOccupied());
		sb.setFreeParkSlotSignNumber(s.getFreeParkSlotSignNumber());
		sb.setFreeParkSlotSignOccupied(s.getFreeParkSlotSignOccupied());
		sb.setHandicappedSlotNumber(s.getHandicappedSlotNumber());
		sb.setHandicappedSlotOccupied(s.getHandicappedSlotOccupied());
		sb.setPaidSlotNumber(s.getPaidSlotNumber());
		sb.setPaidSlotOccupied(s.getPaidSlotOccupied());
		sb.setTimedParkSlotNumber(s.getTimedParkSlotNumber());
		sb.setTimedParkSlotOccupied(s.getTimedParkSlotOccupied());
		sb.setUnusuableSlotNumber(s.getUnusuableSlotNumber());
		sb.setLastChange(s.getLastChange());
		sb.setRateAreaId(s.getRateAreaId());
		sb.setColor(area.getColor());
		sb.setGeometry(convert(s.getGeometry(), LineBean.class));
		sb.setSubscritionAllowedPark(s.isSubscritionAllowedPark());
		//List<Zone> zones = s.getZones();
		//List<ZoneBean> zoneBeans = new ArrayList<ZoneBean>();
		//for(Zone z : zones){
		//	ZoneBean zon = convert(z, ZoneBean.class);
		//	zoneBeans.add(zon);
		//}
		//sb.setZoneBeans(zoneBeans);
		sb.setZones(s.getZones());
		sb.setParkingMeters(s.getParkingMeters());
		return sb;
	}

}