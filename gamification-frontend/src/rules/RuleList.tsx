import {
    List as RaList,
    SimpleListLoading, TextField,
    useListContext, TopToolbar,
    CreateButton,
    Pagination, Datagrid,
    EditButton,
    ShowButton,
    useStore
} from 'react-admin';
import { BulkActionButtons } from '../misc/BulkActionButtons';

const RuleListContent = () => {

    const {
        data: rules,
        isLoading,
        onToggleItem,
        selectedIds,
    } = useListContext<any>();
    if (isLoading) {
        return <SimpleListLoading hasLeftAvatarOrIcon hasSecondaryText />;
    }
    const now = Date.now();
    return (
        <>
            <Datagrid
                bulkActionButtons={<BulkActionButtons title="Rule" resource="rules" />}
                sx={{
                    '& .RaDatagrid-headerCell': {
                        fontWeight: 600,
                    },

                }}>
                <TextField label="Rule Name" source="name" />
                <span style={{ float: 'right' }}>
                    <ShowButton />&nbsp;&nbsp;&nbsp;
                    <EditButton />
                </span>
            </Datagrid>
        </>
    );
};

const RuleListActions = () => (
    <TopToolbar>
        <CreateButton
            variant="contained"
            label="New Rule"
            sx={{ marginLeft: 2 }}
        />
    </TopToolbar>
);

export const RuleList = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };

    return (
        <RaList
            queryOptions={options}
            actions={<RuleListActions />}
            perPage={25}
            pagination={<Pagination rowsPerPageOptions={[10, 25, 50, 100]} />}
            sort={{ field: 'last_seen', order: 'DESC' }}
        >
            <RuleListContent />
        </RaList>
    );
};

