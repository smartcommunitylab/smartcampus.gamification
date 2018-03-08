## Usage

1. `npm install`
2. Set configuration in `app.js`

```
const dbSource = 'mongodb://localhost:27017';
const dbTarget = 'mongodb://localhost:27017';

const dbNameSource = 'gamification';
const dbNameTarget = 'gamification';

const gameSource = 'GAME_ID';
const startDate = Long.fromNumber('START_DATE_TIMESTAMP');
```

3. Run as `npm start`
