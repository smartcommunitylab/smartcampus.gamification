package it.smartcommunitylab.gamification.rule_manager;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

class CliOptions {
    private Map<String, String> options = new HashMap<>();

    private static final String OPTION_PREFIX = "--";
    public enum Options {
    	URL("url", true), USERNAME("username", true), PASSWORD("password",
                true), GAME("game",true), FROM("from", false);

        private String value;
        private boolean required;

        private Options(String value, boolean required) {
            this.value = value;
            this.required = required;
        }

        public String getValue() {
            return value;
        }

        public boolean isRequired() {
            return required;
        }
    }

    public CliOptions(String[] cliArgs) {
        this.options = getOptions(cliArgs);
        validate(this.options);
    }

    private void validate(Map<String, String> options) {
        for (Options opt : Options.values()) {
            if (opt.isRequired() && options.get(opt.getValue()) == null) {
                throw new IllegalArgumentException(
                        String.format("%s option is required", opt.getValue()));
            }
        }
    }

    public String get(Options option) {
        return options.get(option.getValue());
    }

    public String[] getAsArray(Options option) {
        String valuesAsString = options.get(option.getValue());
        if (valuesAsString != null) {
            String[] values = valuesAsString.split(",");
            return Stream.of(values).filter(v -> !v.isEmpty()).map(v -> v.trim())
                    .toArray(String[]::new);
        }
        return null;
    }

    private final Map<String, String> getOptions(String[] args) {
        Map<String, String> options = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String option = args[i];
            if (option.startsWith(OPTION_PREFIX)) {
                options.put(option.substring(2), args[i + 1]);
            }
        }
        return options;
    }
}
