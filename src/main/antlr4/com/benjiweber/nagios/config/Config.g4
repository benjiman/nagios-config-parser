grammar Config;

config  : (define | NEWLINE)* ;
define  : 'define' type '{' NEWLINE (key value NEWLINE)* '}' ;

type  : VALID_NAGIOS_IDENTIFIER;
key   : VALID_NAGIOS_IDENTIFIER;
value : (VALID_NAGIOS_IDENTIFIER | VALID_NAGIOS_VALUE )+ ;

VALID_NAGIOS_IDENTIFIER  :  [a-zA-Z_0-9]+ ;
VALID_NAGIOS_VALUE : [a-zA-Z_0-9!:/\-.,'"()*#{}]+ ;
NEWLINE : [\n\r]+ ;
WS  :   [ \t]+ -> skip;
COMMENT : '#'~[\n]+ -> skip;
MID_LINE_COMMENT : ';'~[\n]+ -> skip;
