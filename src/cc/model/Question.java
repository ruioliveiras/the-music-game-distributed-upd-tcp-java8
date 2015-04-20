package cc.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author ruioliveiras
 */
public class Question {

    /**
     * The text of the Question
     */
    public String question;
    /**
     * All the possibles answers
     */
    public String[] answer;
    /**
     * The index in the array answer of the correct answer
     */
    public int correct;
    /**
     * Image Path of this question, null if don't have one
     */
    public String imagePath;
    /**
     * Music path of the Music, null if don't have one
     */
    public String musicPath;

    /**
     * Byte array of this question's Image
     */
    public byte[] imageArray;

    /**
     * Byte array of this question's Music
     */
    public byte[] musicArray;

    /**
     * Main constructor
     *
     * @param question
     * @param anwser
     * @param correct
     * @param imagePath
     * @param musicPath
     */
    public Question(String question, String[] anwser, int correct, String imagePath, String musicPath) {
        this.question = question;
        this.answer = anwser;
        this.correct = correct;
        this.imagePath = imagePath;
        this.musicPath = musicPath;
//        this.imageArray = imageArray;
//        this.musicArray = musicArray;
    }

    public Question(String question, String[] anwser, int correct, byte[] imageArray, byte[] musicArray) {
        this.question = question;
        this.answer = anwser;
        this.correct = correct;
        this.imagePath = null;
        this.musicPath = null;
        this.imageArray = imageArray;
        this.musicArray = musicArray;
    }

    public void loadImage () {
        if (imageArray != null) {
            return;
        }
        try {
            Path path = Paths.get(imagePath);
            imageArray = Files.readAllBytes(path);
        } catch (IOException ex) {
            throw new RuntimeException("reading Image");
        }
    }

    public void loadMusic() {
        if (musicArray != null) {
            return;
        }
        try {
            Path path = Paths.get(musicPath);
            musicArray = Files.readAllBytes(path);
        } catch (IOException ex) {
            throw new RuntimeException("reading music");
        }
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnwser() {
        return answer;
    }

    public int getCorrect() {
        return correct;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public byte[] getMusicArray() {
        return musicArray;
    }

    public byte[] getImageArray() {
        return imageArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(question).append("\n")
                .append(answer[0]).append(" , ")
                .append(answer[1]).append(" , ")
                .append(answer[2]).append("\n")
                .append(correct).append("\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return getQuestion().hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Question){
            return ((Question)obj).getQuestion().equals(getQuestion());
        }
        return false;
    }

    
}
