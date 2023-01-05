import {
    TextInput,
    ReferenceInput, SelectInput, NumberInput,
    useStore,
    ArrayInput,
    SimpleFormIterator,
    SelectArrayInput
} from 'react-admin';
import { Box } from '@mui/material';

export const LevelInputs = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };
    return (
        <Box flex="1" mt={-1}>
            <Box>
                <ReferenceInput source="pointConcept" queryOptions={options} reference="pointconcepts" perPage={1000}>
                    <SelectInput
                        label="Point Concept"
                        optionText={(pointconcept: any) =>
                            `${pointconcept.pc.name}`
                        }
                        optionValue='pc.name'
                    />
                </ReferenceInput>
            </Box>

            <Box>
                <ArrayInput source="thresholds">
                    <SimpleFormIterator inline>
                        <TextInput source="name" helperText={false} />
                        <NumberInput source="value" helperText={false} />
                        <NumberInput source="config.choices" helperText={false} />
                        <ReferenceInput source="config.activeModels" queryOptions={options} reference="challengemodels" perPage={1000}>
                            <SelectArrayInput label="Challenge model"
                                optionText={(chm: any) =>
                                    `${chm.name}`
                                }
                                optionValue='name'/>
                        </ReferenceInput>
                    </SimpleFormIterator>
                </ArrayInput>
            </Box>
        </Box>
    );
};


