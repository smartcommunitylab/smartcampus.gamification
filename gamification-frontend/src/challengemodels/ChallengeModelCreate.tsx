import { ArrayInput, Create, Form, SimpleFormIterator, TextInput, Toolbar, useNotify, useRedirect, useStore } from 'react-admin';
import { Card, CardContent, Box } from '@mui/material';


export const ChallengeModelCreate = () => {
    const [gameId] = useStore('game.selected');
    const notify = useNotify();
    const redirect = useRedirect();

    const options = { meta: { gameId: gameId } };

    const onSuccess = (data: any) => {
        notify(`ChallengeModel created successfully`);
        redirect('list', 'challengemodels');
    };

    // const transform = (data: any) => {{       
    //     let body: any = {};
    //     body['name'] = data.name;
    //     body['variables'] = [];
    //     data.variables.forEach((element:any) => {
    //         body['variables'].push(element);
    //     });
    //     return body;
    // }}
//transform={transform} 
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
                                    <Box>
                                        <ArrayInput source="variables">
                                            <SimpleFormIterator inline>
                                                <TextInput source="" label="Name" helperText={false} />                                                
                                            </SimpleFormIterator>
                                        </ArrayInput>
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


