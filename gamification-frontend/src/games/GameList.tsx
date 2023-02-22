import {
    List as RaList,
    SimpleListLoading,
    useListContext, TopToolbar,
    CreateButton,
    Pagination,
    BulkActionsToolbar,
    BulkDeleteButton,
    RecordContextProvider,
    TextInput,
    SearchInput,
    ExportButton,
    useDataProvider,
    useNotify
} from 'react-admin';
import { useRedirect } from 'react-admin';
import {
    List,
    ListItem,
    ListItemText,
    Button
} from '@mui/material';
import CheckCircleOutlinedIcon from '@mui/icons-material/CheckCircleOutlined';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import SettingsIcon from '@mui/icons-material/Settings';
import { Game } from '../types';

const GameListContent = () => {
    const {
        data: Games,
        isLoading,
        onToggleItem,
        selectedIds,
    } = useListContext<Game>();
    if (isLoading) {
        return <SimpleListLoading hasLeftAvatarOrIcon hasSecondaryText />;
    }
    const now = Date.now();

    return (
        <>
            <BulkActionsToolbar>
                <BulkDeleteButton />
            </BulkActionsToolbar>
            <List>
                {Games.map(Game => (
                    <RecordContextProvider key={Game.id} value={Game}>
                        <ListItem>
                            <ListItemText primary={`${Game.name}`} secondary={`${Game.id}`}></ListItemText>
                            <SettingButton selectedId={Game.id} />
                            <ManageButton selectedId={Game.id} selectedName={Game.name}></ManageButton>
                        </ListItem>
                    </RecordContextProvider>
                ))}
            </List>
        </>
    );
};

const GameListActions = () => (
    <TopToolbar>
        <ExportGameButton />
        <CreateButton
            variant="contained"
            label="New Game"
            sx={{ marginLeft: 2 }}
        />
    </TopToolbar>
);

export const GameList = () => {
    return (
        <RaList
            actions={<GameListActions />}
            perPage={25}
            pagination={<Pagination rowsPerPageOptions={[10, 25, 50, 100]} />}
            sort={{ field: 'last_seen', order: 'DESC' }}
            filters={GameFilters}
        >
            <GameListContent />
        </RaList>
    )
};

import { useStore } from 'react-admin';

const ManageButton = (params: any) => {
    const [gameId, setGameId] = useStore('game.selected');
    const [gameName, setGameName] = useStore('game.name');
    return (
        <>
            <Button endIcon={<CheckCircleOutlinedIcon />} onClick={() => {
                setGameId(params.selectedId);
                setGameName(params.selectedName);
                console.log(params.selectedId);
            }}>
                Manage
            </Button>
        </>
    );

};

const SettingButton = (params: any) => {
    const redirect = useRedirect();
    const [gameId, getGameId] = useStore('game.selected');
    return (
        <>
            <Button disabled={params.selectedId != gameId} endIcon={<SettingsIcon />} onClick={() => {
                redirect('/game/' + params.selectedId + '/edit');
            }}>
                Edit Settings
            </Button>
        </>
    );
}

const GameFilters = [
    <SearchInput placeholder='Search by game name'source="q" alwaysOn />
];

const ExportGameButton = (params: any) => {
    const dataProvider = useDataProvider();
    const notify = useNotify();
    const exportGames = function () {
        const req = {
            path: 'exportJsonDB',
            // options: { method: 'GET' },
            // body: JSON.stringify(data),
        };

        dataProvider
            .invoke(req)
            .then(async function (response: any) {
                notify("Exported successfully to " + response)
            }).catch(function (error: any) {
                notify(error.toString())
            })
    }

    return (
        <>
            <Button 
            sx={{
                color: 'white',
                backgroundColor: '#1976d2',
                boxShadow: '0px 3px 1px -2px rgb(0 0 0 / 20%), 0px 2px 2px 0px rgb(0 0 0 / 14%), 0px 1px 5px 0px rgb(0 0 0 / 12%)',
                marginLeft: '16px',
                lineHeight: '1.5',
                fontWeight: '500',
                fontSize: '0.8125rem',
                minWidth: '64px',
                padding: '4px 10px',
                borderRadius: '4px'
            }} 
            endIcon={<FileDownloadIcon />} onClick={exportGames}
            >
                Export Games
            </Button>
        </>
    );
};
