import { DateTimeInput, Form, SelectInput, TextInput, BooleanInput, useStore, useNotify, useRedirect, Toolbar, CreateBase, Create, SimpleForm, ReferenceInput, required } from 'react-admin';
import { Card, CardContent, Box } from '@mui/material';
import { useSearchParams } from 'react-router-dom';

export const ChallengeCreate = (params: any) => {
    const [gameId] = useStore('game.selected');
    const [searchParams] = useSearchParams();
    const options = { meta: { gameId: gameId, playerId: searchParams ? searchParams.get('playerId') : null } };
    const notify = useNotify();
    const redirect = useRedirect();

    const onSuccess = (data: any) => {
        notify(`Single Challenge created successfully`);
        redirect('/monitor/' + options.meta.playerId + '/show');
    };

    const transform = (data: any) => {
            let body: any = {};
            body['instanceName'] = data.instanceName;
            body['modelName'] = data.modelName;
            body['start'] = new Date(data.start).getTime();
            body['end'] = new Date(data.end).getTime();            
            let dataChallenge: any = {};
            dataChallenge['bonusScore'] = parseFloat(data.bonusScore);
            dataChallenge['periodName'] = 'weekly';
            dataChallenge['bonusPointType'] = data.bonusPC;
            dataChallenge['counterName'] = data.targetPC;
            dataChallenge['target'] = parseFloat(data.target);
            body['data'] = dataChallenge;
            return body;
    }

    const choices = [
        { _id: 'PROPOSED', label: 'PROPOSED' },
        { _id: 'ASSIGNED', label: 'ASSIGNED' },
    ];

    const modelChoices = [
        { _id: 'absoluteIncrement', label: 'absoluteIncrement' }

    ];

    return (<Create transform={transform} mutationOptions={{ ...options, onSuccess }} >
        <Box mt={2} display="flex">
            <Box flex="1">
                <Form validate={validateUserCreation}>
                    <Card>
                        <CardContent>
                            <Box width={630}>
                                <TextInput label="Challenge Name" source="instanceName" fullWidth />
                            </Box>
                            <Box width={630}>
                                <SelectInput
                                    source="modelName"
                                    choices={modelChoices}
                                    optionText="label"
                                    optionValue="_id"
                                />
                            </Box>
                            <Box width={630}>
                                <SelectInput
                                    source="state"
                                    choices={choices}
                                    optionText="label"
                                    optionValue="_id"
                                />
                            </Box>
                            <Box mt={2} width={630}>
                                <DateTimeInput source="start" onKeyDown={(event) => { event.preventDefault(); }} validate={required()} />
                            </Box>
                            <Box mt={2} width={630}>
                                <DateTimeInput source="end" onKeyDown={(event) => { event.preventDefault(); }}  validate={required()} />
                            </Box>
                            <Box width={630}>
                                <TextInput label="Bonus Score" source="bonusScore" fullWidth />
                            </Box>
                            <Box width={630}>
                                <ReferenceInput source="bonusPC" queryOptions={options} reference="pointconcepts" perPage={1000}>
                                    <SelectInput
                                        label="Bounus Point Concept"
                                        optionText={(pointconcept: any) =>
                                            `${pointconcept.pc.name}`
                                        }
                                        optionValue='pc.name'
                                    />
                                </ReferenceInput>
                            </Box>
                            <Box width={630}>
                                <ReferenceInput source="targetPC" queryOptions={options} reference="pointconcepts" perPage={1000}>
                                    <SelectInput
                                        label="Target Point Concept"
                                        optionText={(pointconcept: any) =>
                                            `${pointconcept.pc.name}`
                                        }
                                        optionValue='pc.name'
                                    />
                                </ReferenceInput>
                            </Box>
                            <Box width={630}>
                                <TextInput label="Target" source="target" fullWidth />
                            </Box>
                        </CardContent>
                        <Toolbar />
                    </Card>
                </Form>
            </Box>
        </Box>
    </Create>
    );
}


const validateUserCreation = (values: any) => {
    let errors: any = {};

    if (!values.instanceName) {
        errors.instanceName = 'The challenge name is required';
    }

    if (!values.modelName) {
        errors.modelName = 'The challenge model is required';
    }

    if (!values.state) {
        errors.state = 'The challenge state is required';
    }

    if (!values.start) {
        errors.start = 'The start date is required';
    }

    if (!values.end) {
        errors.end = 'The end date is required';
    }

    if (values.start != null  &&  values.end != null && values.start > values.end) {
        errors.start = 'start date must be less than end date';
    }

    if (!values.bonusScore) {
        errors.bonusScore = 'The challenge bonus score is required';
    }

    if (!values.bonusPC) {
        errors.pointConcept = 'The bonus point concept is required';
    }

    if (!values.targetPC) {
        errors.pointConcept = 'The target point concept is required';
    }

    if (!values.target) {
        errors.target = 'The challenge target is required';
    }

    return errors
};