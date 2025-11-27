package eapli.shodrone.integrations.plugins.dsl.translators.droneOne;

import eapli.shodrone.integrations.plugins.dsl.FigureDSLBaseVisitor;
import eapli.shodrone.integrations.plugins.dsl.FigureDSLParser;
import org.antlr.v4.runtime.*;
import java.util.ArrayList;
import java.util.List;


public class DroneOneCodeGeneratingVisitor extends FigureDSLBaseVisitor<List<String>> {

    private List<String> droneOneCommands = new ArrayList<>();

    public DroneOneCodeGeneratingVisitor() {
    }

    private List<String> singleCommand(String command) {
        List<String> commands = new ArrayList<>();
        if (command != null && !command.isEmpty()) {
            commands.add(command);
        }
        return commands;
    }

    private void addCommands(List<String> commandsToAdd) {
        if (commandsToAdd != null) {
            this.droneOneCommands.addAll(commandsToAdd);
        }
    }

    @Override
    protected List<String> defaultResult() {
        return new ArrayList<>();
    }

    @Override
    public List<String> visitProgram(FigureDSLParser.ProgramContext ctx) {
        addCommands(visit(ctx.figure()));
        return this.droneOneCommands;
    }

    @Override
    public List<String> visitFigure(FigureDSLParser.FigureContext ctx) {
        List<String> figureCommands = new ArrayList<>();
        if (ctx.dslVersionDeclaration() != null) {
            figureCommands.addAll(visit(ctx.dslVersionDeclaration()));
        }
        for (FigureDSLParser.StatementContext stmtCtx : ctx.statement()) {
            figureCommands.addAll(visit(stmtCtx));
        }
        return figureCommands;
    }

    @Override
    public List<String> visitDslVersionDeclaration(FigureDSLParser.DslVersionDeclarationContext ctx) {
        return singleCommand("DSL version " + ctx.version().getText() +";");
    }

    @Override
    public List<String> visitStatement(FigureDSLParser.StatementContext ctx) {
        if (ctx.declaration() != null) return visit(ctx.declaration());
        if (ctx.figureInstantiation() != null) return visit(ctx.figureInstantiation());
        if (ctx.command() != null) return visit(ctx.command());
        if (ctx.controlFlowBlock() != null) return visit(ctx.controlFlowBlock());
        if (ctx.pauseCommand() != null) return visit(ctx.pauseCommand());
        return defaultResult();
    }

    @Override
    public List<String> visitDeclaration(FigureDSLParser.DeclarationContext ctx) {
        if (ctx.droneTypeDeclaration() != null) return visit(ctx.droneTypeDeclaration());
        if (ctx.positionDeclaration() != null) return visit(ctx.positionDeclaration());
        if (ctx.velocityDeclaration() != null) return visit(ctx.velocityDeclaration());
        if (ctx.distanceDeclaration() != null) return visit(ctx.distanceDeclaration());
        return defaultResult();
    }

    @Override
    public List<String> visitDroneTypeDeclaration(FigureDSLParser.DroneTypeDeclarationContext ctx) {
        String droneTypeName = ctx.IDENTIFIER().getText();
        return singleCommand("DroneType " + droneTypeName +";");
    }

    @Override
    public List<String> visitPositionDeclaration(FigureDSLParser.PositionDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        String vector = ctx.vectorDec().getText();
        return singleCommand("Position " + varName + " = " + vector + ";");
    }

    @Override
    public List<String> visitVelocityDeclaration(FigureDSLParser.VelocityDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        String value = ctx.floatInput().getText();
        String linearV = (varName + "_linear");

        List<String> commands = new ArrayList<>();
        commands.add("LinearVelocity " + linearV + " = " + value + ";");


        return commands;
    }

    @Override
    public List<String> visitDistanceDeclaration(FigureDSLParser.DistanceDeclarationContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        String value = ctx.rawFloat().getText();

        return singleCommand("Distance " + varName + " = " + value + ";");
    }

    @Override
    public List<String> visitFigureInstantiation(FigureDSLParser.FigureInstantiationContext ctx) {
        if(ctx.line() != null) {return visit(ctx.line());}
        if(ctx.rectangle() != null) {return visit(ctx.rectangle());}
        if(ctx.circle() != null) {return visit(ctx.circle());}
        if(ctx.circumference() != null) {return visit(ctx.circumference());}
        return defaultResult();
    }

    @Override
    public List<String> visitLine(FigureDSLParser.LineContext ctx) {
        String varName = ctx.IDENTIFIER(0).getText();
        String values = ("(" + ctx.vectorInput().getText() +","+ ctx.floatInput().getText()+","+ ctx.IDENTIFIER(1) + ")");

        return singleCommand("Line " + varName + values + ";");
    }

    @Override
    public List<String> visitRectangle(FigureDSLParser.RectangleContext ctx) {
        String varName = ctx.IDENTIFIER(0).getText();
        String values = ("(" + ctx.vectorInput().getText() +","+ ctx.floatInput(0).getText() +","+ ctx.floatInput(1).getText() +","+ ctx.IDENTIFIER(1) + ")");

        return singleCommand("Rectangle " + varName + values + ";");
    }

    @Override
    public List<String> visitCircle(FigureDSLParser.CircleContext ctx) {
        String varName = ctx.IDENTIFIER(0).getText();
        String values = ("(" + ctx.vectorInput().getText() +","+ ctx.floatInput().getText() +","+ ctx.IDENTIFIER(1) + ")");

        return singleCommand("Circle " + varName + values + ";");
    }

    @Override
    public List<String> visitCircumference(FigureDSLParser.CircumferenceContext ctx) {
        String varName = ctx.IDENTIFIER(0).getText();
        String values = ("(" + ctx.vectorInput().getText() +","+ ctx.floatInput().getText() +","+ ctx.IDENTIFIER(1) + ")");

        return singleCommand("Circumference " + varName + values + ";");
    }


    @Override
    public List<String> visitCommand(FigureDSLParser.CommandContext ctx) {
        List<String> generatedCommands = new ArrayList<>();
        String objectId = ctx.IDENTIFIER().getText();
        String commandCall;

        if(ctx.commandCall().lightsOn() != null) {
            commandCall = ctx.commandCall().lightsOn().getText().replace(ctx.commandCall().lightsOn().color().getText(),"");
            generatedCommands.add(objectId + "." + commandCall +";");
        }
        if(ctx.commandCall().lightsOff() != null) {
            commandCall = ctx.commandCall().lightsOff().getText();
            generatedCommands.add(objectId + "." + commandCall +"()"+";");
        }

        if(ctx.commandCall().move() != null) {
            String vector = ctx.commandCall().move().vectorInput().getText();
            String time = ctx.commandCall().move().intInput().getText();
            String velocity = ctx.commandCall().move().floatInput().getText();

            commandCall = ("move" + "("+ vector+","+ velocity+","+ time +")");
            generatedCommands.add(objectId + "." + commandCall +";");
        }
        if(ctx.commandCall().rotate() != null) {
            generatedCommands.add("// ROTATE command inst currently suported by this language");
        }

        return generatedCommands;
    }

    @Override
    public List<String> visitControlFlowBlock(FigureDSLParser.ControlFlowBlockContext ctx) {
        if (ctx.beforeBlock() != null) return visit(ctx.beforeBlock());
        if (ctx.afterBlock() != null) return visit(ctx.afterBlock());
        if (ctx.groupBlock() != null) return visit(ctx.groupBlock());
        return defaultResult();
    }

    private List<String> visitBlockStatements(List<FigureDSLParser.StatementContext> statements, String blockType) {
        List<String> blockCommands = new ArrayList<>();
        blockCommands.add(blockType);
        for (FigureDSLParser.StatementContext stmtCtx : statements) {
            blockCommands.addAll(visit(stmtCtx));
        }
        blockCommands.add("end" + blockType);
        return blockCommands;
    }

    @Override
    public List<String> visitBeforeBlock(FigureDSLParser.BeforeBlockContext ctx) {
        return visitBlockStatements(ctx.statement(), "before");
    }

    @Override
    public List<String> visitAfterBlock(FigureDSLParser.AfterBlockContext ctx) {
        return visitBlockStatements(ctx.statement(), "after");
    }

    @Override
    public List<String> visitGroupBlock(FigureDSLParser.GroupBlockContext ctx) {
        return visitBlockStatements(ctx.statement(), "group");
    }

    @Override
    public List<String> visitPauseCommand(FigureDSLParser.PauseCommandContext ctx) {
        String duration = ctx.intInput().getText();

        return singleCommand("hoover(" + duration + ");");
    }

}
