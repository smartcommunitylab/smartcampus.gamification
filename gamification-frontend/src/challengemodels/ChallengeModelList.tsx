import { Button, List, ListItem, Checkbox, ListItemText, ListSubheader } from '@mui/material';
import {
    BulkActionsToolbar,
    CheckboxGroupInput,
    CreateButton, Datagrid, DatagridHeader, EditButton, List as RaList, Pagination, RecordContextProvider, Show, ShowButton, SimpleListLoading, TextField, TopToolbar, useListContext, useRecordContext, useRedirect, useStore
} from 'react-admin';
import { BulkActionButtons } from '../misc/BulkActionButtons';
import VisibilityIcon from '@mui/icons-material/Visibility';
import EditIcon from '@mui/icons-material/Edit';
import { Children } from 'react';

const ChallengeModelListContent = () => {

    const {
        data: chmodels,
        isLoading,
        onToggleItem,
        selectedIds,
    } = useListContext<any>();
    if (isLoading) {
        return <SimpleListLoading hasLeftAvatarOrIcon hasSecondaryText />;
    }
    let groupChallenges: string[] = ['groupCompetitivePerformance', 'groupCompetitiveTime', 'groupCooperative'];

    let result:any=[];
    chmodels.forEach(chm=> {
        if (!groupChallenges.includes(chm.name)) {
            result.push(chm);
        }
    })

    
    return (
        <>
           <Datagrid
                data={result}
                placeholder='Model Name'
                bulkActionButtons={<BulkActionButtons title="Challenge Model" resource="challengemodels" />}
                // isRowSelectable={record => (!groupChallenges.includes(record.name))} 
                sx={{
                    '& .RaDatagrid-headerCell': {
                        fontWeight: 600,
                    },

                }}>
                <TextField label="Model Name" source="name" />
                <span style={{ float: 'right' }}>
                    <ShowButton />
                    <EditButton />
                </span>
                {/* <CustomItem /> */}
            </Datagrid>
        </>
    );

    // return (
    //     <>
    //         <BulkActionsToolbar>
    //             <BulkActionButtons title="Challenge Model" resource="challengemodels" />
    //         </BulkActionsToolbar>
    //          Model Name
    //         <List >
    //             {chmodels.map(model => (
    //                 <RecordContextProvider>
    //                     {!groupChallenges.includes(model.id) && <ListItem dense={true} sx={{ pt: 0, pb: 0, borderBottom: 1, borderColor: 'grey.300' }} >
    //                         <Checkbox
    //                             edge="start"
    //                             checked={selectedIds.includes(model.id)}
    //                             tabIndex={-1}
    //                             disableRipple
    //                             onClick={e => {
    //                                 e.stopPropagation();
    //                                 onToggleItem(model.id);
    //                             }}
    //                         />

    //                         <ListItemText
    //                             primary={`${model.name}`}
    //                         />
    //                         <ButtonPanel id={model.id} />

    //                     </ListItem>
    //                     }

    //                 </RecordContextProvider>
    //             ))}

    //         </List>
    //     </>
    // );
}

const ButtonPanel = (params: any) => {
    const redirect = useRedirect();
    return (
        <>
            <Button endIcon={<VisibilityIcon />} onClick={() => {
                redirect('/challengemodels/' + params.id + '/show');
            }}>
               Show
            </Button>

            <Button endIcon={<EditIcon />} onClick={() => {
                redirect('/challengemodels/' + params.id + '/edit');
            }}>
               Edit
            </Button>
        </>
    );

}

const CustomItem = (params: any) => {
    const record = useRecordContext();
    if (!record) return null;
    if (record.name === "groupCompetitivePerformance" ||
        record.name === "groupCompetitiveTime" ||
        record.name === "groupCooperative") {
        return (
            <>
            </>
        )
        } else {
            return (
                <>
                   
                    <span style={{ float: 'right' }}>
                        <ButtonPanel />
                    </span>
                </>
        
            );
        }
    
}

const CustomDataGridBulkAction = () => {
    const record = useRecordContext();
    if (!record) return null;
    if (record.name === "groupCompetitivePerformance" ||
        record.name === "groupCompetitiveTime" ||
        record.name === "groupCooperative") {
        return (
            false
        );
    }
    return (
        <BulkActionButtons title="Challenge Model" resource="challengemodels" />
    );
}

const ChallengeModelListActions = () => (
    <TopToolbar>
        <CreateButton
            variant="contained"
            label="New Challenge model"
            sx={{ marginLeft: 2 }}
        />
    </TopToolbar>
);

export const ChallengeModelList = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };
    return (
        <RaList
            queryOptions={options}
            actions={<ChallengeModelListActions />}
            perPage={25}
            pagination={<Pagination rowsPerPageOptions={[10, 25, 50, 100]} />}
            sort={{ field: 'last_seen', order: 'DESC' }}
        >
            <ChallengeModelListContent />
        </RaList>
    );
};
