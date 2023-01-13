import * as React from 'react';
import { Create, Form, TextInput, Toolbar, useNotify, useRedirect, useStore } from 'react-admin';
import { Card, CardContent, Box, Avatar } from '@mui/material';

import { PointConceptInputs } from './PointConceptInputs';

export const PointConceptCreate = () => {
    const [gameId] = useStore('game.selected');
    const notify = useNotify();
    const redirect = useRedirect();

    const options = { meta: { gameId: gameId } };

    const onSuccess = (data: any) => {
        notify(`PointConcept created successfully`);
        redirect('list', 'pointconcepts');
    };

    const transform = (data: any) => {
        {
            let body: any = {};
            body['name'] = data.name;
            body['id'] = data.name;
            let dayMillis = 24 * 3600 * 1000;
            body['periods'] = {};
            let periods: any = {};
            data.periods.forEach((element: any) => {
                const periodValue = element.period ? element.period * dayMillis : undefined;
                periods[element.name] = { start: new Date(element.start).getTime(), end: new Date(element.end).getTime(), period: periodValue, identifier: element.name, capacity: element.capacity };

            });
            body['periods'] = periods;
            return body;
        }
    }

    return (
        <Create transform={transform} mutationOptions={{ ...options, onSuccess }} >
            <Box mt={2} display="flex">
                <Box flex="1">
                    <Form>
                        <Card>
                            <CardContent>
                                <Box>
                                    <Box display="flex">
                                    </Box>
                                    <Box display="flex" width={630}>
                                        <TextInput label="Name" source="name" fullWidth />
                                    </Box>
                                    <PointConceptInputs />
                                </Box>
                            </CardContent>
                            <Toolbar />
                        </Card>
                    </Form>
                </Box>
            </Box>
        </Create>);
}


