import * as React from 'react';
import { Create, Form, TextInput, Toolbar, useNotify, useRedirect, useStore } from 'react-admin';
import { Card, CardContent, Box, Avatar } from '@mui/material';

import { LevelInputs } from './LevelInputs';

export const LevelCreate = () => {
    const [gameId] = useStore('game.selected');
    const notify = useNotify();
    const redirect = useRedirect();

    const options = { meta: { gameId: gameId } };

    const onSuccess = (data:any) => {
        notify(`Level created successfully`);
        redirect('list', 'levels');
    };

    return (
    <Create mutationOptions={{ ...options, onSuccess }} >
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
                                    <LevelInputs />
                            </Box>
                        </CardContent>
                        <Toolbar />
                    </Card>
                </Form>
            </Box>
        </Box>
    </Create>);    
}


