/** STEP 2 - MIGRATION SCRIPT INSTANCES* */
conn = new Mongo();
db = conn.getDB('gamification-climb');

var possiblePeriods = [];
var modifiedConceptObjs = [];

db.game
		.find()
		.forEach(
				function(g) {

					var concepts = g.concepts;

					if (concepts) {

						concepts
								.forEach(function(c) {
									if (c.type == "eu.trentorise.game.model.PointConcept") {
										var periods = c.obj.periods;

										if (periods != undefined
												&& periods["weekly"] != undefined) {
											var instances = periods["weekly"].instances;
											var modifiedInstances = {};

											instances
													.forEach(function(
															oldInstance) {

														var d = new Date(
																oldInstance.start);

														var newKey = d
																.getFullYear("YYYY")
																+ "-"
																+ addZeroBefore(d
																		.getMonth() + 1)
																+ "-"
																+ addZeroBefore(d
																		.getDate())
																+ "T"
																+ addZeroBefore(d
																		.getHours())
																+ ":"
																+ addZeroBefore(d
																		.getMinutes())
																+ ":"
																+ addZeroBefore(d
																		.getSeconds());

														modifiedInstances[newKey] = {};

														if (oldInstance.score != undefined) {
															modifiedInstances[newKey]["score"] = oldInstance.score;
														}

														if (oldInstance.index != undefined) {
															modifiedInstances[newKey]["index"] = new NumberInt(oldInstance.index);
														}

														if (oldInstance.end != undefined) {
															modifiedInstances[newKey]["end"] = oldInstance.end;
														}

														modifiedInstances[newKey]["start"] = oldInstance.start;

													});

											periods["weekly"].instances = modifiedInstances;
										}
									}

								});

					}

					modifiedConcept = {};
					modifiedConcept["id"] = g._id;
					modifiedConcept["concepts"] = concepts;
					if (modifiedConceptObjs.indexOf(modifiedConcept) < 0)
						modifiedConceptObjs.push(modifiedConcept);

				});

/** update collection. * */
modifiedConceptObjs.forEach(function(modifyConcept) {

	var objectId = modifyConcept["id"];

	db.game.update({
		_id : objectId
	}, {
		$set : {
			"concepts" : modifyConcept.concepts
		}
	});

});

function addZeroBefore(n) {
	return (n < 10 ? '0' : '') + n;
}

possiblePeriods.forEach(function(period) {
	print(period)
});
