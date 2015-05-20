/*
        String s1[] = {"resposta p11", "resposta p12", "resposta p13"};
        String s2[] = {"resposta p21", "resposta p22", "resposta p23"};
        String s3[] = {"resposta p31", "resposta p32", "resposta p33"};
        String s4[] = {"resposta p41", "resposta p42", "resposta p43"};
        String s5[] = {"resposta p51", "resposta p52", "resposta p53"};
        
        Question q1 = new Question("Pergunta 1", s1, 1, "", "");
        Question q2 = new Question("Pergunta 2", s2, 1, "", "");
        Question q3 = new Question("Pergunta 3", s3, 1, "", "");
        Question q4 = new Question("Pergunta 4", s4, 1, "", "");
        Question q5 = new Question("Pergunta 5", s5, 1, "", "");
        
        List<Question> l = new ArrayList<>();
        l.add(q1); l.add(q2); l.add(q3); l.add(q4); l.add(q5); 





        //For testing
            for(Question q : l){
                createQuestion(q);
                correctAnswer_index = q.getCorrect();
                System.out.println("Resposta dada: " + answerGiven);
                showResult(answerGiven, correctAnswer_index);
                currentPoints++;
                updateScore(currentPoints, correctAnswer_index);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }


*/

    /*public void doChallenge(String desafio){
        
        int currentPoints = 0, correctAnswer = 0, correctAnswer_index=0, pointsWon = 0;
        String args[] = null;
        int pergunta = 0, answerGiven = 0;
   
        Question actualQ = null;
        
        String s1[] = {"resposta p11", "resposta p12", "resposta p13"};
        String s2[] = {"resposta p21", "resposta p22", "resposta p23"};
        String s3[] = {"resposta p31", "resposta p32", "resposta p33"};
        String s4[] = {"resposta p41", "resposta p42", "resposta p43"};
        String s5[] = {"resposta p51", "resposta p52", "resposta p53"};
        String s6[] = {"resposta p61", "resposta p62", "resposta p63"};
        String s7[] = {"resposta p71", "resposta p72", "resposta p73"};
        String s8[] = {"resposta p81", "resposta p82", "resposta p83"};
        String s9[] = {"resposta p91", "resposta p92", "resposta p93"};
        String s10[] = {"resposta p101", "resposta p102", "resposta p103"};
        
        Question q1 = new Question("Pergunta 1", s1, 1, "", "");
        Question q2 = new Question("Pergunta 2", s2, 1, "", "");
        Question q3 = new Question("Pergunta 3", s3, 1, "", "");
        Question q4 = new Question("Pergunta 4", s4, 1, "", "");
        Question q5 = new Question("Pergunta 5", s5, 1, "", "");
        Question q6 = new Question("Pergunta 6", s6, 1, "", "");
        Question q7 = new Question("Pergunta 7", s7, 1, "", "");
        Question q8 = new Question("Pergunta 8", s8, 1, "", "");
        Question q9 = new Question("Pergunta 9", s9, 1, "", "");
        Question q10 = new Question("Pergunta 10", s10, 1, "", "");
        
        List<Question> l = new ArrayList<>();
        l.add(q1); l.add(q2); l.add(q3); l.add(q4); l.add(q5); l.add(q6); l.add(q7); l.add(q8); l.add(q9); l.add(q10); 
        
        MainInterface mInt = new MainInterface(this);
        mInt.setVisible(true);
             
        
        /*for(pergunta=0; pergunta<10; pergunta++){
            
            actualQ = getNextQuestion();
            answerGiven = mInt.createQuestion(actualQ);
            correctAnswer_index = actualQ.getCorrect();
            
            
            //falta enviar o datagrama com os valores passados como bytes
            //valor escolhar é variavel answerGiven, valor questao é a variavel pergunta
            //makeDatagramAnswer(Byte escolha, desafio, Byte questao);
            
            PDU receive = udp_com.nextPDU();

            correctAnswer = (byte) receive.popParameter(PDUType.REPLY_CORRECT);
            pointsWon = (byte) receive.popParameter(PDUType.REPLY_POINTS);
            
            currentPoints += pointsWon;
            
            mInt.showResult(answerGiven, correctAnswer_index);
            mInt.updateScore(currentPoints);
            
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
            //For testing
        /*    for(Question q : l){
                answerGiven = mInt.createQuestion(q);
                correctAnswer_index = q.getCorrect();
                System.out.println("Resposta dada: " + answerGiven);
                mInt.showResult(answerGiven, correctAnswer_index);
                currentPoints++;
                mInt.updateScore(currentPoints);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        //ao inicio do metodo, iniciar a interface
        //meter a esperar pelo proximo pdu... se calhar mete-se o controlo do erro no metodo de receber a questao
        //enquanto nao tiver feito as 10 perguntas, fazer sempre o mesmo ciclo
        //depois do ciclo, receber o pdu com os pontos que amealhou
        
        
    }*/
    

/*
    public void notifyObject(){
         synchronized(obj){
            obj.notify(); 
        }
    }

*/


        
        /*File songfile = new File(music_url);
        Media media = new Media(songfile.toURI().toString());
        MediaPlayer music_player = new MediaPlayer(media);
        music_player.play();    
        
        while(musicRunning);      
        
        music_player.pause();
        music_player.stop();
        */



        //BufferedImage myPicture = ImageIO.read(new File("src/cc/swinggame/images/uminho.jpg"));
        //Path path = Paths.get("src/cc/swinggame/images/","uminho.jpg");
        //byte[] ar = Files.readAllBytes(path);
        //ImageIcon imageI = new ImageIcon(ar);






