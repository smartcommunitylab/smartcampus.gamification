import {
    List as RaList,
    SimpleListLoading,
    useListContext, Pagination, Datagrid,
    TextField,
    useStore,
    ShowButton
} from 'react-admin';

const MonitorListContent = () => {
    const {
        data: any,
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
            <Datagrid bulkActionButtons={false} sx={{
                '& .RaDatagrid-headerCell': {
                    fontWeight: 600
                },
            }}>
                <TextField label="Player" source="playerId" />
                <span style={{ float: 'right' }}>
                    <ShowButton />
                </span>
            </Datagrid>

        </>
    );
};

export const MonitorList = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };
    return (
        <RaList
            queryOptions={options}
            actions={<></>}
            perPage={20}
            pagination={<Pagination rowsPerPageOptions={[10, 20, 30, 40]} />}
            sort={{ field: 'last_seen', order: 'DESC' }}
        >
            <MonitorListContent />
        </RaList>
    )
};


