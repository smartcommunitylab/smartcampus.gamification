import { EditBase, Form, TextInput, useEditContext, useNotify, useRedirect, useStore } from 'react-admin';
import { Card, CardContent, Box, Typography } from '@mui/material';
import { Task } from '../types';
import "ace-builds/src-noconflict/mode-drools";
import "ace-builds/src-noconflict/theme-github";
import { DroolRuleEditor } from '../misc/DroolsEditor';
import { EditToolbar } from '../misc/EditToolbar';

export const RuleEdit = () => {
    const [gameId] = useStore('game.selected');
    const notify = useNotify();
    const redirect = useRedirect();
    const options = { meta: { gameId: gameId } };

    const onSuccess = (data: any) => {
        notify(`Rule updated successfully`);
        redirect('/rules/' + data.id + '/show');
    };

    return (<EditBase mutationMode='pessimistic'
        mutationOptions={{ ...options, onSuccess }}
        queryOptions={options}
    >
        <RuleEditContent />
    </EditBase>);
}

const RuleEditContent = () => {
    const { isLoading, record } = useEditContext<Task>();
    if (isLoading || !record) return null;

    return (
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
                            <Box>
                                <DroolRuleEditor source="content" />
                            </Box>
                        </CardContent>
                        <EditToolbar title="Rule" resourcename="rules" targetname="list" />
                    </Card>
                </Form>
            </Box>

        </Box>
    );
};

