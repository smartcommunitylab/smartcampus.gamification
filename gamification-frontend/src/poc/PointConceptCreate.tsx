import * as React from 'react';
import { CreateBase, Form, Toolbar } from 'react-admin';
import { Card, CardContent, Box, Avatar } from '@mui/material';

import { PointConceptInputs } from './PointConceptInputs';
import { PointConcept } from '../types';

export const PointConceptCreate = () => (
    <CreateBase
        redirect="show"
        transform={(data: PointConcept) => ({
            ...data,
            last_seen: new Date(),
            tags: [],
        })}
    >
        <Box mt={2} display="flex">
            <Box flex="1">
                <Form>
                    <Card>
                        <CardContent>
                            <Box>
                                <Box display="flex">
                                    <PointConceptInputs />
                                </Box>
                            </Box>
                        </CardContent>
                        <Toolbar />
                    </Card>
                </Form>
            </Box>
        </Box>
    </CreateBase>
);
