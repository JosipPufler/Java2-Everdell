package hr.algebra.everdell.utils;

import hr.algebra.everdell.models.Card;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DeckUtils {
    private DeckUtils(){}

    static final String critterPath = "C:\\Users\\josip\\IdeaProjects\\everdell\\src\\main\\java\\hr\\algebra\\everdell\\models\\cards\\critters";
    static final String constructPath = "C:\\Users\\josip\\IdeaProjects\\everdell\\src\\main\\java\\hr\\algebra\\everdell\\models\\cards\\constructs";
    static final String critterStart = "hr.algebra.everdell.models.cards.critters.";
    static final String constructStart = "hr.algebra.everdell.models.cards.constructs.";

    public static List<Card> generateDeck() throws Exception{
        List<String> critterList = new ArrayList<>(Stream.of(new File(critterPath).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName).toList());
        List<String> constructList = new ArrayList<>(Stream.of(new File(constructPath).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName).toList());
        List<Card> deck = new ArrayList<>();
        for (int i = 0; i < critterList.size(); i++) {
            critterList.set(i, critterStart + critterList.get(i).split("\\.")[0]);
        }

        for (int i = 0; i < constructList.size(); i++) {
            constructList.set(i, constructStart + constructList.get(i).split("\\.")[0]);
        }
        List<String> classList = new ArrayList<>(critterList);
        classList.addAll(constructList);
        for (String file : classList){
            System.out.println(file);
            Class<?> aClass = Class.forName(file);
            Card o = (Card) aClass.newInstance();
            deck.add(o);
            for (int i = 0 ; i < o.getNumberInDeck()-1 ; i++){
                deck.add((Card) aClass.newInstance());
            }
        }
        System.out.println(deck.size());
        return deck;
    }
}
