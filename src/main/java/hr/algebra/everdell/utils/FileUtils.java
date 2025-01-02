package hr.algebra.everdell.utils;

import java.io.File;
import java.util.Objects;

public class FileUtils {
    private static final File root = new File("");
    private static final File imageFolder = new File(root.getAbsolutePath(), "src\\main\\resources\\hr\\algebra\\everdell\\images");
    private static final File cardImageFolder = new File(imageFolder.getAbsolutePath(), "cards");
    private static final File cardClassFolder = new File(imageFolder.getAbsolutePath(), "critters");
    private static final String boardFileName = "board.jpg";

    public static String getAbsoluteCardImagePath(String cardName){
        File[] jpgs = cardImageFolder.listFiles((dir, name) -> name.startsWith(cardName) && name.endsWith("jpg"));
        if(Objects.requireNonNull(jpgs).length == 0){
            return null;
        }
        return jpgs[0].getAbsolutePath();
    }

    public static String getRelativeCardImagePath(String cardName){
        String imagePath = getAbsoluteCardImagePath(cardName);
        if(imagePath == null){
            return null;
        }
        return new File(cardClassFolder.getAbsolutePath()).toURI().relativize(new File(imagePath).toURI()).getPath();
    }

    private static String getBoardImagePath(){
        return imageFolder.getAbsolutePath() + File.pathSeparator + boardFileName;
    }

    private FileUtils(){}
}
