package com.image.autocaption.config;

import com.github.dockerjava.api.model.Image;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;

import java.util.List;

// Make sure you have Docker installed and running locally before running starting the ollama test container
public class GemmaTestContainer {

    private static final String TC_OLLAMA_GEMMA3 = "ollama-cpu-gemma3";

    // Creating an Ollama container with Gemma3:4b if it doesn't exist.
    public static void createGemmaOllamaContainer() {

        // Check if the custom Gemma Ollama image exists already
        List<Image> listImagesCmd = DockerClientFactory.lazyClient()
                .listImagesCmd()
                .withImageNameFilter(TC_OLLAMA_GEMMA3)
                .exec();

        if (listImagesCmd.isEmpty()) {
            System.out.println("Creating a new Ollama container with Gemma 3 image...");
            GenericContainer<?> ollama = new GenericContainer<>("ollama-cpu-gemma")
                    .withExposedPorts(11434)
                    .withCommand("serve");
            System.out.println("Starting Ollama...");
            ollama.start();
        }

    }

}
