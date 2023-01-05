import { EditBase, Form, TextInput, useEditContext, useNotify, useRedirect, useStore } from 'react-admin';
import { Card, CardContent, Box } from '@mui/material';
import { LevelInputs } from './LevelInputs';
import { Level } from '../types';
import { EditToolbar } from '../misc/EditToolbar';

export const LevelEdit = () => {
    const [gameId] = useStore('game.selected');
    const notify = useNotify();
    const redirect = useRedirect();
    const options = { meta: { gameId: gameId } };

    const transform = (data: any) => {{
        let body = data;
        return body;
    }}

    const onSuccess = (data:any) => {
        notify(`Level updated successfully`);
        redirect('/levels/' + data.id + '/show');
    };

    return (<EditBase  mutationMode='pessimistic' 
     transform={transform}
      mutationOptions={{ ...options, onSuccess }}
       queryOptions={ options }     
    >
        <LevelEditContent />
    </EditBase>);
}

const LevelEditContent = () => {
    const { isLoading, record } = useEditContext<Level>();
    if (isLoading || !record) return null;
    return (
        <Box mt={2} display="flex">
            <Box flex="1">
                <Form>
                    <Card>
                        <CardContent>
                            <Box>
                                <Box display="flex">
                                    </Box>
                                    <Box display="flex" width={630}>
                                        <TextInput disabled label="Name" source="name" fullWidth />
                                    </Box>
                                    <br />
                                    <LevelInputs />
                            </Box>
                        </CardContent>
                        <EditToolbar title="Level" resourcename="levels" targetname="list"/>
                    </Card>
                </Form>
            </Box>
 
        </Box>
    );
};



