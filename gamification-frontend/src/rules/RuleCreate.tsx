import { Box, Card, CardContent, Typography } from '@mui/material';
import "ace-builds/src-noconflict/mode-drools";
import "ace-builds/src-noconflict/theme-github";
import { Create, Form, TextInput, useNotify, useRedirect, useStore } from 'react-admin';
import { CreateToolbar } from '../misc/CreateToolbar';
import { DroolRuleEditor } from '../misc/DroolsEditor';

export const RuleCreate = () => {
    const [gameId] = useStore('game.selected');
    const notify = useNotify();
    const redirect = useRedirect();

    const options = { meta: { gameId: gameId } };

    const onSuccess = (data: any) => {
        notify(`Rule created successfully`);
        redirect('list', 'rules');
    };

    return (
        <Create mutationOptions={{ ...options, onSuccess }} >
            <Box mt={2} display="flex">
                <Box flex="1">
                    <Form>
                        <Card>
                            <CardContent>
                                <Typography>Name</Typography>
                                <Box>
                                    <TextInput source="name" fullWidth />
                                </Box>
                                <Typography>Content</Typography>
                                <DroolRuleEditor source="content" />
                            </CardContent>
                            <CreateToolbar/>
                        </Card>
                    </Form>
                </Box>
            </Box>
        </Create>);
}