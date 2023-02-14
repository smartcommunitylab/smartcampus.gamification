import {
    TextInput,
    ReferenceInput, RadioButtonGroupInput,
    SelectInput,
    FormDataConsumer,
    NumberInput,
    useStore
} from 'react-admin';
import { Box } from '@mui/material';

export const TaskInputs = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };
    return (
        <Box flex="1" mt={-1}>
            <Box display="flex" width={630}>
                <TextInput label="Name" source="task.name" fullWidth />
                <Spacer />
                <TextInput label="Number elements in classification" source="task.itemsToNotificate" fullWidth />
            </Box>
           
            <Box>
            <ReferenceInput source="pointConceptName" queryOptions={options} reference="pointconcepts" perPage={1000}>
                <SelectInput
                    label="Point Concept"
                    optionText={(pointconcept: any) =>
                        `${pointconcept.pc.name}`
                    }
                    optionValue='pc.name'
                 />
            </ReferenceInput>
            </Box>
           
            <Box mt={2} width={630}>
                <TextInput label="Classification name" source="task.classificationName" fullWidth />
            </Box>
                
            <Box display="flex" width={430}>
            <RadioButtonGroupInput source="type" choices={[{ id: 'general', name: 'General' }, { id: 'incremental', name: 'Incremental' }]} />
            </Box>
          
            <FormDataConsumer>
            {({ formData, ...rest }) => formData.type=='incremental' &&
            <Box>
            <TextInput label="Period Name(weekly, daily)" source="task.periodName" fullWidth/>
            <br/>
            <NumberInput label="Delay" source="task.delayValue"/>
            &nbsp;
            <SelectInput label="Delay unit"  source="task.delayUnit" choices={[{ id: 'DAY', name: 'DAY' }, { id: 'HOUR', name: 'HOUR' }, { id: 'MINUTE', name: 'MINUTE' }]} />
            </Box>
            }
            </FormDataConsumer>              

            <FormDataConsumer>
            {({ formData, ...rest }) => formData.type=='general' &&
            <Box mt={2} width={630}>
            <TextInput label="Cron expression" source="task.cronExpression" fullWidth />
            </Box>
            }
            </FormDataConsumer>              
            
        </Box>
    );
};

const Spacer = () => <Box width={20} component="span" />;
