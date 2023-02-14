import { ArrayInput, EditBase, Form, SimpleFormIterator, TextInput, useNotify, useRedirect, useStore } from 'react-admin';
import { Card, CardContent, Box } from '@mui/material';
import { EditToolbar } from '../misc/EditToolbar';

export const ChallengeModelEdit = () => {
    const [gameId] = useStore('game.selected');
    const notify = useNotify();
    const redirect = useRedirect();
    const options = { meta: { gameId: gameId } };

    // const transform = (data: any) => {{       
    //     let body: any = {};
    //     body['name'] = data.name;
    //     body['id'] = data.id;
    //     body['variables'] = [];
    //     data.variables.forEach((element:any) => {
    //         body['variables'].push(element.name);
    //     });
    //     return body;
    // }}

    const onSuccess = (data: any) => {
        notify(`ChallengeModel updated successfully`);
        redirect('/challengemodels/' + data.id + '/show');
    };


    return (<EditBase
        mutationMode='pessimistic'
        // transform={transform}
        mutationOptions={{ ...options, onSuccess }}
        queryOptions={options}
    >
        <ChallengeModelEditContent />
    </EditBase>);
}

const ChallengeModelEditContent = () => {

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
                                <Box>
                                    <ArrayInput source="variables">
                                        <SimpleFormIterator inline disableReordering>
                                            <TextInput source="" helperText={false} />
                                        </SimpleFormIterator>
                                    </ArrayInput>
                                </Box>
                            </Box>
                        </CardContent>
                        <EditToolbar title="ChallengeModel" resourcename="challengemodels" targetname="list" />
                    </Card>
                </Form>
            </Box>

        </Box>
    );
};



