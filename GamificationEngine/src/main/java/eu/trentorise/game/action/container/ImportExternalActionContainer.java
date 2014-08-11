package eu.trentorise.game.action.container;

/**
 *
 * @author Luca Piras
 */
public class ImportExternalActionContainer implements IImportExternalActionContainer {
    
    protected String fileContent;

    @Override
    public String getFileContent() {
        return fileContent;
    }

    @Override
    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public String toString() {
        return "ImportExternalActionContainer{" + "fileContent=" + fileContent + '}';
    }
}