package eapli.shodrone.figure.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.shodrone.figure.domain.*;
import eapli.shodrone.figure.repository.FigureRepository;
import eapli.shodrone.figurecategory.domain.FigureCategory;
import eapli.shodrone.infrastructure.persistence.PersistenceContext;
import eapli.shodrone.shodroneusermanagement.domain.ShodroneUser;
import utils.FileSelector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UseCaseController
public class AddFigureController {

    AuthorizationService authz = AuthzRegistry.authorizationService();
    private final FigureRepository repo = PersistenceContext.repositories().figures();
    SystemUser loggedUser;

    public AddFigureController () {

        if(authz.hasSession()) {
            defineAuthor(authz.session()
                    .orElseThrow(() -> new IllegalStateException("No user authenticated in the current session."))
                    .authenticatedUser()
            );
        }
    }

    public Figure addFigure(String description, Set<String> keywords, String figureVersion, FigureCategory category, String dslPath,ShodroneUser shodroneUser)
            throws IOException {

        String dslVersionString;
        FileSelector fileSelector = new FileSelector();
        String dslCodeFilePath = fileSelector.getFullDSLPath(dslPath);

        if(alreadyExist(description,figureVersion)){
            System.out.println("Figure already exists");
            return null;
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Figure description cannot be empty.");
        }

        if (dslCodeFilePath == null || dslCodeFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("DSL code file path cannot be empty.");
        }

        String dslCodeContent;

        try {
            dslCodeContent = Files.readString(Paths.get(dslCodeFilePath), StandardCharsets.UTF_8);
            if (dslCodeContent.trim().isEmpty()) {
                throw new IllegalArgumentException("DSL code file is empty: " + dslCodeFilePath);
            }
        } catch (IOException e) {
            System.err.println("Error reading DSL file '" + dslCodeFilePath + "': " + e.getMessage());
            throw e;
        }
        dslVersionString = extractDslVersionFromContent(dslCodeContent);

        FigureBuilder builder = new FigureBuilder(loggedUser,category)
                .withDescription(description)
                .withKeywords(keywords)
                .withFigureVersion(figureVersion)
                .exclusivelyFor(shodroneUser)
                .withDsl(dslCodeFilePath, dslVersionString);

        Figure figure = builder.build();

        repo.save(figure);

        return figure;

    }

    private String extractDslVersionFromContent(String dslCodeContent) {
        try (BufferedReader reader = new BufferedReader(new StringReader(dslCodeContent))) {
            String line = reader.readLine();
            if (line != null) {
                line = line.trim();
                Pattern pattern = Pattern.compile("^DSL version ([0-9]+(\\.[0-9]+)*);$");
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    return matcher.group(1);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading DSL content for version extraction: " + e.getMessage());
        }
        throw new IllegalArgumentException("DSL version not found or in an invalid format in the first line of the DSL code. Expected format: 'DSL version x.y.z;'");
    }

    public boolean alreadyExist(String description,String version) {
        return repo.findFigureByDescriptionAndVersion(Description.valueOf(description),Version.valueOf(version)).isPresent();
    }


    public void defineAuthor(SystemUser user){
        this.loggedUser = user;
    }

}

