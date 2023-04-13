import { DateTimeInput, EditBase, Form, NumberInput, ReferenceInput, SelectInput, TextInput, useEditContext, CheckboxGroupInput, FormDataConsumer, BooleanInput, useStore, useNotify, useRefresh, useRedirect, Button, Confirm, useDeleteMany, useListContext, useUnselectAll, TopToolbar, ToolbarProps, DeleteButton, SaveButton, ToolbarClasses, Toolbar, useDelete } from 'react-admin';
import { Card, CardContent, Box, Theme, useMediaQuery } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { Game } from '../types';
import { Children, useState } from 'react';
import clsx from 'clsx';
import { useParams } from 'react-router';
import { useSearchParams } from 'react-router-dom';
import { EditToolbar } from '../misc/EditToolbar';
import React from 'react';

export const ChallengeEdit = (params: any) => {
    const [gameId] = useStore('game.selected');
    const notify = useNotify();
    const redirect = useRedirect();
    const [searchParams] = useSearchParams();
    const options = { meta: { gameId: gameId, playerId: searchParams ? searchParams.get('playerId') : null } };

    const transform = (data: Game) => {
        {
            if (!data.expiry) {
                data.expiration = 0;
            }
            // convert date to long.
            console.log(typeof data.expiration);
            if (data.expiration != 0) {
                if (typeof (data.expiration) === 'object') {
                    data.expiration = data.expiration.getTime();
                }
            }
            return data
        }
    }

    const onSuccess = (data: any) => {
        console.log('on success');
        notify(`Challenge updated successfully`); // default message is 'ra.notification.updated'
        // redirect('/moinitor/' + gameId + '/show');
    };

    return (
        <EditBase
            mutationMode='pessimistic'
            redirect="list"
            // transform={transform}
            mutationOptions={{ ...options, onSuccess }}
            queryOptions={options}
        >
            <ChallengeEditContent/>
        </EditBase>
    );
}

const ChallengeEditContent = () => {
    const [gameId] = useStore('game.selected');
    const [searchParams] = useSearchParams();
    const options = { meta: { gameId: gameId, playerId: searchParams ? searchParams.get('playerId') : null } };
    const { isLoading, record } = useEditContext<any>();
    if (isLoading || !record) return null;
    const choices = [
        { _id: 'PROPOSED', label: 'PROPOSED' },
        { _id: 'ASSIGNED', label: 'ASSIGNED' },
        { _id: 'ACTIVE', label: 'ACTIVE' },
        { _id: 'COMPLETED', label: 'COMPLETED' },
        { _id: 'FAILED', label: 'FAILED' },
        { _id: 'REFUSED', label: 'REFUSED' },
        { _id: 'AUTO_DISCARDED', label: 'AUTO_DISCARDED' },
        { _id: 'CANCELED', label: 'CANCELED' }
    ];

    return (
        <Box mt={2} display="flex">
            <Box flex="1">
                <Form validate={validateUserCreation}>
                    <Card>
                        <CardContent>
                            <Box width={630}>
                                <TextInput disabled label="Name" source="name" fullWidth />
                            </Box>
                            <Box width={630}>
                                <SelectInput disabled
                                    source="concept.state"
                                    choices={choices}
                                    optionText="label"
                                    optionValue="_id"
                                />
                            </Box>
                            <FormDataConsumer>
                                {({ formData, ...rest }) => formData.concept.start &&
                                    <Box mt={2} width={630}>
                                        <DateTimeInput source="concept.start" onKeyDown={(event) => {event.preventDefault();}} validate={validateUserCreation}/>
                                    </Box>
                                }
                            </FormDataConsumer>
                            <FormDataConsumer>
                                {({ formData, ...rest }) => formData.concept.end &&
                                    <Box mt={2} width={630}>
                                        <DateTimeInput source="concept.end"  onKeyDown={(event) => {event.preventDefault();}} validate={validateUserCreation} />
                                    </Box>
                                }
                            </FormDataConsumer>
                            <Box width={630}>
                                Challenge visibility hidden
                                <BooleanInput source="concept.visibility.hidden" />
                            </Box>
                            <FormDataConsumer>
                                {({ formData, ...rest }) => formData.concept.visibility.disclosureDate &&
                                    <Box mt={2} width={630}>
                                        <DateTimeInput source="concept.visibility.disclosureDate" />
                                    </Box>
                                }
                            </FormDataConsumer>
                        </CardContent>
                        <ChallengeEditToolbar title="Challenge" playerId={options.meta.playerId} resourcename="challenges" targetname="list" />
                    </Card>
                </Form>
            </Box>
        </Box>
    );
};


export const ChallengeEditToolbar = (props: any) => {
    const { isLoading, record } = useEditContext<any>();
    if (isLoading || !record) return null;
    const { children, className, resource, ...rest } = props;
    const isXs = useMediaQuery<Theme>(theme => theme.breakpoints.down('sm'));
    const notify = useNotify();
    const redirect = useRedirect();
    const [gameId, setGameId] = useStore('game.selected');
    const [open, setOpen] = React.useState(false);
    const handleClick = () => setOpen(true);
    const [deleteOne] = useDelete(
        props.resourcename,
        { id: record.id, meta: { gameId: gameId, playerId: props.playerId?props.playerId:null } }
    );
    const handleDialogClose = () => setOpen(false);
    const handleConfirm = (data: any) => {
        deleteOne();
        setOpen(false);
        notify(props.title + ` deleted successfully`);
        redirect("/monitor/"+ props.playerId + "/show");
    };

    let title = props.title + " Deletion";
    let content = "Are you sure you want to delete this " + props.title + " ?"
    return (
        <Toolbar
            className={clsx(
                {
                    [ToolbarClasses.mobileToolbar]: isXs,
                    [ToolbarClasses.desktopToolbar]: !isXs,
                },
                className
            )}
            role="toolbar"
            {...rest}
        >
            {React.Children.count(children) === 0 ? (
                <div className={ToolbarClasses.defaultToolbar}>
                    <SaveButton/>
                    <Button label="Remove" endIcon={<DeleteIcon />} onClick={handleClick} sx={{ color: 'red' }} />
                    <Confirm
                        isOpen={open}
                        loading={isLoading}
                        title= {title}
                        content= {content}
                        onConfirm={handleConfirm}
                        onClose={handleDialogClose}
                    />
                </div>
            ) : (
                children
            )}
        </Toolbar>
    );
};

const validateUserCreation = (values: any) => {
    var errors: any = {};
    if (values.concept.start > values.concept.end) {
        errors.start = 'start date must be less than end date';
    }

    if (values.concept.visibility.disclosureDate != null &&
        ((values.concept.visibility.disclosureDate < values.concept.start) || (values.concept.visibility.disclosureDate > values.concept.end))) {
        errors.start = 'disclosure date must be within start and end date';
    }

    return errors
};