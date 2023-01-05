import { AUTH_LOGIN, AUTH_CHECK, AUTH_LOGOUT } from 'react-admin';
import { Buffer } from 'buffer';

const auth =  (type:any, params:any) => {

    if (type === AUTH_CHECK) {
        return localStorage.getItem('token') ? Promise.resolve() : Promise.reject();
    }

    if (type === AUTH_LOGIN) {
        const { username, password } = params;
        const token = Buffer.from(`${username}:${password}`, 'utf8').toString('base64');
        const request = new Request('http://localhost:8010/gamification/userProfile/', {
            method: 'GET',
            headers: new Headers({ 'Content-Type': 'application/json', "Accept": "application/json", 'Authorization': `Basic ${token}` }),
            
        })
        return fetch(request)
            .then(response => {
                if (response.status < 200 || response.status >= 300) {
                    throw new Error(response.statusText);
                }
                return response.json();
            })
            .then(auth => {
                const token = Buffer.from(`${username}:${password}`, 'utf8').toString('base64');
                localStorage.setItem('token', token);
                localStorage.setItem('role', auth.domains[0]);
                localStorage.setItem('username', auth.username);
            })
            .catch(() => {
                throw new Error('Network error')
        });     
    }

    if (type === AUTH_LOGOUT) {
        localStorage.clear();
    }

    return Promise.resolve();
}



export default auth;