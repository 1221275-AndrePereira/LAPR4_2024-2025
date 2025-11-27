grammar FigureDSL;

program: figure EOF;

figure: dslVersionDeclaration (statement)*;

dslVersionDeclaration
    : DSL VERSION version SEMI
    ;

statement:
    declaration SEMI
    | figureInstantiation SEMI
    | command SEMI
    | controlFlowBlock
    | pauseCommand SEMI
    ;

declaration:
    droneTypeDeclaration
    | positionDeclaration
    | velocityDeclaration
    | distanceDeclaration
    ;

droneTypeDeclaration: DRONE_TYPE IDENTIFIER;
positionDeclaration: POSITION IDENTIFIER EQ vectorDec;
velocityDeclaration: VELOCITY IDENTIFIER EQ floatInput;
distanceDeclaration: DISTANCE IDENTIFIER EQ rawFloat;

figureInstantiation:
    line
    |rectangle
    |circle
    |circumference
    ;

rectangle:RECTANGLE_KEYWORD IDENTIFIER LPAREN vectorInput COMMA floatInput COMMA floatInput COMMA IDENTIFIER RPAREN;
line:LINE_KEYWORD IDENTIFIER LPAREN vectorInput COMMA floatInput COMMA IDENTIFIER RPAREN;
circle:CIRCLE_KEYWORD IDENTIFIER LPAREN vectorInput COMMA floatInput COMMA IDENTIFIER RPAREN;
circumference:CIRCUMFERENCE_KEYWORD IDENTIFIER LPAREN vectorInput COMMA floatInput IDENTIFIER RPAREN;

command:
    IDENTIFIER DOT commandCall;

commandCall:
    lightsOn
    | lightsOff
    | move
    | rotate
    ;

lightsOn: LIGHTS_ON LPAREN color RPAREN ;
lightsOff:LIGHTS_OFF;
move: MOVE LPAREN vectorInput COMMA intInput COMMA floatInput RPAREN;
rotate: ROTATE LPAREN vectorInput COMMA vectorInput COMMA floatInput COMMA floatInput RPAREN;

controlFlowBlock:
    beforeBlock
    | afterBlock
    | groupBlock
    ;

beforeBlock: BEFORE_KEYWORD (statement)* END_BEFORE;
afterBlock: AFTER_KEYWORD (statement)* END_AFTER;
groupBlock: GROUP_KEYWORD (statement)* END_GROUP;

pauseCommand: PAUSE LPAREN intInput RPAREN;


vectorInput: vector (operator vector)*;
vector:(LPAREN floatInput COMMA floatInput COMMA floatInput RPAREN) |IDENTIFIER;
vectorDec:(LPAREN rawFloat COMMA rawFloat COMMA rawFloat RPAREN);

operator:(ADD|SUB|DIV|MUL);

int_val: INT | IDENTIFIER;
float_val: number | IDENTIFIER ;

intInput: '-'? int_val (operator int_val)*;
floatInput: '-'? float_val (operator float_val)*;

rawFloat:'-'? number (operator number)*;
rawInt:'-'? INT (operator INT)*;

version:number (DOT number)*;

color:
    YELLOW_COLOR
    | GREEN_COLOR
    | RED_COLOR
    | BLUE_COLOR
    ;

// Keywords
DSL: 'DSL';
VERSION: 'version';
DRONE_TYPE: 'DroneType';
POSITION: 'Position';
VELOCITY: 'Velocity';
DISTANCE: 'Distance';

LINE_KEYWORD: 'Line';
RECTANGLE_KEYWORD: 'Rectangle';
CIRCLE_KEYWORD: 'Circle';
CIRCUMFERENCE_KEYWORD: 'Circumference';

BEFORE_KEYWORD: 'before';
END_BEFORE: 'endbefore';
AFTER_KEYWORD: 'after';
END_AFTER: 'endafter';
GROUP_KEYWORD: 'group';
END_GROUP: 'endgroup';
PAUSE: 'pause';

LIGHTS_ON: 'lightsOn';
LIGHTS_OFF: 'lightsOff';
MOVE: 'move';
ROTATE: 'rotate';
MOVE_POS: 'movePos';

PI_CONST: 'PI';

YELLOW_COLOR: 'YELLOW';
GREEN_COLOR: 'GREEN';
RED_COLOR: 'RED';
BLUE_COLOR: 'BLUE';

IDENTIFIER: [a-zA-Z_] [a-zA-Z0-9_]*;

number
    : INT
    | FLOAT
    |PI_CONST
    ;

INT
    : INTEGER
    ;

FLOAT
    : FLOATING
    ;

SEMI: ';';
LPAREN: '(';
RPAREN: ')';
EQ: '=';
DOT: '.';
COMMA: ',';
MUL: '*' ;
DIV: '/' ;
ADD: '+' ;
SUB: '-' ;

WS: [ \t\r\n]+ -> skip;

fragment INTEGER: '0' | [1-9] DIGIT*;
fragment FLOATING: INT DOT DIGIT*;
fragment DIGIT: [0-9];