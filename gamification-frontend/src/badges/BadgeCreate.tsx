import { CreateBase, Form, Toolbar, useNotify, useRedirect, useStore } from 'react-admin';
import { Card, CardContent, Box } from '@mui/material';

import { BadgeInputs } from './BadgeInputs';

export const BadgeCreate = () => {
    const [gameId] = useStore('game.selected');
    const notify = useNotify();
    const redirect = useRedirect();

    const options = { meta: { gameId: gameId } };

    const transform = (data: any) => {
        {
            console.log(data);
            let body = createBadge(data);
            console.log(body);
            return body;
        }
    }


    const onSuccess = (data: any) => {
        notify(`Badge created successfully`);
        redirect('list', 'badges');
    };
    return (
        <CreateBase transform={transform} mutationOptions={{ ...options, onSuccess }}>
            <Box mt={2} display="flex">
                <Box flex="1">
                    <Form>
                        <Card>
                            <CardContent>
                                <Box>
                                    <Box display="flex">
                                        <BadgeInputs />
                                    </Box>
                                </Box>
                            </CardContent>
                            <Toolbar />
                        </Card>
                    </Form>
                </Box>
            </Box>
        </CreateBase>
    );
}

function createBadge(data: any): any {
    let body: any = {};
    body['id'] = data.badgeName;
    body['name'] = data.badgeName;
    body['hidden'] = data.hidden;
    return body;
}
