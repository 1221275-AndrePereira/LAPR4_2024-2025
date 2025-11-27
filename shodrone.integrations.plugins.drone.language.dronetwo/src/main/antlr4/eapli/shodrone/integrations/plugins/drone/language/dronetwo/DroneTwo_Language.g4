grammar DroneTwo_Language;


file: dslDeclaration instructions EOF;


dslDeclaration: 'DSL version' version SM;

instructions:(vDeclaration SM|command SM|group)*;

//VARIABLE_DECLARATION
vDeclaration:(dDroneType|dPoint|dVector|dLinearVelocity|dAngularVelocity|dDistance|dTime
             |dLine|dRectangle|dCircle|dCircumference);

//TYPES
dDroneType:'DroneType' STRING;
dPoint:'Point' STRING E (vPoint|vArrayPoint);
dVector:'Vector' STRING E vVector;
dLinearVelocity:'LinearVelocity' STRING E vLinearVelocity;
dAngularVelocity:'AngularVelocity' STRING E vAngularVelocity;
dDistance:'Distance' STRING E vDistance;
dTime:'Time' STRING E vTime;

//COMMON_SHAPE_TYPES
dLine:'Line' STRING LP vPoint V vDistance V vDroneType RP;
dRectangle:'Rectangle' STRING LP vPoint V vDistance V vDistance V vDroneType RP;
dCircle:'Circle' STRING LP vPoint V vDistance V vDroneType RP;
dCircumference:'Circumference' STRING LP vPoint V vDistance V vDroneType RP;

//TYPES_VALUES
vDroneType: STRING;
vPoint: vector                              |STRING (operator vPoint)?;
vArrayPoint: arrayVector                    |STRING (operator vArrayPoint)?;
vVector: vector                             |STRING (operator vVector)?;
vDistance: float_val                            |STRING (operator vDistance)?;
vTime: INT                                  |STRING (operator vTime)?;
vLinearVelocity: float_val                      |STRING (operator vLinearVelocity)?;
vAngularVelocity: float_val                     |STRING (operator vAngularVelocity)?;
vVelocity: vLinearVelocity|vAngularVelocity;

//COMMAND
command:(STRING P method|simpleMethod);

//METHODS
method:(takeOff|land|move|movePath|moveCircle|hoover|lightsOn|lightsOff|blink);

takeOff:'takeOff' LP vDistance V vVelocity RP;
land:'land' LP vVelocity RP;
move:'move'LP(vPoint V vVelocity|vVector V vVelocity V vTime)RP;
movePath:'movePath'LP vArrayPoint V vVelocity RP;
moveCircle:'moveCircle' LP (vPoint V float_val V vAngularVelocity| vPoint V vTime V vAngularVelocity) RP;
hoover:'hoover' LP vTime RP;
lightsOn:'lightsOn' LP COLOR RP;
lightsOff:'lightsOff' LP RP;
blink:'blink' LP vTime RP;

//SIMPLE_METHODS
simpleMethod:(sHoover);

sHoover:'hoover' LP vTime RP;

//GROUP
group:(beforeGroup | afterGroup | nGroup);

beforeGroup:BEFORE instructions ENDBEFORE;
afterGroup:AFTER instructions ENDAFTER;
nGroup: GROUP instructions ENDGROUP;


//VALUES_TEMPLATE
vectorSum:(vector (operator posVector)+);
vector: posVector|negVector;
posVector: LP float_val V float_val V float_val RP;
negVector: M posVector;
arrayVector: LP vector (V vector)* RP;

floatOp: M? posFloat (operator posFloat)?;
float_val:(posFloat|negFloat|floatOp);
posFloat: (INT (P INT)?|PI);
negFloat: M posFloat;

version:INT (P INT)*;
operator:('*'|'/'|'+'|M);


//KEYWORDS
BEFORE: 'before';
AFTER: 'after';
GROUP: 'group';
ENDBEFORE: 'endbefore';
ENDAFTER: 'endafter';
ENDGROUP: 'endgroup';

DSLVER:'DSL version';

//LEXER
COLOR: ('RED'| 'BLUE' | 'GREEN' | 'YELLOW');
PI:'PI';
STRING: ([a-z]|[A-Z])([a-z]|[A-Z]|[0-9]|'_')*;
INT:[0-9]+;


SM:';';
V:',';
P:'.';
E:'=';
LP:'(';
RP:')';
M:'-';

WS: [ \t\r\n]+ -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;