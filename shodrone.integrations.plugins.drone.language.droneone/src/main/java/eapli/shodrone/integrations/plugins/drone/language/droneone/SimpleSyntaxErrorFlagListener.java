package eapli.shodrone.integrations.plugins.drone.language.droneone;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.List;

public class SimpleSyntaxErrorFlagListener extends BaseErrorListener {
    private boolean errorOccurred = false;
    private List<String> collectedErrors;

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) {
        this.errorOccurred = true;

        this.collectedErrors.add("Error at line " + line + ":" + charPositionInLine + " " + msg+"\n");
    }

    public boolean hasErrorOccurred() {
        return errorOccurred;
    }

    public List<String> getErrors(){
        return this.collectedErrors;
    }

    public void reset() {
        errorOccurred = false;
    }
}
