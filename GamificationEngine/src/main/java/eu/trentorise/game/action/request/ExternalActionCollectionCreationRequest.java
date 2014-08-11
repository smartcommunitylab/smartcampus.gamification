package eu.trentorise.game.action.request;

/**
 *
 * @author Luca Piras
 */
public class ExternalActionCollectionCreationRequest {
    
    protected String fileContent;

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public String toString() {
        return "ExternalActionsRequest{" + "fileContent=" + fileContent + '}';
    }
}