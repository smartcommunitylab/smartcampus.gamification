import { Create, Form, Toolbar, useNotify, useRedirect, useStore } from 'react-admin';
import { Card, CardContent, Box } from '@mui/material';

import { TaskInputs } from './TaskInputs';

export const TaskCreate = () => {
    const [gameId] = useStore('game.selected');
    const notify = useNotify();
    const redirect = useRedirect();

    const options = { meta: { gameId: gameId } };

    const transform = (data: any) => {{
        console.log(data);
        let body = createTaskBody(data, gameId);
        console.log(body);
        return body;
    }}


    const onSuccess = (data:any) => {
        notify(`Task created successfully`);
        redirect('list', 'tasks');
    };

    return (
    <Create transform={transform}  mutationOptions={{ ...options, onSuccess }} >
        <Box mt={2} display="flex">
            <Box flex="1">
                <Form>
                    <Card>
                        <CardContent>
                            <Box>
                                <Box display="flex">
                                    <Box mr={2}>
                                    </Box>
                                    <TaskInputs />
                                </Box>
                            </Box>
                        </CardContent>
                        <Toolbar />
                    </Card>
                </Form>
            </Box>
        </Box>
    </Create>);
    
}

function createTaskBody(data: any, gameId:any) {
    let body: any = {};
    if (data.type === 'general') {
        body['type'] = data.type;
        body['cronExpression'] = data.task.cronExpression;
        body['name'] = data.task.name;
        body['gameId'] = gameId;
        body['itemsToNotificate'] = data.task.itemsToNotificate;
        body['itemType'] = data.pointConceptName;
        body['classificationName'] = data.task.classificationName;
    } else if (data.type === 'incremental') {
        body['name'] = data.task.name;
        body['itemType'] = data.pointConceptName;
        body['classificationName'] = data.task.classificationName;
        body['periodName'] = data.task.periodName;
        body['itemsToNotificate'] = data.task.itemsToNotificate;
        body['type'] = data.type;
        body['delayValue'] = data.task.delayValue;
        body['delayUnit'] = data.task.delayUnit;
        body['gameId'] = gameId;
    }

    return body;

}
