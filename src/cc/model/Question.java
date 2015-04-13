package cc.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
     * @param imageArray
     * @param musicArray
     */
    public Question(String question, String[] anwser, int correct, String imagePath, String musicPath, byte[] imageArray, byte[] musicArray) {
        this.question = question;
        this.answer = anwser;
        this.correct = correct;
        this.imagePath = imagePath;
        this.musicPath = musicPath;
        this.imageArray = imageArray;
        this.musicArray = musicArray;
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
    

    public byte[] getMusicArray(){
        return musicArray;
    }
    
    public byte[] getImageArray(){
        return imageArray;

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(question + "\n");
        sb.append(answer[0] + " , " + answer[1] + " , " + answer[2] + "\n");
        sb.append(correct + "\n");
        return sb.toString();
    }
}
