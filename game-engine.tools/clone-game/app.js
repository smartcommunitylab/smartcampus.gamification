const mongodb = require('mongodb');
const logger = require('winston');

const MongoClient = mongodb.MongoClient;
const ObjectId = mongodb.ObjectId;
const Long = mongodb.Long;

// example: mongodb://USER:PASSWORD@HOST:PORT/DBNAME?authSource=AUTHDB
const dbSource = 'mongodb://localhost:27017';
const dbTarget = 'mongodb://localhost:27017';

const dbNameSource = 'gamification';
const dbNameTarget = 'gamification';

const gameSource = 'GAME_ID';
const startDate = Long.fromNumber('START_DATE_TIMESTAMP');



let gameCloned;

MongoClient.connect(dbSource, (err, client) => {
  // connect to db source + game source
  // read the game instance
   const db = client.db(dbNameSource);
   
   const gameCollection = db.collection('game');
   
   gameCollection.findOne({_id: ObjectId(gameSource)}).then( game => {
    logger.info(`Source game ${game.name} - ${game._id}`);
    gameCloned = Object.assign({},game);

    gameCloned._id = undefined;
    gameCloned.tasks = [];
    gameCloned.rules = [];
    
    const newConcepts = newStartPeriods(getPointConcepts(game.concepts), startDate);
    gameCloned.concepts = newConcepts.concat(game.concepts.filter( c => c.type !== "eu.trentorise.game.model.PointConcept"));
    
    // connect to db target
    MongoClient.connect(dbTarget, (err, client) => {
       const db = client.db(dbNameTarget);
       
       const gameCollection = db.collection('game');
       
       gameCollection.insertOne(gameCloned, function(err, result) {
        if(!err) {
          logger.info(`Saved cloned game into ${dbTarget}/${dbNameTarget} with id ${result.insertedId}`);
          cloneRules({url: dbSource, name: dbNameSource}, gameSource, result.insertedId);
        } else {
          logger.info(err);
        }
      });
     
      client.close();
   });
   
   client.close();
   
   }).catch( error => {
    logger.info('error ', error);
    client.close();
   });
});
  
const cloneRules = (dbRef, gameSourceId, gameTargetId) => {
  MongoClient.connect(dbRef.url, (err, client) => {
  const db = client.db(dbRef.name);
  const rulesCollection = db.collection('rule');
  rulesCollection.find({gameId: gameSourceId}).each(function(err, rule) {
      if(rule) {
        rule._id = undefined;
        rule.gameId = gameTargetId.toHexString();
        pushRulesToTarget({url: dbTarget, name: dbNameTarget}, rule);
      } else {
        client.close();
      }
    });
});
};


const pushRulesToTarget = (dbRef, rule) => {
 MongoClient.connect(dbRef.url, (err, client) => {
  const db = client.db(dbRef.name);
  const ruleCollection = db.collection('rule');
  ruleCollection.insertOne(rule, function(err,result) {
    if(!err) {
      logger.info(`Saved cloned rule with name ${rule.name} into game ${rule.gameId} db ${dbRef.url}/${dbRef.name} with id ${result.insertedId}`);
      updateRule(dbRef, rule.gameId, result.insertedId);
    } else {
      logger.error(err);
    }
  });
  client.close();
 });
};


const updateRule = (dbRef, gameTargetId, ruleId) => {
  MongoClient.connect(dbRef.url, (err, client) => {
  const db = client.db(dbRef.name);
  const gameCollection = db.collection('game');
  gameCollection.findOneAndUpdate({_id: ObjectId(gameTargetId)}, { $push: { rules: `db://${ruleId}` } }).then( game => {
      logger.info(`Pushed ${ruleId} to game ${gameTargetId}`)
  }).catch( error => {
      logger.error(error);
  });
  client.close();
  });  
};
const getPointConcepts = concepts => {
  return concepts.filter(concept => concept.type === "eu.trentorise.game.model.PointConcept");
};


const newStartPeriods = (pointConcepts, startDate) => {
  return pointConcepts.map( point => {
    point.obj.periods = setStart(point.obj.periods,startDate);
    point.obj.score = new mongodb.Double(0);
    return point;
  });
};

const setStart = (periods, startDate) => {
  const newObj = Object.assign({}, periods);
  const periodNames = Object.keys(newObj);
  periodNames.forEach( periodName => {
    newObj[periodName].start = startDate;  
    newObj[periodName].period = Long.fromNumber(newObj[periodName].period);
  });
  return newObj;
};
