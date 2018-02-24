parser grammar Config;

options { tokenVocab=ConfigTokens; }

config  : (define | NEWLINE)* ;
keyvalue : (key value?);
define  : DEFINE type OPEN_BLOCK BLOCK_NEWLINE (keyvalue BLOCK_NEWLINE+)* BLOCK_CLOSE_BLOCK ;

type  : VALID_NAGIOS_IDENTIFIER;
key   : (VALID_NAGIOS_IDENTIFIER | BLOCK_VALID_NAGIOS_IDENTIFIER );
value : (VALID_NAGIOS_IDENTIFIER | BLOCK_VALID_NAGIOS_IDENTIFIER | BLOCK_VALID_NAGIOS_VALUE )+ ;

