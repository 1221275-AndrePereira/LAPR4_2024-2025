package eapli.shodrone.integrations.plugins.dsl;

import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom error listener for capturing syntax errors during parsing with ANTLR.
 * This implementation extends the {@code BaseErrorListener} and collects
 * all syntax errors in a list for later retrieval.
 * <p>
 * The {@code SyntaxErrorListener} is particularly useful in scenarios where
 * the default error reporting mechanisms of ANTLR are not enough or need
 * to be customized, such as when aggregating errors for display or logging.
 *
 * @author Daniil Pogorielov
 */
public class SyntaxErrorListener extends BaseErrorListener {

    private final List<String> syntaxErrors = new ArrayList<>();

    public List<String> getSyntaxErrors() {
        return syntaxErrors;
    }

    @Override
    public void syntaxError(
            Recognizer<?, ?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e
            ) {
        String error = String.format("Syntax Error at line %d:%d - %s", line, charPositionInLine, msg);
        syntaxErrors.add(error);
    }
}