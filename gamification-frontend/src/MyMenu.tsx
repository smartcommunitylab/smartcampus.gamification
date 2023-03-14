
import { MenuItemLink, useCreatePath, useGetResourceLabel, useResourceDefinitions, useStore } from 'react-admin';
import GamesIcon from '@mui/icons-material/Games';

export const MyMenu = () => {

    const resources = useResourceDefinitions();
    const getResourceLabel = useGetResourceLabel();
    const createPath = useCreatePath();
    const listHiddenMenu = ['challenges'];

    const [gameId, getGameId] = useStore('game.selected');

    return (Object.keys(resources)
        .filter(name => name === 'game' || (gameId && !listHiddenMenu.includes(name)))
        .map(name => {
            if (name == 'game') {
                return (<MenuItemLink style={styles.align} leftIcon={<GamesIcon fontSize='small'/>} key={name} to={createPath({ resource: name, type: 'list', })} state={{ _scrollToTop: true }} primaryText={getResourceLabel(name, 2)} />)
            }
            return (<MenuItemLink style={styles.align50} key={name} to={createPath({ resource: name, type: 'list', })} state={{ _scrollToTop: true }} primaryText={getResourceLabel(name, 2)} />)
        }

        ))
}


const styles = {
    align50: {
        padding: '5px 60px'
    },
    align: {
        padding: '5px 5px'        
    },
    icon: {
        minWidth: '0px'
    }
}