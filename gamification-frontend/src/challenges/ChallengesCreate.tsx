import { DateTimeInput, Form, SelectInput, TextInput, useStore, useNotify, useRedirect, Toolbar, Create, ReferenceInput, required, FormDataConsumer } from 'react-admin';
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
            body['origin'] = 'gcra';
            body['start'] = new Date(data.start).getTime();
            body['end'] = new Date(data.end).getTime();            
            let dataChallenge: any = {};
            dataChallenge['bonusScore'] = parseFloat(data.bonusScore).toFixed(1);
            dataChallenge['periodName'] = data.periodName;
            dataChallenge['bonusPointType'] = data.bonusPC;
            dataChallenge['counterName'] = data.targetPC;
            dataChallenge['target'] = parseFloat(data.target).toFixed(1);;
            if (data.modelName === 'repetitiveBehaviour') {
                dataChallenge['periodTarget'] = parseFloat(data.periodTarget).toFixed(1);;  
            } else if (data.modelName === 'percentageIncrement') {
                dataChallenge['percentage'] = parseFloat(data.percentage).toFixed(1);;
                dataChallenge['baseline'] = parseFloat(data.baseline).toFixed(1);;
            }
            body['data'] = dataChallenge;
            return body;
    }

    const stateChoices = [
        // { _id: 'PROPOSED', label: 'PROPOSED' },
        { _id: 'ASSIGNED', label: 'ASSIGNED' },
    ];

    const periodChoices = [
        { _id: 'daily', label: 'daily' },
        { _id: 'weekly', label: 'weekly' },
    ];

    const modelChoices = [
        { _id: 'absoluteIncrement', label: 'absoluteIncrement' },
        { _id: 'percentageIncrement', label: 'percentageIncrement' },
        { _id: 'repetitiveBehaviour', label: 'repetitiveBehaviour' }
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
                                <SelectInput  sx={{ minWidth: 225}}
                                    source="modelName"
                                    choices={modelChoices}
                                    optionText="label"
                                    optionValue="_id"
                                />
                            </Box>
                            <Box width={630}>
                                <SelectInput  sx={{ minWidth: 225}}
                                    source="state"
                                    choices={stateChoices}
                                    optionText="label"
                                    optionValue="_id"
                                />
                            </Box>
                            <Box width={630}>
                                <SelectInput  sx={{ minWidth: 225}}
                                    source="periodName"
                                    choices={periodChoices}
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
                                    <SelectInput sx={{ minWidth: 225}}
                                        label="Bounus Point Concept"
                                        optionText={(pointconcept: any) =>
                                            `${pointconcept.pc.name}`
                                        }
                                        optionValue='pc.name'
                                    />
                                </ReferenceInput>
                            </Box>
                            <Box>
                                <ReferenceInput source="targetPC" queryOptions={options} reference="pointconcepts" perPage={1000}>
                                    <SelectInput sx={{ minWidth: 225}}
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
                            <FormDataConsumer>
                                 {({ formData, ...rest }) => formData.modelName=='repetitiveBehaviour' &&
                                  <Box width={630}>
                                  <TextInput label="Period Target" source="periodTarget" fullWidth />
                              </Box>
                            }
                            </FormDataConsumer>
                            <FormDataConsumer>
                                 {({ formData, ...rest }) => formData.modelName=='percentageIncrement' &&
                                  <Box width={630}>
                                  <TextInput label="Percentage" source="percentage" fullWidth />
                              </Box>
                            }
                            </FormDataConsumer>
                            <FormDataConsumer>
                                 {({ formData, ...rest }) => formData.modelName=='percentageIncrement' &&
                                  <Box width={630}>
                                  <TextInput label="Baseline" source="baseline" fullWidth />
                              </Box>
                            }
                            </FormDataConsumer>
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

    if (!values.periodName) {
        errors.periodName = 'The period is required';
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
        errors.targetPC = 'The target point concept is required';
    }

    if (!values.target) {
        errors.target = 'The challenge target is required';
    }

    return errors
};