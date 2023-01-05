import {
    CreateButton, Datagrid, List as RaList, Pagination, SimpleListLoading, TextField, TopToolbar, useListContext, useStore
} from 'react-admin';
import { Action } from '../types';
import { BulkActionButtons } from '../misc/BulkActionButtons';

const ActionsListContent = () => {
    const {
        data: Actions,
        isLoading,
        onToggleItem,
        selectedIds,
    } = useListContext<Action>();
    if (isLoading) {
        return <SimpleListLoading hasLeftAvatarOrIcon hasSecondaryText />;
    }
    const now = Date.now();

    return (
        <>
            <Datagrid
                bulkActionButtons={<BulkActionButtons title="Action" resource="actions" />}
                sx={{
                    '& .RaDatagrid-headerCell': {
                        fontWeight: 600
                    },

                }}>
                <TextField label="Action name" source="id" />
            </Datagrid>

        </>
    );
};

const ActionListActions = () => (
    <TopToolbar>
        <CreateButton
            variant="contained"
            label="New Action"
            sx={{ marginLeft: 2 }}
        />
    </TopToolbar>
);

export const ActionList = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };

    return (
        <RaList
            queryOptions={options}
            actions={<ActionListActions />}
            perPage={20}
            pagination={<Pagination rowsPerPageOptions={[10, 20, 30, 40]} />}
            sort={{ field: 'last_seen', order: 'DESC' }}
        >
            <ActionsListContent />
        </RaList>
    )
};


