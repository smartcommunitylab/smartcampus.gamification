import {
    TextInput, NumberInput,
    useStore,
    ArrayInput,
    SimpleFormIterator, DateField, DateInput, DateTimeInput
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
                        <TextInput source="name" helperText={false} />
                        <DateInput source="start" helperText={false}/>
                        <DateInput source="end" helperText={false} />
                        <NumberInput title='period in days' source="period" helperText={'period in days'} />
                        <NumberInput source="capacity" helperText={false} />
                     </SimpleFormIterator>
                </ArrayInput>
            </Box>
        </Box>
    );
};


