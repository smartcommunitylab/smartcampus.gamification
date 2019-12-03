package it.smartcommunitylab.gamification.log2elastic.analyzer;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import it.smartcommunitylab.gamification.log2elastic.Config;

public class ConfigTest {

    @Test
    public void elastic7_flag_true_by_default() {
        String[] oneCliOption = new String[] {"path_to_log_folder"};
        Config config = new Config(oneCliOption);
        assertThat(config.pushToElastic7(), is(true));
    }

    @Test
    public void elastic7_flag_true_if_second_option_is_invalid() {
        String[] invalidSecondCliOption = new String[] {"path_to_log_folder", "no_valid_option"};
        Config config = new Config(invalidSecondCliOption);
        assertThat(config.pushToElastic7(), is(true));
    }


    @Test
    public void push_to_elastic5_server() {
        String[] elastic5OnOption = new String[] {"path_to_log_folder", "--elastic5"};
        Config config = new Config(elastic5OnOption);
        assertThat(config.pushToElastic7(), is(false));
    }
}
