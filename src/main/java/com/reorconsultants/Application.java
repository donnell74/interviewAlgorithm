package com.reorconsultants;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.reorconsultants.GitHubAnalyzerService;

public class Application {
    private static Properties properties;

    public static void main(String[] args) {
        loadProperties();
        String git_username = properties.getProperty("git_username");
        String git_password = properties.getProperty("git_password");

        GitHubAnalyzerService gitHubAnalyzerService =
            new GitHubAnalyzerService(git_username, git_password);
    }

    private static void loadProperties() {
        properties = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("config.properties");
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
              try {
                input.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
        }
    }
}
