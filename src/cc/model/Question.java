/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.model;

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
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(question + "\n");
        sb.append(answer[0] + " , " + answer[1] + " , " + answer[2] + "\n");
        sb.append(correct + "\n");
        return sb.toString();
    }
}
