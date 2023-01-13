
import { MenuItemLink, useCreatePath, useGetResourceLabel, useResourceDefinitions, useStore } from 'react-admin';

export const MyMenu = () => {
    
    const resources = useResourceDefinitions();
    const getResourceLabel = useGetResourceLabel();
    const createPath = useCreatePath();
    const listHiddenMenu = ['challenges'];

    const [gameId, getGameId] = useStore('game.selected');
    
    return (Object.keys(resources)
        .filter(name => name==='game' || ( gameId && !listHiddenMenu.includes(name)))
        .map(name => (
            // name==='game'?
            <MenuItemLink
                key={name}
                to={createPath({
                    resource: name,
                    type: 'list',
                })}
                state={{ _scrollToTop: true }}
                primaryText={getResourceLabel(name, 2)
                }
            />
            // :null
        )
        )

    )
     
}