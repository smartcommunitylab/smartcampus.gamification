import {
    BooleanInput,
    TextInput
} from 'react-admin';
import { Divider, Box } from '@mui/material';

export const BadgeInputs = () => {
    return (
        <Box flex="1" mt={-1}>
            <Box display="flex" width={430}>
                <TextInput source="badgeName" fullWidth />
                
            </Box>
            <Box>
                <BooleanInput source="hidden" fullWidth />
            </Box>
            <Divider />
        </Box>
    );
};

const Spacer = () => <Box width={20} component="span" />;
