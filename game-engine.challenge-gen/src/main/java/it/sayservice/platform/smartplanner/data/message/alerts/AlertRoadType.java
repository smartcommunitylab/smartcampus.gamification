package it.sayservice.platform.smartplanner.data.message.alerts;

public enum AlertRoadType {

	ROAD_BLOCK, PARKING_BLOCK, DRIVE_CHANGE, OTHER;
	
	public static AlertRoadType getAlertRoadTypeType (String type) {
		
		if (type.equalsIgnoreCase("road_block"))
			return ROAD_BLOCK;
		else if (type.equalsIgnoreCase("parking_block"))
			return PARKING_BLOCK;
		else if (type.equalsIgnoreCase("drive_change"))
			return DRIVE_CHANGE;
		
		return OTHER;
		}
	
}
