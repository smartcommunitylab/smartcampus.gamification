import { SimpleForm, FileInput, FileField, Create } from 'react-admin';

export const GameCreate = () => {
    return (
        <Create redirect="list">
            <SimpleForm>
                <FileInput  multiple={false} source="src" label="Upload file (.json)" isRequired={true} accept="application/json" > 
                    <FileField source="src" title="title" />
                </FileInput>             
            </SimpleForm>
        </Create>
    )
}
