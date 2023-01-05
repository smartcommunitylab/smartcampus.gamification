import simpleRestProvider from 'ra-data-simple-rest';

const apiUrl= process.env.REACT_APP_API_ENDPOINT + '/console-ui';
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
        }

        return fetch(url, { method: 'GET', headers: headers })
            .then(response => response.json())
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
            .then(response => response.json())
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

        if (resource === 'game') {
            headers = { 'Authorization': `Basic ${token}` };
            let formData = new FormData();
            formData.append('data', params.data.src.rawFile);
            body = formData;
            console.log(body.get('data'));
        }

        return fetch(url, { method: 'POST', headers: headers, body: body })
            .then(response => response.json())
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
        if (resource === 'game') {
            body = JSON.stringify(params.data);
        }

        return fetch(url, { method: 'PUT', headers: headers, body: body })
            .then(async response => {
                const json = await response.json();
                return json;
            })
    },
    delete: (resource: any, params: any) => {
        let url = `${apiUrl}/${resource}`;
        const token = localStorage.getItem('token');
        const headers = { 'Authorization': `Basic ${token}` };
        // if (resource === 'tasks') {
        //     const gameId = params.meta.gameId;
        //     url = url + '/' + gameId + '/' + params.id;
        // }
        // if (resource === 'rules') {
        //     const gameId = params.meta.gameId;
        //     url = url + '/' + gameId + '/' + params.id;
        // }
        // if (resource === 'levels') {
        //     const gameId = params.meta.gameId;
        //     url = url + '/' + gameId + '/' + params.id;
        // }
        if (resource === 'game') {
            url = url + '/' + params.id;
        } else {
            const gameId = params.meta.gameId;
            url = url + '/' + gameId + '/' + params.id;
        }
        return fetch(url, { method: 'DELETE', headers: headers })
            .then(response => response.json())
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
            .then(response => response.json())
    },
    getMany: (resource: any, params: any) => {
        let url = `${apiUrl}/${resource}`;
        const token = localStorage.getItem('token');
        const headers = { 'Authorization': `Basic ${token}` };
        // if (resource === 'pointconcepts') {
            const gameId = params.meta.gameId;
            url = url + '/' + gameId + '/' + params.ids;
            // console.log(url);
        // }
        return fetch(url, { method: 'GET', headers: headers })
            .then(response => response.json())
    }
};

export default gamificationDataProvider;