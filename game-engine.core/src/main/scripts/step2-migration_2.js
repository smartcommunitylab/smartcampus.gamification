/** STEP 2 - MIGRATION SCRIPT INSTANCES* */

/**
 * run as mongo NOME_DB step2-migration_2.js
 * 
 * */

var possiblePeriods = [];
var modifiedConceptObjs = [];

db.playerState
		.find()
		.forEach(
				function(pState) {

					var concepts = pState.concepts;

					var pointConceptMap = concepts["PointConcept"];

					if (pointConceptMap) {

						for ( var i in pointConceptMap) {
							var periods = pointConceptMap[i].obj.periods;

							if (periods != undefined
									&& periods["weekly"] != undefined) {
								var instances = periods["weekly"].instances;
								var modifiedInstances = {};

								/** make transformation * */
								instances
										.forEach(function(oldInstance) {

											var d = new Date(oldInstance.start);

											var newKey = d.getFullYear("YYYY")
													+ "-"
													+ addZeroBefore(d
															.getMonth() + 1)
													+ "-"
													+ addZeroBefore(d.getDate())
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

								/** update transformation. * */
								periods["weekly"].instances = modifiedInstances;
							}

						}

					}

					modifiedConcept = {};
					modifiedConcept["id"] = pState._id;
					modifiedConcept["concepts"] = concepts;
					// print(JSON.stringify(concepts));
					if (modifiedConceptObjs.indexOf(modifiedConcept) < 0)
						modifiedConceptObjs.push(modifiedConcept);

				});

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

function addZeroBefore(n) {
	return (n < 10 ? '0' : '') + n;
}

possiblePeriods.forEach(function(period) {
	print(period)
});
