package it.smartcommunitylab.gamification.log2elastic;

public class Config {
    private String logFolderPath;
    private boolean pushToElastic7 = true;


    public Config(String[] commandLineArgs) {
        logFolderPath = commandLineArgs[0];
        pushToElastic7 = commandLineArgs.length == 1 || !commandLineArgs[1].equals("--elastic5");
    }


    public String getLogFolderPath() {
        return logFolderPath;
    }


    public boolean pushToElastic7() {
        return pushToElastic7;
    }

}
