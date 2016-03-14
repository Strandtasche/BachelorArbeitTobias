package com.saibot1207;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> paths = new ArrayList<String>();

        try {
            Files.walk(Paths.get("/Users/saibot1207/Documents/BA/Nutzerdaten/")).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    paths.add((filePath.toString()));
                }
            });

//			File folder = new File("/Users/saibot1207/Documents/BA/Auswertung/");
//			File[] listOfFiles = folder.listFiles();
//
//			for (File file : listOfFiles) {
//				if (file.isFile()) {
//						(file.getName())
//				}
//			}

        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] array = null;

        System.out.println("Files: " + paths.size());
        int filecounter = 0;

        for (String p : paths) {
            String s = readStringFromFile(p);

            //System.out.println(s);
            HashMap<Integer, Notification> hmap = new HashMap<>();

            array = s.split("(\\r?\\n)|,|(\t)");
            for (int i = 0; i < array.length; i++) {
                array[i] = array[i].substring(1, array[i].length() - 1);
            }

            long timeMin = Long.MAX_VALUE;
            long timeMax = Long.MIN_VALUE;
            long timeDiff;



            for (int i = 5; i < array.length; i = i + 5) {

                int id;
                int textlength;
                long time;
                try {
                    id = Integer.parseInt(array[i]);
                    textlength = Integer.parseInt(array[i + 3]);
                    time = Long.parseLong(array[i + 4]);
                }
                catch (NumberFormatException e) {
                    id = -1;
                    textlength = 0;
                    time = 0;
                }

                if (time != 0 && time > timeMin) {
                    timeMin = time;
                }
                if (time != 0 && time < timeMax){
                    timeMax = time;
                }

                String app = array[i + 1];
                String title = array[i + 2];


                Notification notification = new Notification(id, app, title, textlength, time);
                hmap.put(id, notification);


            }

            timeDiff = timeMax - timeMin;

            ArrayList<Notification> notiHangout = new ArrayList<>();
            ArrayList<Notification> notiWhatsapp = new ArrayList<>();
            ArrayList<Notification> notiSignal = new ArrayList<>();
            ArrayList<Notification> notiTelegram = new ArrayList<>();
            ArrayList<Notification> notiSkype = new ArrayList<>();
            ArrayList<Notification> notiFBApp = new ArrayList<>();
            ArrayList<Notification> notiFBMess = new ArrayList<>();
            ArrayList<Notification> notiTwitter = new ArrayList<>();
            ArrayList<Notification> notiOther = new ArrayList<>();


            Iterator it = hmap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Notification not = (Notification) pair.getValue();
                //System.out.println(pair.getKey() + " = " + not.getString());
                switch (not.getApp()) {
                    case HANGOUTS:
                        notiHangout.add(not);
                        break;
                    case WHATSAPP:
                        notiWhatsapp.add(not);
                        break;
                    case SIGNAL:
                        notiSignal.add(not);
                        break;
                    case TELEGRAM:
                        notiTelegram.add(not);
                        break;
                    case SKYPE:
                        notiSkype.add(not);
                        break;
                    case FACEBOOKAPP:
                        notiFBApp.add(not);
                        break;
                    case FACEBOOKMESS:
                        notiFBMess.add(not);
                        break;
                    case TWITTER:
                        notiFBApp.add(not);
                        break;
                    default:
                        notiOther.add(not);
                        break;
                }
                it.remove(); // avoids a ConcurrentModificationException
            }

            String output = "";
            output += ("hangouts: \n" + "number No: " + notiHangout.size() + "\n");
            output += "number conv: " + calculateConversations(notiHangout) + "\n";
            output += "max Conv: " + calculateMaxConversationSize(notiHangout) + "\n";
            if (calculateMaxConversationSize(notiHangout) == 0) {
                output += "relative not/conv: " + 0 + "\n";
            }
            else {
                output += "relative not/conv: " + notiHangout.size() / calculateConversations(notiHangout) + "\n";
            }
            output += "avg messLength: " + calculateAvgLength(notiHangout) + "\n\n";

            output += ("whatsapp: \n" + "number No: " + notiWhatsapp.size() + "\n");
            output += "number conv: " + calculateConversations(notiWhatsapp) + "\n";
            output += "max Conv: " + calculateMaxConversationSize(notiWhatsapp) + "\n";
            if (calculateMaxConversationSize(notiWhatsapp) == 0){
                output += "relative not/conv: " + 0 + "\n";
            }
            else {
                output += "relative not/conv: " + notiWhatsapp.size() / calculateConversations(notiWhatsapp) + "\n";
            }
            output += "avg messLength: " + calculateAvgLength(notiWhatsapp) + "\n\n";

            output += ("signal: \n" + "number No: " + notiSignal.size() + "\n");
            output += "number conv: " + calculateConversations(notiSignal) + "\n";
            output += "max Conv: " + calculateMaxConversationSize(notiSignal) + "\n";
            if (calculateMaxConversationSize(notiSignal) == 0){
                output += "relative not/conv: " + 0 + "\n";
            }
            else {
                output += "relative not/conv: " + notiSignal.size() / calculateConversations(notiSignal) + "\n";
            }
            output += "avg messLength: " + calculateAvgLength(notiSignal) + "\n\n";

            output += ("telegram: \n" + "number No: " + notiTelegram.size() + "\n");
            output += "number conv: " + calculateConversations(notiTelegram) + "\n";
            output += "max Conv: " + calculateMaxConversationSize(notiTelegram) + "\n";
            if (calculateMaxConversationSize(notiTelegram) == 0){
                output += "relative not/conv: " + 0 + "\n";
            }
            else {
                output += "relative not/conv: " + notiTelegram.size() / calculateConversations(notiTelegram) + "\n";
            }
            output += "avg messLength: " + calculateAvgLength(notiTelegram) + "\n\n";

            output += ("skype: \n" + "number No: " + notiSkype.size() + "\n");
            output += "number conv: " + calculateConversations(notiSkype) + "\n";
            output += "max Conv: " + calculateMaxConversationSize(notiSkype) + "\n";
            if (calculateMaxConversationSize(notiSkype) == 0){
                output += "relative not/conv: " + 0 + "\n";
            }
            else {
                output += "relative not/conv: " + notiSkype.size() / calculateConversations(notiSkype) + "\n";
            }
            output += "avg messLength: " + calculateAvgLength(notiSkype) + "\n\n";

            output += ("FBApp: \n" + "number No: " + notiFBApp.size() + "\n");
            output += "number conv: " + calculateConversations(notiFBApp) + "\n";
            output += "max Conv: " + calculateMaxConversationSize(notiFBApp) + "\n";
            if (calculateMaxConversationSize(notiFBApp) == 0){
                output += "relative not/conv: " + 0 + "\n";
            }
            else {
                output += "relative not/conv: " + notiFBApp.size() / calculateConversations(notiFBApp) + "\n";
            }
            output += "avg messLength: " + calculateAvgLength(notiFBApp) + "\n\n";

            output += ("FBMess: \n" + "number No: " + notiFBMess.size() + "\n");
            output += "number conv: " + calculateConversations(notiFBMess) + "\n";
            output += "max Conv: " + calculateMaxConversationSize(notiFBMess) + "\n";
            if (calculateMaxConversationSize(notiFBMess) == 0){
                output += "relative not/conv: " + 0 + "\n";
            }
            else {
                output += "relative not/conv: " + notiFBMess.size() / calculateConversations(notiFBMess) + "\n";
            }
            output += "avg messLength: " + calculateAvgLength(notiFBMess) + "\n\n";

            output += ("Twitter: \n" + "number No: " + notiTwitter.size() + "\n");
            output += "number conv: " + calculateConversations(notiTwitter) + "\n";
            output += "max Conv: " + calculateMaxConversationSize(notiTwitter) + "\n";
            if (calculateMaxConversationSize(notiTwitter) == 0){
                output += "relative not/conv: " + 0 + "\n";
            }
            else {
                output += "relative not/conv: " + notiTwitter.size() / calculateConversations(notiTwitter) + "\n";
            }
            output += "avg messLength: " + calculateAvgLength(notiTwitter) + "\n\n";

            output += ("Other: \n" + "number No: " + notiOther.size() + "\n");
            output += "number conv: " + calculateConversations(notiOther) + "\n";
            output += "max Conv: " + calculateMaxConversationSize(notiOther) + "\n";
            if (calculateMaxConversationSize(notiOther) == 0){
                output += "relative not/conv: " + 0 + "\n";
            }
            else {
                output += "relative not/conv: " + notiOther.size() / calculateConversations(notiOther) + "\n";
            }
            output += "avg messLength: " + calculateAvgLength(notiOther) + "\n\n";

//            System.out.println("hangouts: " + notiHangout.size());
//            System.out.println("whatsapp: " + notiWhatsapp.size());
//            System.out.println("signal: " + notiSignal.size());
//            System.out.println("telegram: " + notiTelegram.size());
//            System.out.println("skype: " + notiSkype.size());
//            System.out.println("FBApp: " + notiFBApp.size());
//            System.out.println("FBMess: " + notiFBMess.size());
//            System.out.println("Twitter: " + notiTwitter.size());
//            System.out.println("other: " + notiOther.size());

            System.out.print(output);



            int dist = 0;

//            try {
//                PrintWriter out = new PrintWriter("/Users/saibot1207/Documents/BA/AusgabeNutzerDaten/Line" + dist + ".txt");
//                //out.println(saveToFile);
//                out.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
            filecounter++;
        }


        //System.out.println(saveToFile);

        array = null;
    }



    public static String readStringFromFile(String path) {
        byte[] encoded = null;
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded);
    }

    public static int calculateConversations(ArrayList<Notification> list) {
        HashMap<String, Integer> hmap = new HashMap<>();
        for (Notification noti : list) {
            if (!hmap.containsKey(noti.getTitle())) {
                hmap.put(noti.getTitle(), 1);
            }
            else {
                hmap.put(noti.getTitle(), hmap.get(noti.getTitle())+ 1);
            }
        }
        return hmap.size();
    }

    public static int calculateMaxConversationSize(ArrayList<Notification> list) {
        HashMap<String, Integer> hmap = new HashMap<>();
        for (Notification noti : list) {
            if (!hmap.containsKey(noti.getTitle())) {
                hmap.put(noti.getTitle(), 1);
            }
            else {
                hmap.put(noti.getTitle(), hmap.get(noti.getTitle())+ 1);
            }
        }
        if (hmap.size() == 0){
            return 0;
        }
        else {
            return (Collections.max(hmap.values()));
        }

    }

    public static double calculateAvgLength(ArrayList<Notification> list) {
        int sum = 0;
        for (Notification noti : list) {
            sum += noti.getTextlength();
        }
        if (list.size() == 0){
            return 0;
        }
        else{
            return sum/list.size();
        }
    }



}
