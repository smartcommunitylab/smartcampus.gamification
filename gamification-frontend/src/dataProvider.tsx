import simpleRestProvider from 'ra-data-simple-rest';

const apiUrl = process.env.REACT_APP_API_ENDPOINT + '/console-ui';
console.log("Endpoint ->" + apiUrl);
const dataProvider = simpleRestProvider(apiUrl);

const gamificationDataProvider = {
    ...dataProvider,
    getList: (resource: any, params: any) => {
        let url = `${apiUrl}/${resource}`;
        const token = localStorage.getItem('token');
        const headers = { 'Authorization': `Basic ${token}` };

        const { page, perPage } = params.pagination;
        const { field, order } = params.sort;

        if (resource !== "game") {
            const gameId = params.meta.gameId;
            url = url + '/' + gameId + "?&page=" + page + "&size=" + perPage + "&sort=" + field + "," + order;
        } else {
            url = url + "?&page=" + page + "&size=" + perPage + "&sort=" + field + "," + order;
        }

        return fetch(url, { method: 'GET', headers: headers })
            .then(async (response: any) => {
                const json = await response.json();
                if (json.error) {
                    const errorObj = { status: json.status, message: json.error + " - " + json.message };
                    throw errorObj;
                }
                return json;
            })
    },
    getOne: (resource: any, params: any) => {
        let url = `${apiUrl}/${resource}`;
        const token = localStorage.getItem('token');
        const headers = { 'Authorization': `Basic ${token}`, 'Content-type': 'application/json' };

        if (resource === "game") {
            const gameId = params.meta.gameId;
            url = url + '/' + gameId;
        } else {
            const gameId = params.meta.gameId;
            url = url + '/' + gameId + '/' + params.id;
        }

        return fetch(url, { method: 'GET', headers: headers })
            .then(async (response: any) => {
                let json = await response.json();
                if (json.error) {
                    const errorObj = { status: json.status, message: json.error + " - " + json.message };
                    throw errorObj;
                }
                // if (resource==='challengemodels') {
                //     json.data = modifyChallengeModel(json);
                // }
                if (resource==="pointconcepts") {
                    json.data = modifyPointConceptModel(json);
                }
                return json;
            })
    },
    create: (resource: any, params: any) => {
        let url = `${apiUrl}/${resource}`;
        let body;
        const token = localStorage.getItem('token');
        let headers;

        if (resource === 'actions') {
            headers = { 'Authorization': `Basic ${token}`, 'Content-type': 'application/json' };
            const gameId = params.meta.gameId;
            url = url + '/' + gameId + '/' + params.data.actionName;
        }
        if (resource === 'tasks') {
            headers = { 'Authorization': `Basic ${token}`, 'Content-type': 'application/json' };
            const gameId = params.meta.gameId;
            body = JSON.stringify(params.data);
            url = url + '/' + gameId + '/' + params.data.type;
        }
        if (resource === 'badges') {
            headers = { 'Authorization': `Basic ${token}`, 'Content-type': 'application/json' };
            const gameId = params.meta.gameId;
            body = JSON.stringify(params.data);
            url = url + '/' + gameId;
        }
        if (resource === 'rules') {
            headers = { 'Authorization': `Basic ${token}`, 'Content-type': 'application/json' };
            const gameId = params.meta.gameId;
            body = JSON.stringify(params.data);
            url = url + '/' + gameId;
        }

        if (resource === 'levels') {
            headers = { 'Authorization': `Basic ${token}`, 'Content-type': 'application/json' };
            const gameId = params.meta.gameId;
            body = JSON.stringify(params.data);
            url = url + '/' + gameId;
        }

        if (resource === 'challengemodels') {
            headers = { 'Authorization': `Basic ${token}`, 'Content-type': 'application/json' };
            const gameId = params.meta.gameId;
            body = JSON.stringify(params.data);
            url = url + '/' + gameId;
        }

        if (resource === 'pointconcepts') {
            headers = { 'Authorization': `Basic ${token}`, 'Content-type': 'application/json' };
            const gameId = params.meta.gameId;
            body = JSON.stringify(params.data);
            url = url + '/' + gameId;
        }

        if (resource === 'game') {
            headers = { 'Authorization': `Basic ${token}` };
            let formData = new FormData();
            formData.append('data', params.data.src.rawFile);
            body = formData;
            console.log(body.get('data'));
        }

        return fetch(url, { method: 'POST', headers: headers, body: body })
            .then(async (response: any) => {
                const json = await response.json();
                if (json.error) {
                    const errorObj = { status: json.status, message: json.error + " - " + json.message };
                    throw errorObj;
                }
                return json;
            })

    },
    update: (resource: any, params: any) => {
        let url = `${apiUrl}/${resource}`;
        let body;
        const token = localStorage.getItem('token');
        const headers = { 'Authorization': `Basic ${token}`, 'Content-type': 'application/json' };

        if (resource === 'actions') {
            const gameId = params.meta.gameId;
            url = url + '/' + gameId + '/' + params.data.action_name;
        }
        if (resource === 'tasks') {
            const gameId = params.meta.gameId;
            body = JSON.stringify(params.data);
            url = url + '/' + gameId + '/' + params.data.type;
        }
        if (resource === 'rules') {
            const gameId = params.meta.gameId;
            body = JSON.stringify(params.data);
            url = url + '/' + gameId;
        }
        if (resource === 'levels') {
            const gameId = params.meta.gameId;
            body = JSON.stringify(params.data);
            url = url + '/' + gameId;
        }
        if (resource === 'challengemodels') {
            const gameId = params.meta.gameId;
            body = JSON.stringify(params.data);
            url = url + '/' + gameId;
        }
        if (resource === 'game') {
            body = JSON.stringify(params.data);
        }

        return fetch(url, { method: 'PUT', headers: headers, body: body })
            .then(async (response: any) => {
                const json = await response.json();
                if (json.error) {
                    const errorObj = { status: json.status, message: json.error + " - " + json.message };
                    throw errorObj;
                }
                return json;
            })
    },
    delete: (resource: any, params: any) => {
        let url = `${apiUrl}/${resource}`;
        const token = localStorage.getItem('token');
        const headers = { 'Authorization': `Basic ${token}` };
        if (resource === 'game') {
            url = url + '/' + params.id;
        } else if (resource=== 'challenges') {
            const gameId = params.meta.gameId;
            const playerId = params.meta.playerId;
            url = url + '/' + gameId + '/' + playerId + '/challenge/' + params.id;
        } else {
            const gameId = params.meta.gameId;
            url = url + '/' + gameId + '/' + params.id;
        }
        return fetch(url, { method: 'DELETE', headers: headers })
            .then(async (response: any) => {
                const json = await response.json();
                if (json.error) {
                    const errorObj = { status: json.status, message: json.error + " - " + json.message };
                    throw errorObj;
                }
                return json;
            })
    },
    deleteMany: (resource: any, params: any) => {
        let url = `${apiUrl}/${resource}`;
        const token = localStorage.getItem('token');
        const headers = { 'Authorization': `Basic ${token}` };
        if (resource === 'game') {
            url = url + '/' + params.ids;
        } else {
            const gameId = params.meta.gameId;
            url = url + '/' + gameId + '/' + params.ids;
        }
        return fetch(url, { method: 'DELETE', headers: headers })
            .then(async (response: any) => {
                const json = await response.json();
                if (json.error) {
                    const errorObj = { status: json.status, message: json.error + " - " + json.message };
                    throw errorObj;
                }
                return json;
            })
    },
    getMany: (resource: any, params: any) => {
        let url = `${apiUrl}/${resource}`;
        const token = localStorage.getItem('token');
        const headers = { 'Authorization': `Basic ${token}` };
        const gameId = params.meta.gameId;
        url = url + '/' + gameId + '?ids=' + params.ids;
        return fetch(url, { method: 'GET', headers: headers })
            .then(async (response: any) => {
                const json = await response.json();
                if (json.error) {
                    const errorObj = { status: json.status, message: json.error + " - " + json.message };
                    throw errorObj;
                }
                return json;
            })
    }
};

export default gamificationDataProvider;

// function modifyChallengeModel(json: any) {
//     let body: any = {};
//     let variables: any = [];
//     json.data.variables.forEach((element: any) => {
//         let v: any = {};
//         v['name'] = element;              
//         variables.push(v);
//     });
//     body['gameId']= json.data.gameId;
//     body['id'] = json.data.id;
//     body['name'] = json.data.name;
//     body['variables'] = variables;
//     return body;
// }

function modifyPointConceptModel(json: any): any {
    let body: any = {};
    let periods: any = [];
    Object.entries(json.data.periods).map((element: any) => {
        let item: any = {};
        item['name'] = element[1].identifier;   
        item['start'] = element[1].start;
        item['end']=element[1].end;
        item['period'] = element[1].period;
        item['capacity'] = element[1].capacity;           
        periods.push(item);
    });
    body['id'] = json.data.name;
    body['name'] = json.data.name;
    body['score'] = json.data.score;
    body['periods'] = periods;
    return body;
}

