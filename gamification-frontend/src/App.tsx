import {
    Admin,
    Resource,
    ListGuesser
} from 'react-admin';
import authProvider from './authProvider';
import gamificationDataProvider from './dataProvider';
import games from './games';
import { Layout } from 'react-admin';
import { MyMenu } from './MyMenu';
import { MyAppBar } from './MyAppBar';
import actions from './actions';
import tasks from './tasks';
import poc from './poc';
import badges from './badges';
import rules from './rules';
import levels from './levels';
import challengemodels from './challengemodels';
import monitor from './monitor';

const MyLayout = (props:any) => <Layout {...props} menu={MyMenu}  appBar={MyAppBar} />;

let listOfResources:any[] = [{name:'game'}];

function App() {
 

    return (
        <Admin 
        layout={MyLayout}
        authProvider={authProvider}
        dataProvider={gamificationDataProvider}>
            <Resource name="game" {...games}></Resource>
            <Resource name="actions" {...actions}></Resource>
            <Resource name="tasks" {...tasks}></Resource>
            <Resource name="pointconcepts" {...poc}></Resource>
            <Resource name="badges" {...badges}></Resource>
            <Resource name="rules" {...rules}></Resource>
            <Resource name="levels" {...levels}></Resource>
            <Resource name="challengemodels" {...challengemodels}></Resource>
            <Resource name="monitor" {...monitor}></Resource>
            <Resource name="challenges" list={ListGuesser}></Resource>
        </Admin>
    );
}

export default App;