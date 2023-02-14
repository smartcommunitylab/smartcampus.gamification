import { CreateBase, Form, Toolbar, useStore } from 'react-admin';
import { Card, CardContent, Box } from '@mui/material';

import { ActionInputs } from './ActionInputs';

export const ActionCreate = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };

    return (<CreateBase
        mutationOptions={options}
    >
        <Box mt={2} display="flex">
            <Box flex="1">
                <Form>
                    <Card>
                        <CardContent>
                            <Box>
                                <Box display="flex">
                                    <ActionInputs />
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
