package com.yashasvi.bloggingapp.blogs.summarizer;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class OpenNLPTextSummarizer implements TextSummarizer {
    private SentenceDetectorME sentenceDetector;

    public OpenNLPTextSummarizer() {
        // Initialize the sentence detector
        try (InputStream modelIn = getClass().getResourceAsStream("/opennlp/opennlp-en-ud-ewt-sentence-1.0-1.9.3.bin");) {
            SentenceModel model = new SentenceModel(modelIn);
            sentenceDetector = new SentenceDetectorME(model);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO: Currently method returns only starting sentences. Update it to make it smart.
     */
    @Override
    public String summarize(String text, int numberOfSentences) {
        // Detect sentences in the text
        String[] sentences = sentenceDetector.sentDetect(text);

        if (numberOfSentences <= 0 || numberOfSentences > sentences.length) {
            numberOfSentences = sentences.length;
        }

        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < numberOfSentences; i++) {
            summary.append(sentences[i]).append(". ");
        }

        return summary.toString();
    }
}
