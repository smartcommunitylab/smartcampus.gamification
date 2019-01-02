package it.smartcommunitylab.model.ext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.smartcommunitylab.model.Notification;

public class NotificationControllerUtils {
	
	ObjectMapper mapper = new ObjectMapper();
	
    public List<Notification> convertToSpecificNotificationType(Map[] objs) throws IllegalArgumentException, ClassNotFoundException {
    	List<Notification> result = new ArrayList<>();
    	for (Map notif: objs) {
        	result.add((Notification) mapper.convertValue(notif, Class.forName("it.smartcommunitylab.model.ext." + notif.get("type"))));
        }
    	return result;
    	
    }
    
	public Map<String, Collection<Notification>> convertToSpecificGroupNotification(Map map)
			throws IllegalArgumentException, ClassNotFoundException {
		Map<String, Collection<Notification>> result = new HashMap<String, Collection<Notification>>();

		Iterator entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String) entry.getKey();
			List<Map> objs = (List<Map>) entry.getValue();
			
			Collection<Notification> notifyTypeList = result.get(key);
			if (notifyTypeList == null) {
				notifyTypeList = new ArrayList<>();
				result.put(key, notifyTypeList);
			}

			for (Map value: objs) {
				notifyTypeList.add(
					(Notification) mapper.convertValue(value, Class.forName("it.smartcommunitylab.model.ext." + key)));
				
			}

		}

		return result;
	}

}
