import { ArrayInput, Edit, EditBase, Form, InputProps, RecordContext, RecordContextProvider, SimpleFormIterator, TextInput, useEditContext, useNotify, useRecordContext, useRedirect, useStore, WithRecord } from 'react-admin';
import { Card, CardContent, Box } from '@mui/material';
import { ChallengeModel, Level } from '../types';
import { EditToolbar } from '../misc/EditToolbar';
import { isVariableStatement } from 'typescript';
import { getValue } from '@testing-library/user-event/dist/utils';

export const ChallengeModelEdit = () => {
    const [gameId] = useStore('game.selected');
    const notify = useNotify();
    const redirect = useRedirect();
    const options = { meta: { gameId: gameId } };
   
    const transform = (data: any) => {
        {
            let body = data;
            return body;
        }
    }

    const onSuccess = (data: any) => {
        notify(`ChallengeModel updated successfully`);
        redirect('/challengemodels/' + data.id + '/show');
    };


    return (<EditBase 
        mutationMode='pessimistic'
        transform={transform}
        mutationOptions={{ ...options, onSuccess }}
        queryOptions={options}
    >
         <ChallengeModelEditContent />
        
    </EditBase>);
}

const ChallengeModelEditContent = () => {
    const { isLoading, record } = useEditContext<ChallengeModel>();
    if (isLoading || !record) return null;
    
    let saved = record.variables;
    let variables: any = [];
   
    saved.forEach((element: any) => {
        let v: any = {};
        v['name'] = element;              
        variables.push(v);
    });
    record.variables = [];
    record.variables = variables;

    console.log(record);


    
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
                                        <SimpleFormIterator inline>
                                            <TextInput source="name" helperText={false} />
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



