conn = new Mongo();
db = conn.getDB('gamification-climb');

var modifiedConceptObjs = [];
var conceptTypes = [];
var transformedObject = {};

print("editing " + db.playerState.find().count()+ " records inside playerState collection");

db.playerState
		.find()
		.forEach(
				function(pState) {

					var existingConcept = pState.concepts;

					var modifiedObj = {};
					modifiedObj["BadgeCollectionConcept"] = {};
					modifiedObj["PointConcept"] = {};
					modifiedObj["ChallengeConcept"] = {};

					var modifiedConcept = {};

					// transform loop.
					existingConcept
							.forEach(function(concept) {

								var conceptKey = concept.type.substring(
										concept.type.lastIndexOf(".") + 1,
										concept.type.length);
								var nameKey = concept.obj.name;
								var objAttributes = {
									"_class" : "eu.trentorise.game.repo.GenericObjectPersistence",
									"obj" : concept.obj,
									"type" : concept.type
								};

								if (conceptKey == "BadgeCollectionConcept") {
									modifiedObj["BadgeCollectionConcept"][nameKey] = objAttributes;
								} else if (conceptKey == "PointConcept") {
									modifiedObj["PointConcept"][nameKey] = objAttributes;
								} else if (conceptKey == "ChallengeConcept") {
									modifiedObj["ChallengeConcept"][nameKey] = objAttributes;
								}

							});

					var objectId = pState._id.toString();
					modifiedConcept["id"] = pState._id;
					modifiedConcept["concepts"] = modifiedObj;

					if (modifiedConceptObjs.indexOf(modifiedConcept) < 0)
						modifiedConceptObjs.push(modifiedConcept);

				});

print("transformed " + modifiedConceptObjs.length + " records are being saved in playerState collection");

/** update collection. * */
modifiedConceptObjs.forEach(function(modifyConcept) {

	var objectId = modifyConcept["id"];

	db.playerState.update({
		_id : objectId
	}, {
		$set : {
			"concepts" : modifyConcept.concepts
		}
	});

});