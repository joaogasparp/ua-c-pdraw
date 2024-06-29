grammar pdrawGrammar;

program: stat* EOF;

stat: expr ';'          
    | penDefinition ';'
    | variableAssignment ';'
    | ifStatement   ';'    
    | loopStatement ';'   
    | forStatement  ';'   
    ;

ifStatement: 'if' '(' value ')' '{' (stat)+ '}';

loopStatement: 'until' '(' value ')' '{' (stat)+ '}';

forStatement: 'for' '(' expr ';' value ';' expr ')' '{' (stat)+ '}';

expr: penCreation
    | penAction
    | penValueAtribution
    | defineCanvas
    | executeProgram
    | assignmentExpression
    | readStatement        
    | writeStatement    
    | typeConversion      
    | pauseStatement     
    ;

penDefinition: 'define pen' ID '{' penAttribute+ '}' 
    ;

penAttribute: attribute '=' value ';'
    ;


penCreation: 'pen' ID '=' 'new' ID 
    | 'pen' ID '=' 'new' 
    ;

penAction: ID penCommand+    
    ;

penCommand: vertical pauseCommand?          #Penvertical
    | movement value pauseCommand?          #PenMovement
    | rotate value  pauseCommand?           #PenRotate
    ;

pauseCommand: 'pause' NUMERIC;

vertical: 'up'
    | 'down'
    ;

movement: 'forward'
    | 'backward'
    ;

rotate: 'left'
    | 'right'
    ;


penValueAtribution: ID '<-' attribute value;


attribute: 'color'
    | 'position'
    | 'orientation'
    | 'thickness'   
    | 'pressure'
    ;

defineCanvas: 'define' 'canvas' ID STRING ('('value ',' value ')')? ;


executeProgram: ID '<-' 'execute' STRING ;

variableAssignment: type assignmentExpression (',' assignmentExpression)*  
                |   type ID 
                ;

assignmentExpression: ID '=' attributionExpression;

attributionExpression: value
                    |  typeConversion
                    ; 


readStatement: ID '<-' 'read' '(' STRING ')' ;

writeStatement: value '->' 'stdout' ;

pauseStatement: 'pause' NUMERIC ;

condition: condition op=('and' | 'or') condition
        | value ('==' | '!=' | '<' | '<=' | '>' | '>=') value
        | value
        ;



// ESTA BEM PARA BAIXO

ponto: '(' value ',' value ')';

color: ('white' | 'black' | 'red' | 'green' | 'blue')   #ColorName
    | '#' HEX                                           #ColorHex
    ;


typeConversion: type '(' value ')';

type: 'int' | 'real' | 'string' | 'bool';


inputStatement: 'stdin'  STRING ;


value: '(' value ')'                                            #ValueExprParentises  
    |   value op=('*' | '/' | '//' | '\\\\') value              #ValueExprAritmetica  
    |   value op=('+' | '-') value                              #ValueExprSomDiv
    |   value op=('and' | 'or') value                           #ValueCondAndOr
    |   value ('==' | '!=' | '<' | '<=' | '>' | '>=') value     #ValueCondSymb
    |   NUMERIC                                                 #ValueNum
    |   typeConversion                                          #ValueTypeConvert
    |   color                                                   #ValueColor
    |   inputStatement                                          #ValueInput
    |   boolean                                                 #ValueBool
    |   STRING                                                  #ValueString
    |   ponto                                                   #ValuePonto
    |   DEGREE                                                  #ValueDegree
    |   ID                                                      #ValueID
    ;

boolean: 'true' | 'false';

NUMERIC: '-'? [0-9]+ ('.' [0-9]+)?;

HEX: [0-9a-fA-F]+;
 
DEGREE: [0-9]+ 'º';

ID: [a-zA-Z_][a-zA-Z_0-9]*;

STRING: '"' .*? '"';

WS: [ \n\t\r]+ -> skip;

COMMENT: '%' .*? '\n' -> skip;

