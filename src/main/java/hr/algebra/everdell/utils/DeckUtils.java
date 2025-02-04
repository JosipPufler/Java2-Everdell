package hr.algebra.everdell.utils;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.models.BaseCard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DeckUtils {
    private DeckUtils(){}
    static final File root = new File("");
    static final File CRITTER_FOLDER = new File(root.getAbsolutePath(), "src\\main\\java\\hr\\algebra\\everdell\\models\\cards\\critters");
    static final File CONSTRUCT_FOLDER = new File(root.getAbsolutePath(), "src\\main\\java\\hr\\algebra\\everdell\\models\\cards\\constructs");
    static final String CRITTER_START = "hr.algebra.everdell.models.cards.critters.";
    static final String CONSTRUCT_START = "hr.algebra.everdell.models.cards.constructs.";

    public static List<Card> generateDeck() throws Exception{
        List<String> critterList = new ArrayList<>(Stream.of(Objects.requireNonNull(CRITTER_FOLDER.listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName).toList());
        List<String> constructList = new ArrayList<>(Stream.of(Objects.requireNonNull(CONSTRUCT_FOLDER.listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName).toList());
        List<Card> deck = new ArrayList<>();
        critterList.replaceAll(s -> CRITTER_START + s.split("\\.")[0]);

        constructList.replaceAll(s -> CONSTRUCT_START + s.split("\\.")[0]);
        List<String> classList = new ArrayList<>(critterList);
        classList.addAll(constructList);
        for (String file : classList){
            Class<?> aClass = Class.forName(file);
            BaseCard o = (BaseCard) aClass.getDeclaredConstructor().newInstance();
            deck.add(o);
            for (int i = 0 ; i < o.getNumberInDeck()-1 ; i++){
                deck.add((BaseCard) aClass.getDeclaredConstructor().newInstance());
            }
        }
        System.out.println(deck.size());
        return deck;
    }
}
