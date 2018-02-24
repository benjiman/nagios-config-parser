lexer grammar ConfigTokens;

EMPTY_COMMENT : '#\n' -> skip;
COMMENT : '#'~[\n]+'\n' -> skip;

DEFINE : 'define' ;
OPEN_BLOCK : '{' -> pushMode(BLOCK_MODE) ;
VALID_NAGIOS_IDENTIFIER  :  [a-zA-Z_0-9]+ ;
NEWLINE : [\n\r]+ ;
WS  :   [ \t]+ -> skip;
MID_LINE_COMMENT : ';'~[\n]+ -> skip;

mode BLOCK_MODE;
BLOCK_CLOSE_BLOCK : '}' -> popMode ;
BLOCK_COMMENT : '#'~[\n]+ -> skip;
BLOCK_VALID_NAGIOS_IDENTIFIER  :  [a-zA-Z_0-9]+ ;
BLOCK_VALID_NAGIOS_VALUE : [a-zA-Z_0-9!:/\-=$&\[\].,'"()@|*%#^{}]+ ;
BLOCK_NEWLINE : [\n\r]+ ;
BLOCK_WS  :   [ \t]+ -> skip;
BLOCK_MID_LINE_COMMENT : ';'~[\n]+ -> skip;
