grammar ShowProposalDocument;

//Start of grammar
start
    : addressBlock
      referenceBlock
      proposalTitle
      proposalBody
      signature
      separator
      attachment
      EOF  //end of file
    ;

//--------------------------------------------PARSER RULES-------------------------------------------------------


//The address block will contain the greeting, the recipient of the proposal, the company name, the address of the company(includes street, postal code, city and country) and the vatNumber.
addressBlock
    : greeting recipientName? companyName streetAddress? postalCode? city? country? vatNumber NEWLINE+
    ;

//The greeting to the customer or customer representative(ex: 'Dear,')
greeting
       : text NEWLINE
       ;

//The name of the customer representative(optional)(ex: 'John Doe')
recipientName
       : text NEWLINE
       ;

//The name of the company(ex: 'Shodrone')
companyName
       : text NEWLINE
       ;

//The street where the company is located(ex: 'Rua ISEP')
streetAddress
       : text DIGIT* text? DIGIT? NEWLINE
       ;

//The postal code of the location of the company(ex: 1111-111)
postalCode
       : DIGIT+ '-' DIGIT+
       ;

//The street where the company is located(ex: 'Porto')
city
       : text NEWLINE
       ;

//The street where the company is located(ex: 'Portugal')
country
       : text NEWLINE
       ;

//The vatNumber of the company(ex: 111111111)
vatNumber
       : DIGIT+ NEWLINE
       ;



//---------------------------------------------------------------------------------------------------


//The reference block will indicate the number of the proposal and the proposal date
referenceBlock
    : 'Reference' WS proposalNumber WS '/' WS proposalDate NEWLINE
    ;

//The number of the show proposal
proposalNumber
    : DIGIT+
    ;

//The date in which this show proposal was created
proposalDate
    : date
    ;


//---------------------------------------------------------------------------------------------------

//The title of the show proposal message
proposalTitle
    : 'Show Proposal' NEWLINE+
    ;

//---------------------------------------------------------------------------------------------------

//The body of the show proposal message
proposalBody
    : paragraph+
    ;

//A 'paragraph'(sentence) of the show proposal message
paragraph
    : text+ urlLink? insuranceAmount? text+ NEWLINE+
    ;

//The url link of the video of the simulation for this show proposal
urlLink
    : 'http://' LETTER+
    ;

//The insurance amount of the show proposal
insuranceAmount
    : DIGIT* ' euros'
    ;


//---------------------------------------------------------------------------------------------------

//The end of the show proposal message
signature
    : 'Best regards,' NEWLINE+ signatureName NEWLINE signatureTitle NEWLINE*
    ;

//The name of the Shodrone employee that sent the message
signatureName
    : text
    ;

//The 'title' of the signature. In this case it will correspond to the position of the message sender within Shodrone(CRM Manager)
signatureTitle
    : text
    ;



//---------------------------------------------------------------------------------------------------

//The page break between the message and the attachment
separator
    : '-'+ NEWLINE+
    ;



//---------------------------------------------------------------------------------------------------

//The attachment as the name indicates will be the attachment of the proposal containing the gps location of the show, the time of day where the show will take place, it's duration and the list of drone types and figures that will be used.
attachment
    : attachmentTitle NEWLINE+
      showLocation
      showDate
      showTime
      showDuration NEWLINE+
      droneList NEWLINE+
      figureList
    ;

//The title of the attachment
attachmentTitle
    : 'Attachment' WS '–' WS text proposalNumber NEWLINE
    ;

//The location in GPS coordinates where the show will take place
showLocation
    : 'Location' WS '–' WS coordinate (',' WS coordinate)? NEWLINE
    ;


//The date in YYYY/MM/DD format in which the show will take place
showDate
    : 'Date' WS '–' WS date NEWLINE
    ;

//The time of day when the show will take place
showTime
    : 'Time' WS '–' WS time NEWLINE
    ;

//The duration of the show(in minutes)
showDuration
    : 'Duration' WS '–' WS number WS 'minutes' NEWLINE
    ;

//The list of drone types to be used in the show
droneList
    : '#List of used drones' NEWLINE
      droneItem+
    ;

//Drone type to be used in the show
droneItem
    : text WS '–' WS number WS 'units.' NEWLINE
    ;


//List of figures to be displayed in the show
figureList
    : '#List of figures' NEWLINE
      figureItem+
    ;


//Figure to be displayed in the show
figureItem
    : number WS '–' WS text DIGIT+ NEWLINE?
    ;


//--------------------------------------------EXTRA PARSER RULES-------------------------------------------------------


date
    : DIGIT+ '/' DIGIT+ '/' DIGIT+
    ;

time
    : DIGIT+ ':' DIGIT+
    ;

coordinate
    : '-'? DIGIT+ '.' DIGIT+
    ;

number
    : DIGIT+
    ;

text
: (LETTER | WS | PUNCT|'/' | ','|'.'|'-'|':')+
    ;


//--------------------------------------------LEXER-------------------------------------------------------


//Lexer Rules
LETTER      : [a-zA-Z];
DIGIT       : [0-9];
WS          : [ \t]+;
NEWLINE     : '\r'? '\n';
UNKNOWN     : . -> skip;
PUNCT       : [.,!?;:()/\-@#$%&*"'_©];

