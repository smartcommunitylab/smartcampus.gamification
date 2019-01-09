/**
 * Copyright 2018-2019 SmartCommunity Lab(FBK-ICT).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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

	public List<Notification> convertToSpecificNotificationType(Map[] objs)
			throws IllegalArgumentException, ClassNotFoundException {
		List<Notification> result = new ArrayList<>();
		for (Map notif : objs) {
			result.add((Notification) mapper.convertValue(notif,
					Class.forName("it.smartcommunitylab.model.ext." + notif.get("type"))));
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

			for (Map value : objs) {
				notifyTypeList.add((Notification) mapper.convertValue(value,
						Class.forName("it.smartcommunitylab.model.ext." + key)));

			}

		}

		return result;
	}

}
