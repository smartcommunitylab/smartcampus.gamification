function XMLParser(xml) {
    if (typeof xml == "string") {
        xml = DOMParser ? (new DOMParser()).parseFromString(xml, "text/xml") : ((new ActiveXObject("Microsoft.XMLDOM")).loadXML(xml));
    }
    
    function nodesToJSONObject(nodes, parent) {
        for (var n = 0; n < nodes.length; n++) {
            if (nodes[n].nodeType == 7) {
                continue;
            }

            var tmp = {};
            var nodeName = nodes[n].nodeName;
            
            if (nodes[n].nodeType == 3 || nodes[n].nodeType == 4) {              
                tmp.text = "";
                if (nodes[n].childNodes.length > 0) {
                    for (var i = 0; i < nodes[n].childNodes.length; i++) {
                        tmp.text += nodes[n].childNodes[i].nodeValue;
                    }
                } else {
                    tmp.text = nodes[n].nodeValue;
                }
                tmp["@attributes"] = attributesToJSONObject(nodes[n].attributes);
                //tmp.toString = function() {return parent.text};
                return tmp;
            } else {
                tmp = nodesToJSONObject(nodes[n].childNodes, {});
                tmp["@attributes"] = attributesToJSONObject(nodes[n].attributes);
            }
            
            if (!(nodeName in parent)) {
                parent[nodeName] = tmp;
            } else {
                if (!(parent[nodeName] instanceof Array)) {
                    parent[nodeName] = [parent[nodeName]];            
                }
                parent[nodeName].push(tmp);
            }
        }

        return parent;
    }
    
    function attributesToJSONObject(attributes) {
        if (!attributes) {return {}}

        var attribs = {};
        for (var i = 0; i < attributes.length; i++) {
            attribs[attributes[i].nodeName] = attributes[i].nodeValue;
        }
        return attribs;
    }

    this.toJSON = function () {
        return nodesToJSONObject(xml.childNodes, {});
    };
}