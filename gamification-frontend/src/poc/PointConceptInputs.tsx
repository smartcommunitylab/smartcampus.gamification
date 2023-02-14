import {
    TextInput, NumberInput,
    useStore,
    ArrayInput,
    SimpleFormIterator, DateField, DateInput, DateTimeInput, required
} from 'react-admin';
import { Box } from '@mui/material';

export const PointConceptInputs = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };
    return (
        <Box flex="1" mt={-1}>
            <Box>
                <ArrayInput source="periods">
                    <SimpleFormIterator inline>
                        <TextInput source="name" helperText={false} validate={[required()]}/>
                        <DateInput source="start"  validate={[required()]} helperText={false}/>
                        <DateInput source="end"  validate={[required()]} helperText={false} />
                        <NumberInput title='period in days' source="period" validate={[required()]} helperText={'period in days'} />
                        <NumberInput source="capacity" validate={[required()]} helperText={false} />
                     </SimpleFormIterator>
                </ArrayInput>
            </Box>
        </Box>
    );
};


