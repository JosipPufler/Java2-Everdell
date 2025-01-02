package hr.algebra.everdell.utils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class DocumentationUtils {
    private static final String BASE_PATH = "target/classes/";
    private static final String HTML_DOCUMENTATION_FILE_NAME = "doc/documentation.html";
    private static final String CLASS_FILE_EXTENSION = ".class";

    public static void generateDocumentation() throws RuntimeException {

        try (Stream<Path> paths = Files.walk(Paths.get(BASE_PATH))) {
            List<Path> classList = paths.filter(path -> path.getFileName().toString().endsWith(CLASS_FILE_EXTENSION) &&
                            Character.isUpperCase(path.getFileName().toString().charAt(0)))
                    .toList();

            String htmlCode = generateHtmlCode(classList);

            Files.writeString(Path.of(HTML_DOCUMENTATION_FILE_NAME), htmlCode);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateHtmlCode(List<Path> classList) {
        StringBuilder sb = new StringBuilder();

        String htmlStart = """
                <!DOCTYPE html>
                <html>
                <head>
                <title>Java documentation</title>
                </head>
                <body>
                <h1>List of classes:</h1>
                """;

        sb.append(htmlStart);

        for (Path classPath : classList) {

            String className = classPath.toString()
                    .substring(BASE_PATH.length(),
                            classPath.toString().length() - CLASS_FILE_EXTENSION.length())
                    .replace("\\", ".");

            try {
                Class<?> clazz =
                        Class.forName(className);

                sb.append("<h2>Class name: ").append(className).append("</h2>");

                if(clazz.getConstructors().length > 0) {
                    sb.append("<h3>List of constructors:</h3>");

                    for (Constructor<?> constructor : clazz.getConstructors()) {
                        sb.append("<h4>Constructor: ").append(constructor).append("</h4>");
                    }
                }
                else {
                    sb.append("<h3>No constructors</h3>");
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        String htmlEnd = """
                </body>
                </html>
                """;

        sb.append(htmlEnd);

        return sb.toString();
    }
}

